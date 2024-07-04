package com.dentalmoovi.website;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Products;
import com.dentalmoovi.website.models.entities.Roles;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.GenderList;
import com.dentalmoovi.website.models.entities.enums.RolesList;
import com.dentalmoovi.website.models.exceptions.ImageLoadingException;
import com.dentalmoovi.website.repositories.CategoriesRep;
import com.dentalmoovi.website.repositories.ImgRep;
import com.dentalmoovi.website.repositories.ProductsRep;
import com.dentalmoovi.website.repositories.RolesRep;
import com.dentalmoovi.website.repositories.UserRep;

import jakarta.annotation.PostConstruct;

@Component
@Profile("test")
public class InitialTestData {
    private final CategoriesRep categoriesRep;
    private final ProductsRep productsRep;
    private final ImgRep imagesRep;
    private final RolesRep rolesRep;
    private final UserRep userRep;

    public InitialTestData(CategoriesRep categoriesRep, ProductsRep productsRep, ImgRep imagesRep, RolesRep rolesRep,
            UserRep userRep) {
        this.categoriesRep = categoriesRep;
        this.productsRep = productsRep;
        this.imagesRep = imagesRep;
        this.rolesRep = rolesRep;
        this.userRep = userRep;
    }

    @Value("${spring.mail.otherPassword}")
    private String password;

    @Value("${spring.mail.username}")
    private String email;

    @PostConstruct
    public void init() throws ImageLoadingException{

        //Roles Part ----------------------------------------------------------------------------------------------
        

        rolesRep.save(new Roles(null, RolesList.USER_ROLE));
        rolesRep.save(new Roles(null, RolesList.ADMIN_ROLE));

        //User Part ---------------------------------------------------------------
        UserDTO dentalMooviDTO = new UserDTO(null, "Dental", "Moovi", email, "314-453-6435", null, GenderList.UNDEFINED, "123456", password);

            //create admin role
            Roles adminRole = rolesRep.findByRole(RolesList.ADMIN_ROLE)
                .orElseThrow(() -> new RuntimeException("Role not found"));
            Roles userRole = rolesRep.findByRole(RolesList.USER_ROLE)
                .orElseThrow(() -> new RuntimeException("Role not found"));

            //encrypt the password
            String hashedPassword = new BCryptPasswordEncoder().encode(dentalMooviDTO.password()); 

            //set and save user
            Users adminUser = 
                new Users(
                    null, dentalMooviDTO.firstName(), dentalMooviDTO.lastName(), dentalMooviDTO.email(), 
                    dentalMooviDTO.celPhone(), null, dentalMooviDTO.gender(), hashedPassword, 
                    null, null);

            adminUser.addRole(userRole);
            adminUser.addRole(adminRole);
            
            adminUser = userRep.save(adminUser);

        // Parent categories
        Categories desechables = categoriesRep.save(new Categories(null, "DESECHABLES", null));
        Categories cuidadoDental = categoriesRep.save(new Categories(null, "HIGIENE ORAL", null));
        Categories instrumentacionDental = categoriesRep.save(new Categories(null, "INSTRUMENTACIÓN DENTAL", null));
            // Dental instrumentation sub-categories
            Categories hojaBisturi = categoriesRep.save(new Categories(null, "HOJA DE BISTURÍ", instrumentacionDental.id()));
            Categories jeringas = categoriesRep.save(new Categories(null, "JERINGAS",  instrumentacionDental.id()));
            Categories mangoBisturi = categoriesRep.save(new Categories(null, "MANGOS PARA BISTURÍ",  instrumentacionDental.id()));
            Categories pinzasDental = categoriesRep.save(new Categories(null, "PINZAS DENTALES",  instrumentacionDental.id()));
            Categories portaAgujas = categoriesRep.save(new Categories(null, "PORTA AGUJAS", instrumentacionDental.id()));
            Categories tijeras = categoriesRep.save(new Categories(null, "TIJERAS", instrumentacionDental.id()));
        Categories ortodoncia = categoriesRep.save(new Categories(null, "ORTODONCIA", null));
            // Orthodontics sub-categories
            Categories alambres = categoriesRep.save(new Categories(null, "ALAMBRES", ortodoncia.id()));
            Categories arcos = categoriesRep.save(new Categories(null, "ARCOS", ortodoncia.id()));
            Categories auxiliares = categoriesRep.save(new Categories(null, "AUXILIARES", ortodoncia.id()));
            Categories brakets = categoriesRep.save(new Categories(null, "BRACKETS", ortodoncia.id()));
                // Brakets sub-categories
                /* Categories carriere = */ categoriesRep.save(new Categories(null, "CARRIERE", brakets.id()));
                /* Categories deltaForce = */ categoriesRep.save(new Categories(null, "DELTA FORCE", brakets.id()));
                //Categories estandar = categoriesRep.save(new Categories(null, "ESTANDAR", brakets));
                Categories mbt = categoriesRep.save(new Categories(null, "MBT", brakets.id()));
                Categories roth = categoriesRep.save(new Categories(null, "ROTH", brakets.id()));
            Categories distalizador = categoriesRep.save(new Categories(null, "DISTALIZADOR", ortodoncia.id()));
            Categories elastomeros = categoriesRep.save(new Categories(null, "ELASTOMEROS", ortodoncia.id()));
            Categories instrumentosOrtodonticos = categoriesRep.save(new Categories(null, "INSTRUMENTOS ORTODONTICOS", ortodoncia.id()));
            /* Categories microImplantes = */ categoriesRep.save(new Categories(null, "MICROIMPLANTES", ortodoncia.id()));
            Categories pinzasOrtodoncia = categoriesRep.save(new Categories(null, "PINZAS ORTODONCIA", ortodoncia.id()));
            Categories tubos = categoriesRep.save(new Categories(null, "TUBOS", ortodoncia.id()));
        Categories ortopedia = categoriesRep.save(new Categories(null, "ORTOPEDIA", null));
        Categories rehabilitacionOral = categoriesRep.save(new Categories(null, "REHABILITACIÓN ORAL", null));
            // Oral rehabilitation sub-categories
            Categories adhesivos = categoriesRep.save(new Categories(null, "ADHESIVOS", rehabilitacionOral.id()));
            Categories bisacrilicos = categoriesRep.save(new Categories(null, "BISACRÍLICOS", rehabilitacionOral.id()));
            Categories cemento = categoriesRep.save(new Categories(null, "CEMENTO", rehabilitacionOral.id()));
            Categories compomeros = categoriesRep.save(new Categories(null, "COMPOMEROS", rehabilitacionOral.id()));
            /* Categories postes = */ categoriesRep.save(new Categories(null, "POSTES", rehabilitacionOral.id()));
            Categories provisionales = categoriesRep.save(new Categories(null, "PROVICIONALES", rehabilitacionOral.id()));
            Categories rebases = categoriesRep.save(new Categories(null, "REBASES", rehabilitacionOral.id()));
            Categories reconstructor = categoriesRep.save(new Categories(null, "RECONSTRUCTOR", rehabilitacionOral.id()));
            Categories resinas = categoriesRep.save(new Categories(null, "RESINAS", rehabilitacionOral.id()));

        
        Products adhesivo1 =  productsRep.save(new Products(null, "Adhesivo1", "description adhesivo1", "descripción corta", 13000,4, true, true, null, adhesivos.id()));
        Products alambre1 =  productsRep.save(new Products(null, "Alambre1", "description alambre1", "descripción corta", 13000,4, true, true, null, alambres.id()));
        Products alambre2 =  productsRep.save(new Products(null, "Alambre2", "description alambre2", "descripción corta", 13000,4, true, true, null, alambres.id()));
        Products arco1 =  productsRep.save(new Products(null, "Arco1", "description arco1", "descripción corta", 13000,4, true, true, null, arcos.id()));
        Products arco2 =  productsRep.save(new Products(null, "Arco2", "description arco2", "descripción corta", 13000,4, true, true, null, arcos.id()));
        Products auxiliar1 =  productsRep.save(new Products(null, "Auxiliar1", "description Auxiliar1", "descripción corta", 13000,4, true, true, null, auxiliares.id()));
        Products auxiliar2 =  productsRep.save(new Products(null, "auxiliar2", "description auxiliar2", "descripción corta", 13000,4, true, true, null, auxiliares.id()));
        Products bisacrilico1 =  productsRep.save(new Products(null, "bisacrilico1", "description bisacrilico1", "descripción corta", 13000,4, true, true, null, bisacrilicos.id()));
        Products bisacrilico2 =  productsRep.save(new Products(null, "bisacrilico2", "description bisacrilico2", "descripción corta", 13000,4, true, true, null, bisacrilicos.id()));
        Products carrierBraket1 =  productsRep.save(new Products(null, "carrierBraket1", "description carrierBraket1", "descripción corta", 13000,4, true, true, null, brakets.id()));
        Products cemento1 =  productsRep.save(new Products(null, "cemento1", "description cemento1", "descripción corta", 13000,4, true, true, null, cemento.id()));
        Products cepilloOral =  productsRep.save(new Products(null, "cepilloOral", "description cepilloOral", "descripción corta", 13000,4, true, true, null, cuidadoDental.id()));
        Products compomero1 =  productsRep.save(new Products(null, "compomero1", "description compomero1", "descripción corta", 13000,4, true, true, null, compomeros.id()));
        Products compomero2 =  productsRep.save(new Products(null, "compomero2", "description compomero2", "descripción corta", 13000,4, true, true, null, compomeros.id()));
        Products deltaForceBraket1 =  productsRep.save(new Products(null, "deltaForceBraket1", "description deltaForceBraket1", "descripción corta", 13000,4, true, true, null, brakets.id()));
        Products distalizador1 =  productsRep.save(new Products(null, "distalizador1", "description distalizador1", "descripción corta", 13000,4, true, true, null, distalizador.id()));
        Products distalizador2 =  productsRep.save(new Products(null, "distalizador2", "description distalizador2", "descripción corta", 13000,4, true, true, null, distalizador.id()));
        Products elastomeros1 =  productsRep.save(new Products(null, "elastomeros1", "description elastomeros1", "descripción corta", 13000,4, true, true, null, elastomeros.id()));
        Products elastomeros2 =  productsRep.save(new Products(null, "elastomeros2", "description elastomeros2", "descripción corta", 13000,4, true, true, null, elastomeros.id()));
        Products estandarBraket1 =  productsRep.save(new Products(null, "estandarBraket1", "description estandarBraket1", "descripción corta", 13000,4, true, true, null, brakets.id()));
        Products estandarBraket2 =  productsRep.save(new Products(null, "estandarBraket2", "description estandarBraket2", "descripción corta", 13000,4, true, true, null, brakets.id()));
        Products guantesLatex =  productsRep.save(new Products(null, "guantesLatex", "description guantesLatex", "descripción corta", 13000,4, true, true, null, desechables.id()));
        Products hojaBisturi1 =  productsRep.save(new Products(null, "hojaBisturi1", "description hojaBisturi1", "descripción corta", 13000,4, true, true, null, hojaBisturi.id()));
        Products hojaBisturi2 =  productsRep.save(new Products(null, "hojaBisturi2", "description hojaBisturi2", "descripción corta", 13000,4, true, true, null, hojaBisturi.id()));
        Products instrumentoOrtodoncia1 =  productsRep.save(new Products(null, "instrumentoOrtodoncia1", "description instrumentoOrtodoncia1", "descripción corta", 13000,4, true, true, null, instrumentosOrtodonticos.id()));
        Products instrumentoOrtodoncia2 =  productsRep.save(new Products(null, "instrumentoOrtodoncia2", "description instrumentoOrtodoncia2", "descripción corta", 13000,4, true, true, null, instrumentosOrtodonticos.id()));
        Products jeringa1 =  productsRep.save(new Products(null, "jeringa1", "description jeringa1", "descripción corta", 13000,4, true, true, null, jeringas.id()));
        Products jeringa2 =  productsRep.save(new Products(null, "jeringa2", "description jeringa2", "descripción corta", 13000,4, true, true, null, jeringas.id()));
        Products mango1 =  productsRep.save(new Products(null, "mango1", "description mango1", "descripción corta", 13000,4, true, true, null, mangoBisturi.id()));
        Products mango2 =  productsRep.save(new Products(null, "mango2", "description mango2", "descripción corta", 13000,4, true, true, null, mangoBisturi.id()));
        Products mBTBraket1 =  productsRep.save(new Products(null, "mBTBraket1", "description mBTBraket1", "descripción corta", 13000,4, true, true, null, mbt.id()));
        Products mBTBraket2 =  productsRep.save(new Products(null, "mBTBraket2", "description mBTBraket2", "descripción corta", 13000,4, true, true, null, mbt.id()));
        Products ortopedia1 =  productsRep.save(new Products(null, "ortopedia1", "description ortopedia1", "descripción corta", 13000,4, true, true, null, ortopedia.id()));
        Products ortopedia2 =  productsRep.save(new Products(null, "ortopedia2", "description ortopedia2", "descripción corta", 13000,4, true, true, null, ortopedia.id()));
        Products pinzaDental1 =  productsRep.save(new Products(null, "pinzaDental1", "description pinzaDental1", "descripción corta", 13000,4, true, true, null, pinzasDental.id()));
        Products pinzaDental2 =  productsRep.save(new Products(null, "pinzaDental2", "description pinzaDental2", "descripción corta", 13000,4, true, true, null, pinzasDental.id()));
        Products pinzaOrtodoncia1 =  productsRep.save(new Products(null, "pinzaOrtodoncia1", "description pinzaOrtodoncia1", "descripción corta", 13000,4, true, true, null, pinzasOrtodoncia.id()));
        Products pinzaOrtodoncia2 =  productsRep.save(new Products(null, "pinzaOrtodoncia2", "description pinzaOrtodoncia2", "descripción corta", 13000,4, true, true, null, pinzasOrtodoncia.id()));
        Products portaAguja1 =  productsRep.save(new Products(null, "portaAguja1", "description portaAguja1", "descripción corta", 13000,4, true, true, null, portaAgujas.id()));
        Products portaAguja2 =  productsRep.save(new Products(null, "portaAguja2", "description portaAguja2", "descripción corta", 13000,4, true, true, null, portaAgujas.id()));
        Products protectorCepillo =  productsRep.save(new Products(null, "protectorCepillo", "description protectorCepillo", "descripción corta", 13000,4, true, true, null, cuidadoDental.id()));
        Products provisional1 =  productsRep.save(new Products(null, "provicionales1", "description provicionales1", "descripción corta", 13000,4, true, true, null, provisionales.id()));
        Products provisional2 =  productsRep.save(new Products(null, "provicionales2", "description provicionales2", "descripción corta", 13000,4, true, true, null, provisionales.id()));
        Products rebase1 =  productsRep.save(new Products(null, "rebase1", "description rebase1", "descripción corta", 13000,4, true, true, null, rebases.id()));
        Products rebase2 =  productsRep.save(new Products(null, "rebase2", "description rebase2", "descripción corta", 13000,4, true, true, null, rebases.id()));
        Products reconstructor1 =  productsRep.save(new Products(null, "reconstructor1", "description reconstructor1", "descripción corta", 13000,4, true, true, null, reconstructor.id()));
        Products reconstructor2 =  productsRep.save(new Products(null, "reconstructor2", "description reconstructor2", "descripción corta", 13000,4, true, true, null, reconstructor.id()));
        Products resina1 =  productsRep.save(new Products(null, "resina1", "description resina1", "descripción corta", 13000,4, true, true, null, resinas.id()));
        Products resina2 =  productsRep.save(new Products(null, "resina2", "description resina2", "descripción corta", 13000,4, true, true, null, resinas.id()));
        Products rothBraket1 =  productsRep.save(new Products(null, "rothBraket1", "description rothBraket1", "descripción corta", 13000,4, true, true, null, roth.id()));
        Products rothBraket2 =  productsRep.save(new Products(null, "rothBraket2", "description rothBraket2", "descripción corta", 13000,4, true, true, null, roth.id()));
        Products tapaBocas1 =  productsRep.save(new Products(null, "tapabocas1", "description tapabocas1", "descripción corta", 13000,4, true, true, null, desechables.id()));
        Products tigera1 =  productsRep.save(new Products(null, "tigera1", "description tigera1", "descripción corta", 13000,4, true, true, null, tijeras.id()));
        Products tubo1 =  productsRep.save(new Products(null, "tubo1", "description tubo1", "descripción corta", 13000,4, true, true, null, tubos.id()));
        Products tubo2 =  productsRep.save(new Products(null, "tubo2", "description tubo2", "descripción corta", 13000,4, true, true, null, tubos.id()));

        
        imagesRep.save(new Images(null, "adhesivo1-2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo2.jpg"), adhesivo1.id()));
        /* Images adhesivo13Image= */ imagesRep.save(new Images(null, "adhesivo1-3Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), adhesivo1.id()));
        /* Images adhesivo14Image= */ imagesRep.save(new Images(null, "adhesivo1-4Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), adhesivo1.id()));
        Images adhesivo1Image= imagesRep.save(new Images(null, "adhesivo1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\adhesivo1.jpg"), adhesivo1.id()));

        Images alambre1Image= imagesRep.save(new Images(null, "alambre1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre1.jpeg"), alambre1.id()));
        Images alambre2Image= imagesRep.save(new Images(null, "alambre2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\alambre2.jpg"), alambre2.id()));
        Images arco1Image= imagesRep.save(new Images(null, "arco1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco1.jpg"), arco1.id()));
        Images arco2Image= imagesRep.save(new Images(null, "arco2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\arco2.jpg"), arco2.id()));
        Images auxiliar1Image= imagesRep.save(new Images(null, "auxiliar1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar1.jpg"), auxiliar1.id()));
        Images auxiliar2Image= imagesRep.save(new Images(null, "auxiliar2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\auxiliar2.jpg"), auxiliar2.id()));
        Images bisacrilico1Image= imagesRep.save(new Images(null, "bisacrilico1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico1.jpg"), bisacrilico1.id()));
        Images bisacrilico2Image= imagesRep.save(new Images(null, "bisacrilico2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\bisacrilico2.jpg"), bisacrilico2.id()));
        Images carrierBraket1Image= imagesRep.save(new Images(null, "carrierBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\carrierBraket1.jpg"), carrierBraket1.id()));
        Images cemento1Image= imagesRep.save(new Images(null, "cemento1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\cemento1.jpg"), cemento1.id()));
        Images cepilloOralImage= imagesRep.save(new Images(null, "cepilloOralImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\CEPILLO-ORAL.jpg"), cepilloOral.id()));
        Images compomero1Image= imagesRep.save(new Images(null, "compomero1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\compomero1.png"), compomero1.id()));
        Images deltaForceBraket1Image= imagesRep.save(new Images(null, "deltaForceBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\dentalForceBraket1.jpg"), deltaForceBraket1.id()));
        Images distalizador1Image= imagesRep.save(new Images(null, "distalizador1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador1.jpg"), distalizador1.id()));
        Images distalizador2Image= imagesRep.save(new Images(null, "distalizador2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\distalizador2.jpg"), distalizador2.id()));
        Images elastomeros1Image= imagesRep.save(new Images(null, "elastomeros1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero1.jpg"), elastomeros1.id()));
        Images elastomeros2Image= imagesRep.save(new Images(null, "elastomeros2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\elastomero2.jpg"), elastomeros2.id()));
        Images estandarBraket1Image= imagesRep.save(new Images(null, "estandarBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket1.jpg"), estandarBraket1.id()));
        Images estandarBraket2Image= imagesRep.save(new Images(null, "estandarBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\estandarBraket2.jpg"), estandarBraket2.id()));
        Images guantesLatexImage= imagesRep.save(new Images(null, "guantesLatexImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\Guantes-latex.jpg"), guantesLatex.id()));
        Images hojaBisturi1Image= imagesRep.save(new Images(null, "hojaBisturi1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi1.jpg"), hojaBisturi1.id()));
        Images hojaBisturi2Image= imagesRep.save(new Images(null, "hojaBisturi2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\hojaBisturi2.jpg"), hojaBisturi2.id()));
        Images instrumentoOrtodoncia1Image= imagesRep.save(new Images(null, "instrumentoOrtodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia1.jpg"), instrumentoOrtodoncia1.id()));
        Images instrumentoOrtodoncia2Image= imagesRep.save(new Images(null, "instrumentoOrtodoncia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\instrumentoOrtodoncia2.jpg"), instrumentoOrtodoncia2.id()));
        Images jeringa1Image= imagesRep.save(new Images(null, "jeringa1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa1.jpg"), jeringa1.id()));
        Images jeringa2Image= imagesRep.save(new Images(null, "jeringa2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\jeringa2.jpg"), jeringa2.id()));
        Images mango1Image= imagesRep.save(new Images(null, "mango1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango1.jpg"), mango1.id()));
        Images mango2Image= imagesRep.save(new Images(null, "mango2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\mango2.jpg"), mango2.id()));
        Images mBTBraket1Image= imagesRep.save(new Images(null, "MBTBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket1.jpg"), mBTBraket1.id()));
        Images mBTBraket2Image= imagesRep.save(new Images(null, "MBTBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\MBTBraket2.jpg"), mBTBraket2.id()));
        Images ortopedia1Image= imagesRep.save(new Images(null, "ortopedia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia1.jpg"), ortopedia1.id()));
        Images ortopedia2Image= imagesRep.save(new Images(null, "ortopedia2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\ortopedia2.jpg"), ortopedia2.id()));
        Images pinza1Image= imagesRep.save(new Images(null, "pinza1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza1.jpg"), pinzaDental1.id()));
        Images pinza2Image= imagesRep.save(new Images(null, "pinza2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinza2.jpg"), pinzaDental2.id()));
        Images pinza1Ortodoncia1Image= imagesRep.save(new Images(null, "pinza1Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia1.jpg"), pinzaOrtodoncia1.id()));
        Images pinza2Ortodoncia1Image= imagesRep.save(new Images(null, "pinza2Ortodoncia1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\pinzaOrtodoncia2.jpg"), pinzaOrtodoncia2.id()));
        Images portaaguja1Image= imagesRep.save(new Images(null, "portaaguja1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja1.jpg"), portaAguja1.id()));
        Images portaaguja2Image= imagesRep.save(new Images(null, "portaaguja2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\portaaguja2.jpg"), portaAguja2.id()));
        Images protectorCepilloImage= imagesRep.save(new Images(null, "protectorCepilloImage", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\protectorCepillo.jpg"), protectorCepillo.id()));
        Images provisional1Image= imagesRep.save(new Images(null, "provisional1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional1.jpg"), provisional1.id()));
        Images provisional2Image= imagesRep.save(new Images(null, "provisional2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\provisional2.jpg"), provisional2.id()));
        Images rebase1Image= imagesRep.save(new Images(null, "rebase1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase1.png"), rebase1.id()));
        Images rebase2Image= imagesRep.save(new Images(null, "rebase2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rebase2.jpg"), rebase2.id()));
        Images reconstructor1Image= imagesRep.save(new Images(null, "reconstructor1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor1.jpg"), reconstructor1.id()));
        Images reconstructor2Image= imagesRep.save(new Images(null, "reconstructor2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\reconstructor2.jpg"), reconstructor2.id()));
        Images resina1Image= imagesRep.save(new Images(null, "resina1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina1.jpg"), resina1.id()));
        Images resina2Image= imagesRep.save(new Images(null, "resina2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\resina2.jpg"), resina2.id()));
        Images rothBraket1Image= imagesRep.save(new Images(null, "rothBraket1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket1.jpg"), rothBraket1.id()));
        Images rothBraket2Image= imagesRep.save(new Images(null, "rothBraket2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\rothBraket2.jpg"), rothBraket2.id()));
        Images tigera1Image= imagesRep.save(new Images(null, "tigera1Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tigera1.jpg"), tigera1.id()));
        Images tubo1Image= imagesRep.save(new Images(null, "tubo1Image", "png", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo1.png"), tubo1.id()));
        Images tubo2Image= imagesRep.save(new Images(null, "tubo2Image", "jpeg", Utils.loadImageData("C:\\Users\\dj-os\\OneDrive\\Documentos\\Spring\\web-page\\dont-used\\example-images\\tubo2.jpg"), tubo2.id()));

        adhesivo1 =  productsRep.save(new Products(adhesivo1.id(), "Adhesivo1", "description adhesivo1", "descripción corta", 13000,4, true, true, 
        adhesivo1Image.id(), adhesivos.id()));
        alambre1 =  productsRep.save(new Products(alambre1.id(), "Alambre1", "description alambre1", "descripción corta", 13000,4, true, true, 
        alambre1Image.id(), alambres.id()));
        alambre2 =  productsRep.save(new Products(alambre2.id(), "Alambre2", "description alambre2", "descripción corta", 13000,4, true, true, 
        alambre2Image.id(), alambres.id()));
        arco1 =  productsRep.save(new Products(arco1.id(), "Arco1", "description arco1", "descripción corta", 13000,4, true, true, 
        arco1Image.id(), arcos.id()));
        arco2 =  productsRep.save(new Products(arco2.id(), "Arco2", "description arco2", "descripción corta", 13000,4, true, true, 
        arco2Image.id(), arcos.id()));
        auxiliar1 =  productsRep.save(new Products(auxiliar1.id(), "Auxiliar1", "description Auxiliar1", "descripción corta", 13000,4, true, true, 
        auxiliar1Image.id(), auxiliares.id()));
        auxiliar2 =  productsRep.save(new Products(auxiliar2.id(), "auxiliar2", "description auxiliar2", "descripción corta", 13000,4, true, true, 
        auxiliar2Image.id(), auxiliares.id()));
        bisacrilico1 =  productsRep.save(new Products(bisacrilico1.id(), "bisacrilico1", "description bisacrilico1", "descripción corta", 13000,4, true, true, 
        bisacrilico1Image.id(), bisacrilicos.id()));
        bisacrilico2 =  productsRep.save(new Products(bisacrilico2.id(), "bisacrilico2", "description bisacrilico2", "descripción corta", 13000,4, true, true, 
        bisacrilico2Image.id(), bisacrilicos.id()));
        carrierBraket1 =  productsRep.save(new Products(carrierBraket1.id(), "carrierBraket1", "description carrierBraket1", "descripción corta", 13000,4, true, true, 
        carrierBraket1Image.id(), brakets.id()));
        cemento1 =  productsRep.save(new Products(cemento1.id(), "cemento1", "description cemento1", "descripción corta", 13000,4, true, true, 
        cemento1Image.id(), cemento.id()));
        cepilloOral =  productsRep.save(new Products(cepilloOral.id(), "cepilloOral", "description cepilloOral", "descripción corta", 13000,4, true, true, 
        cepilloOralImage.id(), cuidadoDental.id()));
        compomero1 =  productsRep.save(new Products(compomero1.id(), "compomero1", "description compomero1", "descripción corta", 13000,4, true, true, 
        compomero1Image.id(), compomeros.id()));
        compomero2 =  productsRep.save(new Products(compomero2.id(), "compomero2", "description compomero2", "descripción corta", 13000,4, true, true, 
        compomero2.id(), compomeros.id()));
        deltaForceBraket1 =  productsRep.save(new Products(deltaForceBraket1.id(), "deltaForceBraket1", "description deltaForceBraket1", "descripción corta", 13000,4, true, true, 
        deltaForceBraket1Image.id(), brakets.id()));
        distalizador1 =  productsRep.save(new Products(distalizador1.id(), "distalizador1", "description distalizador1", "descripción corta", 13000,4, true, true, 
        distalizador1Image.id(), distalizador.id()));
        distalizador2 =  productsRep.save(new Products(distalizador2.id(), "distalizador2", "description distalizador2", "descripción corta", 13000,4, true, true, 
        distalizador2Image.id(), distalizador.id()));
        elastomeros1 =  productsRep.save(new Products(elastomeros1.id(), "elastomeros1", "description elastomeros1", "descripción corta", 13000,4, true, true, 
        elastomeros1Image.id(), elastomeros.id()));
        elastomeros2 =  productsRep.save(new Products(elastomeros2.id(), "elastomeros2", "description elastomeros2", "descripción corta", 13000,4, true, true, 
        elastomeros2Image.id(), elastomeros.id()));
        estandarBraket1 =  productsRep.save(new Products(estandarBraket1.id(), "estandarBraket1", "description estandarBraket1", "descripción corta", 13000,4, true, true, 
        estandarBraket1Image.id(), brakets.id()));
        estandarBraket2 =  productsRep.save(new Products(estandarBraket2.id(), "estandarBraket2", "description estandarBraket2", "descripción corta", 13000,4, true, true, 
        estandarBraket2Image.id(), brakets.id()));
        guantesLatex =  productsRep.save(new Products(guantesLatex.id(), "guantesLatex", "description guantesLatex", "descripción corta", 13000,4, true, true, 
        guantesLatexImage.id(), desechables.id()));
        hojaBisturi1 =  productsRep.save(new Products(hojaBisturi1.id(), "hojaBisturi1", "description hojaBisturi1", "descripción corta", 13000,4, true, true, 
        hojaBisturi1Image.id(), hojaBisturi.id()));
        hojaBisturi2 =  productsRep.save(new Products(hojaBisturi2.id(), "hojaBisturi2", "description hojaBisturi2", "descripción corta", 13000,4, true, true, 
        hojaBisturi2Image.id(), hojaBisturi.id()));
        instrumentoOrtodoncia1 =  productsRep.save(new Products(instrumentoOrtodoncia1.id(), "instrumentoOrtodoncia1", "description instrumentoOrtodoncia1", "descripción corta", 13000,4, true, true, 
        instrumentoOrtodoncia1Image.id(), instrumentosOrtodonticos.id()));
        instrumentoOrtodoncia2 =  productsRep.save(new Products(instrumentoOrtodoncia2.id(), "instrumentoOrtodoncia2", "description instrumentoOrtodoncia2", "descripción corta", 13000,4, true, true, 
        instrumentoOrtodoncia2Image.id(), instrumentosOrtodonticos.id()));
        jeringa1 =  productsRep.save(new Products(jeringa1.id(), "jeringa1", "description jeringa1", "descripción corta", 13000,4, true, true, 
        jeringa1Image.id(), jeringas.id()));
        jeringa2 =  productsRep.save(new Products(jeringa2.id(), "jeringa2", "description jeringa2", "descripción corta", 13000,4, true, true, 
        jeringa2Image.id(), jeringas.id()));
        mango1 =  productsRep.save(new Products(mango1.id(), "mango1", "description mango1", "descripción corta", 13000,4, true, true, 
        mango1Image.id(), mangoBisturi.id()));
        mango2 =  productsRep.save(new Products(mango2.id(), "mango2", "description mango2", "descripción corta", 13000,4, true, true, 
        mango2Image.id(), mangoBisturi.id()));
        mBTBraket1 =  productsRep.save(new Products(mBTBraket1.id(), "mBTBraket1", "description mBTBraket1", "descripción corta", 13000,4, true, true, 
        mBTBraket1Image.id(), mbt.id()));
        mBTBraket2 =  productsRep.save(new Products(mBTBraket2.id(), "mBTBraket2", "description mBTBraket2", "descripción corta", 13000,4, true, true, 
        mBTBraket2Image.id(), mbt.id()));
        ortopedia1 =  productsRep.save(new Products(ortopedia1.id(), "ortopedia1", "description ortopedia1", "descripción corta", 13000,4, true, true, 
        ortopedia1Image.id(), ortopedia.id()));
        ortopedia2 =  productsRep.save(new Products(ortopedia2.id(), "ortopedia2", "description ortopedia2", "descripción corta", 13000,4, true, true, 
        ortopedia2Image.id(), ortopedia.id()));
        pinzaDental1 =  productsRep.save(new Products(pinzaDental1.id(), "pinzaDental1", "description pinzaDental1", "descripción corta", 13000,4, true, true, 
        pinza1Image.id(), pinzasDental.id()));
        pinzaDental2 =  productsRep.save(new Products(pinzaDental2.id(), "pinzaDental2", "description pinzaDental2", "descripción corta", 13000,4, true, true, 
        pinza2Image.id(), pinzasDental.id()));
        pinzaOrtodoncia1 =  productsRep.save(new Products(pinzaOrtodoncia1.id(), "pinzaOrtodoncia1", "description pinzaOrtodoncia1", "descripción corta", 13000,4, true, true, 
        pinza1Ortodoncia1Image.id(), pinzasOrtodoncia.id()));
        pinzaOrtodoncia2 =  productsRep.save(new Products(pinzaOrtodoncia2.id(), "pinzaOrtodoncia2", "description pinzaOrtodoncia2", "descripción corta", 13000,4, true, true, 
        pinza2Ortodoncia1Image.id(), pinzasOrtodoncia.id()));
        portaAguja1 =  productsRep.save(new Products(portaAguja1.id(), "portaAguja1", "description portaAguja1", "descripción corta", 13000,4, true, true, 
        portaaguja1Image.id(), portaAgujas.id()));
        portaAguja2 =  productsRep.save(new Products(portaAguja2.id(), "portaAguja2", "description portaAguja2", "descripción corta", 13000,4, true, true, 
        portaaguja2Image.id(), portaAgujas.id()));
        protectorCepillo =  productsRep.save(new Products(protectorCepillo.id(), "protectorCepillo", "description protectorCepillo", "descripción corta", 13000,4, true, true, 
        protectorCepilloImage.id(), cuidadoDental.id()));
        provisional1 =  productsRep.save(new Products(provisional1.id(), "provicionales1", "description provicionales1", "descripción corta", 13000,4, true, true, 
        provisional1Image.id(), provisionales.id()));
        provisional2 =  productsRep.save(new Products(provisional2.id(), "provicionales2", "description provicionales2", "descripción corta", 13000,4, true, true, 
        provisional2Image.id(), provisionales.id()));
        rebase1 =  productsRep.save(new Products(rebase1.id(), "rebase1", "description rebase1", "descripción corta", 13000,4, true, true, 
        rebase1Image.id(), rebases.id()));
        rebase2 =  productsRep.save(new Products(rebase2.id(), "rebase2", "description rebase2", "descripción corta", 13000,4, true, true, 
        rebase2Image.id(), rebases.id()));
        reconstructor1 =  productsRep.save(new Products(reconstructor1.id(), "reconstructor1", "description reconstructor1", "descripción corta", 13000,4, true, true, 
        reconstructor1Image.id(), reconstructor.id()));
        reconstructor2 =  productsRep.save(new Products(reconstructor2.id(), "reconstructor2", "description reconstructor2", "descripción corta", 13000,4, true, true, 
        reconstructor2Image.id(), reconstructor.id()));
        resina1 =  productsRep.save(new Products(resina1.id(), "resina1", "description resina1", "descripción corta", 13000,4, true, true, 
        resina1Image.id(), resinas.id()));
        resina2 =  productsRep.save(new Products(resina2.id(), "resina2", "description resina2", "descripción corta", 13000,4, true, true, 
        resina2Image.id(), resinas.id()));
        rothBraket1 =  productsRep.save(new Products(rothBraket1.id(), "rothBraket1", "description rothBraket1", "descripción corta", 13000,4, true, true, 
        rothBraket1Image.id(), roth.id()));
        rothBraket2 =  productsRep.save(new Products(rothBraket2.id(), "rothBraket2", "description rothBraket2", "descripción corta", 13000,4, true, true, 
        rothBraket2Image.id(), roth.id()));
        tapaBocas1 =  productsRep.save(new Products(tapaBocas1.id(), "tapabocas1", "description tapabocas1", "descripción corta", 13000,4, true, true, 
        tapaBocas1.id(), desechables.id()));
        tigera1 =  productsRep.save(new Products(tigera1.id(), "tigera1", "description tigera1", "descripción corta", 13000,4, true, true, 
        tigera1Image.id(), tijeras.id()));
        tubo1 =  productsRep.save(new Products(tubo1.id(), "tubo1", "description tubo1", "descripción corta", 13000,4, true, true, 
        tubo1Image.id(), tubos.id()));
        tubo2 =  productsRep.save(new Products(tubo2.id(), "tubo2", "description tubo2", "descripción corta", 13000,4, true, true, 
        tubo2Image.id(), tubos.id()));
        
    }   
}
