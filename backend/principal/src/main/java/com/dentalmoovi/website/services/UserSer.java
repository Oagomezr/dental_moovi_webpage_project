package com.dentalmoovi.website.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.AddressesDTO;
import com.dentalmoovi.website.models.dtos.EmailData;
import com.dentalmoovi.website.models.dtos.Enum1DTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.ActivityLogs;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Enterprises;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.models.exceptions.ALotTriesException;
import com.dentalmoovi.website.models.exceptions.AlreadyExistException;
import com.dentalmoovi.website.models.exceptions.IncorrectException;
import com.dentalmoovi.website.models.responses.AddressesResponse;
import com.dentalmoovi.website.models.responses.EnumResponse1;
import com.dentalmoovi.website.models.responses.UserResponse;
import com.dentalmoovi.website.repositories.ActivityLogsRep;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.EnterprisesRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;
import com.dentalmoovi.website.security.LoginDTO;
import com.dentalmoovi.website.security.MainUser;
import com.dentalmoovi.website.security.PwDTO;
import com.dentalmoovi.website.services.cache.CacheSer;

@Service
public class UserSer {

    private final CacheSer cacheSer;
    private final UserRep userRep;
    private final RolesRep rolesRep;
    private final AddressesRep addressesRep;
    private final MunicipalyRep municipalyRep;
    private final DepartamentsRep departamentsRep;
    private final ActivityLogsRep activityLogsRep;
    private final RestTemplate restTemplate;
    private final EnterprisesRep enterprisesRep;

    private static final BCryptPasswordEncoder pwe = new BCryptPasswordEncoder();

    @Value("${spring.mail.otherPassword}")
    private String ref; 

    @Value("${server.emailService}")
    private String emailServiceUrl;

    @Async("taskExecutor")
    public void sendEmailNotification(String email, String subject, String body) {
        EmailData emailData = new EmailData(email, subject, body);
        restTemplate.postForEntity(emailServiceUrl+"/sendCode", emailData, Void.class);
    }

    public String createUser(UserDTO userDTO) throws RuntimeException {

        class CreateUser{

            /* ¡¡PLEASE PAY CLOSE ATTENTION ONLY THIS "createUser()" METHOD TO UNDERSTAND THIS SERVICE!! */ 
            String createUser() throws RuntimeException{
                //verify if email exist
                if(checkEmailExists(userDTO.email(), true)) 
                    throw new AlreadyExistException("That user already exist");
                
                validateCode(userDTO.email(), userDTO.code());

                //create default role
                Roles defaultRole = rolesRep.findByRole(RolesList.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

                //encrypt the password
                String hashedPassword = pwe.encode(userDTO.password()); 

                //get id enterprise
                Long idEnterprise = getEnterprise(userDTO.enterprise());

                //set and save user
                Users newUser = createUser(userDTO, idEnterprise, hashedPassword);
                newUser.addRole(defaultRole);
                
                newUser = userRep.save(newUser);

                ActivityLogs log = new ActivityLogs(null, "El usuario se registro en la plataforma", Utils.getNow(), newUser.id());
                activityLogsRep.save(log);

                return "User Created";
            }

            Users createUser(UserDTO userDTO, Long idEnterprise, String password) {
                if(Boolean.TRUE.equals(userRep.existsByEmailIgnoreCase(userDTO.email()))){
                    Users user = Utils.getUserByEmail(userDTO.email(), userRep);
                    return new Users(
                        user.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(), userDTO.celPhone(), 
                        userDTO.birthdate(), userDTO.gender(), password, idEnterprise, null, user.addresses());
                }
                return new Users(
                    null, userDTO.firstName(), userDTO.lastName(), userDTO.email(), userDTO.celPhone(), 
                    userDTO.birthdate(), userDTO.gender(), password, idEnterprise, null, null);
            }
        }

        CreateUser innerClass = new CreateUser();
        return innerClass.createUser();
    }

    //verify if user exist
    public boolean checkEmailExists(String value, boolean signup) {
        boolean result = userRep.existsByEmailIgnoreCase(value) && userRep.wasRegister(value);
        return signup ? result : !result;
    }

    public MessageDTO getName(){
        return new MessageDTO(getUserAuthenticated().firstName());
    }

    public Users getUserAuthenticated(){
        return Utils.getUserByEmail(getUsername(), userRep);
    }

    public boolean getIsAdmin(){
        String userDetails = getUserDetails(getUsername());
        return userDetails.charAt(0) == 'A';
    }

    public UserDTO getUserAuthDTO(){
        Users user = getUserAuthenticated();
        return getUserDTO(user);
    }

    public UserDTO getUserDTO(Long id){
        Users user = getUser(id);
        return getUserDTO(user);
    }

    public MainUser getMainUser(String email){
        Users user = Utils.getUserByEmail(email, userRep);
        return MainUser.build(user, rolesRep, ref);
    }

    @Cacheable("getUserDetails")
    public String getUserDetails(String email){
        MainUser mainUser = getMainUser(email);
        boolean isAdmin = mainUser.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                .equals(RolesList.ADMIN_ROLE.name()));
        if (isAdmin) return "A" + mainUser.getCacheRef();

        return mainUser.getCacheRef();
    }

    public MessageDTO updateUserInfoByAdmin(UserDTO userDTO) throws IllegalArgumentException{
        Users user =  getUser(userDTO.idUser());
        return updateUserInfo(userDTO, user);
    }

    public MessageDTO updateUserInfo(UserDTO userDTO) throws IllegalArgumentException{
        Users user =  getUserAuthenticated();
        return updateUserInfo(userDTO, user);
    }
    
    public AddressesResponse getAddresses(){
        Users user = getUserAuthenticated();
        return getAddresses(user);
    }

    public AddressesResponse getAddresses(Long id){
        Users user = getUser(id);
        return getAddresses(user);
    }

    private AddressesResponse getAddresses(Users user){
        List<AddressesDTO> addressesDTO = getAddressesFromDatabase(user);
        return new AddressesResponse(addressesDTO);
    }

    public String createUsers(UserDTO userDTO) throws RuntimeException {

        class CreateUser{

            String createUser() throws RuntimeException{
                //verify if email exist
                if(Boolean.TRUE.equals(userRep.existsByEmailIgnoreCase((userDTO.email()))))
                    throw new AlreadyExistException("That user already exist");

                //get id enterprise
                Long idEnterprise = getEnterprise(userDTO.enterprise());

                //set and save user
                Users newUser = 
                    new Users(
                        null, userDTO.firstName(), userDTO.lastName(), userDTO.email(), userDTO.celPhone(), 
                        userDTO.birthdate(), userDTO.gender(), null, idEnterprise, null, null);

                userRep.save(newUser);
                
                Users adminUser = getUserAuthenticated();

                ActivityLogs log = new ActivityLogs(null, "El administrador agrego un nuevo usuario: "+userDTO.email(), Utils.getNow(), adminUser.id());
                activityLogsRep.save(log);

                return "User Created";
            }
        }

        CreateUser innerClass = new CreateUser();
        return innerClass.createUser();
    }

    @CacheEvict(value = "getUserByEmail", key = "#userDTO.email")
    private MessageDTO updateUserInfo(UserDTO userDTO, Users user) throws IllegalArgumentException{

        //numberUpdates
        Utils.addTriesCache("update",user.id().toString(),3, cacheSer);

        Long idEnterprise = getEnterprise(userDTO.enterprise());

        Users userUpdated = new Users(
            user.id(), userDTO.firstName(), userDTO.lastName(), user.email(), 
            userDTO.celPhone(), userDTO.birthdate(), userDTO.gender(), 
            user.password(), idEnterprise, user.roles(), user.addresses());
        
        String logMessage = compareUpdateUserData(user,userUpdated);

        if (!logMessage.equals("El usuario actualizo sus datos personales: ")) {
            ActivityLogs log = new ActivityLogs(null, logMessage, Utils.getNow(), user.id());
            activityLogsRep.save(log);
            userRep.save(userUpdated);
        }

        return new MessageDTO("User updated");
    }



    @Cacheable(value = "getAddresses", key = "#user.id()")
    private List<AddressesDTO> getAddressesFromDatabase(Users user){
        List<Long> idsAddresses = new ArrayList<>(user.getAddressesIds());
        List<AddressesDTO> addressesDTO = new ArrayList<>();
        
        idsAddresses.stream().forEach(id ->{
            AddressesDTO addressDTO = getAddressById(id);
            addressesDTO.add(addressDTO);
        });

        return addressesDTO;
    }

    public AddressesDTO getAddress(Long id){
        Users user = getUserAuthenticated();
        List<Long> idsAddresses = new ArrayList<>(user.getAddressesIds());
        boolean belongUser = idsAddresses.stream().anyMatch(iduser -> Objects.equals(id, iduser));
        return belongUser ? getAddressById(id) : null;
    }

    public AddressesDTO getAddressById(Long id){
        Addresses address = addressesRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));

            MunicipalyCity municipaly = municipalyRep.findById(address.idMunicipalyCity())
                .orElseThrow(() -> new RuntimeException("Municipaly not found"));

            Departaments departament = departamentsRep.findById(municipaly.id_departament())
                .orElseThrow(() -> new RuntimeException("Departament not found"));

            return new AddressesDTO(
                address.id(), address.address(), address.phone(), address.description(), 
                municipaly.name(), departament.name(), 0);
    }
    
    public MessageDTO createAddress(AddressesDTO addressDTO, Long idUser){
        Users user = getUser(idUser);
        return createAddress(user, addressDTO);
    }

    public MessageDTO createAddress(AddressesDTO addressDTO){
        Users user = getUserAuthenticated();
        return createAddress(user, addressDTO);
    }

    private MessageDTO createAddress(Users user, AddressesDTO addressDTO){
        //numberUpdates
        Utils.addTriesCache("createAddress",user.id().toString(),3, cacheSer);

        Addresses address = new Addresses(
            null, addressDTO.address(), addressDTO.phone(), addressDTO.description(), addressDTO.idMunicipaly());

        return saveAddress(user, address);
    }

    @CacheEvict(value = "getAddresses", key = "#user.id()")
    private MessageDTO saveAddress(Users user, Addresses address){

        MunicipalyCity municipaly = Utils.getMunicipalyCity(address.idMunicipalyCity(), municipalyRep);
        
        Departaments departament = Utils.getDepartament(municipaly.id_departament(), departamentsRep);
        
        address = addressesRep.save(address);

        String logMessage = "Agrego una nueva direccion: "+address.address()+" "+municipaly.name()+" "+departament.name();

        ActivityLogs log = new ActivityLogs(null, logMessage, Utils.getNow(), user.id());
        activityLogsRep.save(log);
        
        user.addAddress(address);
        userRep.save(user);

        return new MessageDTO("Address created");
    }

    @CacheEvict(value = "getAddresses", key = "#user.id()")
    public MessageDTO updateAddress(AddressesDTO addressDTO){

        Users user = getUserAuthenticated();

        List<Long> idsAddresses = new ArrayList<>(user.getAddressesIds());
        boolean belongUser = idsAddresses.stream().anyMatch(iduser -> Objects.equals(addressDTO.id(), iduser));

        if (!belongUser) 
            throw new IncorrectException("That address don't belong to the user");

        return updateAddress(user, addressDTO);
    }

    @CacheEvict(value = "getAddresses", key = "#user.id()")
    public MessageDTO updateAddressByAdmin(AddressesDTO addressDTO){
        Users user = getUserAuthenticated();
        return updateAddress(user, addressDTO);
    }

    private MessageDTO updateAddress(Users user, AddressesDTO addressDTO){
        Addresses newAddress = new Addresses(
            addressDTO.id(), addressDTO.address(), addressDTO.phone(), 
            addressDTO.description(), addressDTO.idMunicipaly());

        return updateAddress(user, newAddress);
    }

    @Caching(evict = {
        @CacheEvict(value = "getAddresses", key = "#user.id()"),
        @CacheEvict(value = "getAddress", key = "#newAddress.id()")
    })
    private MessageDTO updateAddress(Users user, Addresses newAddress){
        //numberUpdates
        Utils.addTriesCache("updateAddress",user.id().toString(),3, cacheSer);

        Addresses currentAddress = Utils.getAddress(newAddress.id(), addressesRep);

        String logMessage = compareUpdateAddressData(currentAddress, newAddress);

        if (!logMessage.equals("El usuario actualizo la direccion: ")) {
            ActivityLogs log = new ActivityLogs(null, logMessage, Utils.getNow(), user.id());
            activityLogsRep.save(log);
            addressesRep.save( newAddress );
        }

        return new MessageDTO("Address updated");
    }

    @CacheEvict(value = "getAddresses", key = "#id")
    public MessageDTO deleteAddress(long id){

        Addresses address = addressesRep.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));

        MunicipalyCity municipaly = Utils.getMunicipalyCity(address.idMunicipalyCity(), municipalyRep);

        Departaments departament = Utils.getDepartament(municipaly.id_departament(), departamentsRep);

        String logMessage = "Elimino la direccion: "+address.address()+" "+municipaly.name()+" "+departament.name();
        
        Users user = getUserAuthenticated();

        //numberUpdates
        Utils.addTriesCache("deleteAddress",user.id().toString(),2, cacheSer);

        ActivityLogs log = new ActivityLogs(null, logMessage, Utils.getNow(), user.id());
        activityLogsRep.save(log);

        return deleteAddress(user, id);
    }

    @CacheEvict(value = "getAddresses", key = "#user.id()")
    private MessageDTO deleteAddress(Users user, Long id){
        user.deleteAddress(id);
        userRep.save(user);
        addressesRep.deleteById(id);
        return new MessageDTO("Address deleted");
    }

    public MessageDTO changePw(PwDTO dto){
        Users user = getUserAuthenticated();
        boolean valid = pwe.matches(dto.oldP(), user.password());

        if (valid){
            ActivityLogs log = new ActivityLogs(null, "El usuario cambio la contraseña", Utils.getNow(), user.id());
            activityLogsRep.save(log);
            return updatePw(user, dto.newP());
        }
        
        throw new IncorrectException("Incorrect Password");
    }

    public MessageDTO rememberPw(LoginDTO userCredentials){ 

        validateCode(userCredentials.userName(), userCredentials.code());

        String newPw = Utils.generateRandomString(10);

        String subject = "Recuperación de contraseña";
        String body = "Dental Moovi recibió una solicitud de recuperación de contraseña.\n\n"+
                        "Su nueva contraseña es: " + newPw + "\n\n"+
                        "Puedes cambiarla cuando quieras a travez de la pagina Web de Dental Moovi";

        Users user = Utils.getUserByEmail(userCredentials.userName(), userRep);

        EmailData emailData = new EmailData(userCredentials.userName(), subject, body);

        restTemplate.postForEntity(emailServiceUrl+"/sendEmail", emailData, Void.class);

            ActivityLogs log = new ActivityLogs(null, "El usuario solicito recordatorio de contraseña", Utils.getNow(), user.id());
            activityLogsRep.save(log);

            return updatePw(user, newPw);
    }

    public void validateCode(String key, String codeEntered){

        //get tries
        Integer tries = cacheSer.getFromNumberTries(key);
        tries = tries == null ? 0 : tries;

        if (tries > 3) 
            throw new ALotTriesException("Many tries");

        Boolean isCorrect = restTemplate.getForEntity(
            emailServiceUrl+"/isCodeCorrect/"+key+"/"+codeEntered, Boolean.class
            ).getBody();

        //verify if code sended is equals the verification code
        if(Boolean.FALSE.equals(isCorrect)) {
            cacheSer.addToOrUpdateNumberTries(key, tries+1);
            throw new IncorrectException("The code: "+codeEntered+" is incorrect");
        }

        cacheSer.removeFromNumberTries(key);
    }
    
    public Users getUser(long id){
        return Utils.getUserById(id, userRep);
    }

    public UserResponse getUsers(){
        List<Users> users = userRep.findUsers();
        List<UserDTO> userDTOs = new ArrayList<>();
        users.stream().forEach(user ->{
            UserDTO userDto = new UserDTO(
                user.id(), user.firstName(), user.lastName(), null, null, 
                null, null, null, null, null);
                userDTOs.add(userDto);
        });
        return new UserResponse(userDTOs);
    }

    public EnumResponse1 getEnterprises(String name){
        List<Enterprises> enterprises = enterprisesRep.findEnterprises(name);
        List<Enum1DTO> enterpriseDTO = new ArrayList<>();
        enterprises.stream().forEach(enterprise ->
            enterpriseDTO.add(new Enum1DTO(enterprise.id().intValue() , enterprise.name())));

        return new EnumResponse1(enterpriseDTO);
    }

    private UserDTO getUserDTO(Users user){
        String enterpriseName = user.idEnterprise() == null ? "" : Utils.getEnterprise(user.idEnterprise(), enterprisesRep).name();
        return new UserDTO(null, user.firstName(), user.lastName(), user.email(), user.celPhone(), user.birthdate(), user.gender(), null, null, enterpriseName);
    }

    private String getUsername(){
        Authentication authentication = 
            SecurityContextHolder.getContext().getAuthentication();

        // Verify if the user is authenticated
        if (authentication != null && authentication.isAuthenticated()) 
            return authentication.getName(); //Get username
        
        throw new NoSuchElementException("The user is not authenticated");
    }

    @CacheEvict(value = "getUserByEmail", key = "#user.email")
    private MessageDTO updatePw(Users user, String pw){
        String pwNew = pwe.encode(pw);
        
        userRep.save(new Users(
            user.id(), user.firstName(), user.lastName(), user.email(), user.celPhone(), 
            user.birthdate(), user.gender(), pwNew, user.idEnterprise(), user.roles(), user.addresses()));
        
        return new MessageDTO("Password updated successfully");
    }

    private String compareUpdateUserData(Users current, Users updated){
        StringBuilder log = new StringBuilder();
        int count = 1;
    
        count += compareAndAppendChange(log, count, "nombre", current.firstName(), updated.firstName());
        count += compareAndAppendChange(log, count, "apellido", current.lastName(), updated.lastName());
        count += compareAndAppendChange(log, count, "número de celular", current.celPhone(), updated.celPhone());
        count += compareAndAppendChange(log, count, "fecha de nacimiento", current.birthdate(), updated.birthdate());
        count += compareAndAppendChange(log, count, "enterprise", current.idEnterprise(), updated.idEnterprise());
        
        compareAndAppendChange(log, count, "género", current.gender(), updated.gender());
    
        return "El usuario actualizo sus datos personales: " + log.toString();
    }

    private String compareUpdateAddressData(Addresses current, Addresses updated){
        StringBuilder log = new StringBuilder();
        int count = 1;
    
        count += compareAndAppendChange(log, count, "Direccion", current.address(), updated.address());
        count += compareAndAppendChange(log, count, "Telefono", current.phone(), updated.phone());
        count += compareAndAppendChange(log, count, "Descripcion", current.description(), updated.description());

        MunicipalyCity currentMunicipaly = Utils.getMunicipalyCity(current.idMunicipalyCity(), municipalyRep);

        Departaments currentDepartament = Utils.getDepartament(currentMunicipaly.id_departament(), departamentsRep);

        MunicipalyCity newMunicipaly = Utils.getMunicipalyCity(updated.idMunicipalyCity(), municipalyRep);

        Departaments newDepartament = Utils.getDepartament(newMunicipaly.id_departament(), departamentsRep);

        count += compareAndAppendChange(log, count, "municipio", currentMunicipaly.name(), newMunicipaly.name());
        compareAndAppendChange(log, count, "departamento", currentDepartament.name(), newDepartament.name());
    
        return "El usuario actualizo la direccion: "+ log.toString();
    }
    
    private int compareAndAppendChange(StringBuilder log, int count, String field, Object currentValue, Object updatedValue) {
        Object current = currentValue==null ? "" : currentValue;
        Object updated = updatedValue==null ? "" : updatedValue;
        if (!current.equals(updated)) {
            log .append(count)
                .append(". El Campo '")
                .append(field)
                .append("' fue cambiado de '")
                .append(current)
                .append("' a '")
                .append(updated)
                .append("'\n");
                return 1;
        }
        return 0;
    }

    public UserSer(CacheSer cacheSer, UserRep userRep, RolesRep rolesRep, AddressesRep addressesRep,
            MunicipalyRep municipalyRep, DepartamentsRep departamentsRep, ActivityLogsRep activityLogsRep,
            RestTemplate restTemplate, EnterprisesRep enterprisesRep) {
        this.cacheSer = cacheSer;
        this.userRep = userRep;
        this.rolesRep = rolesRep;
        this.addressesRep = addressesRep;
        this.municipalyRep = municipalyRep;
        this.departamentsRep = departamentsRep;
        this.activityLogsRep = activityLogsRep;
        this.restTemplate = restTemplate;
        this.enterprisesRep = enterprisesRep;
    }

    private Long getEnterprise(String name){
        if (name.equals("") || name == null) return null;
        Enterprises enterprise;
        if (Boolean.TRUE.equals(enterprisesRep.existsByNameIgnoreCase(name))) {
            enterprise = enterprisesRep.findByName(name)
                .orElseThrow(() -> new RuntimeException("Enterprise not found"));
            return enterprise.id();
        }
        enterprise = enterprisesRep.save(new Enterprises(null, name));
        return enterprise.id();
    }
}
