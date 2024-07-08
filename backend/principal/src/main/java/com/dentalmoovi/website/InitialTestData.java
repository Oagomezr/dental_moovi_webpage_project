package com.dentalmoovi.website;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.dentalmoovi.website.models.dtos.UserDTO;
import com.dentalmoovi.website.models.entities.Categories;
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

        
        
        
    }   
}
