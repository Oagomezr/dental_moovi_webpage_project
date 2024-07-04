package com.dentalmoovi.website.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dentalmoovi.website.models.dtos.CategoriesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.entities.ActivityLogs;
import com.dentalmoovi.website.models.entities.Categories;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.responses.CategoriesResponse;
import com.dentalmoovi.website.repositories.ActivityLogsRep;
import com.dentalmoovi.website.repositories.CategoriesRep;

@Service
public class CategoriesSer {
    private final CategoriesRep categoriesRep;
    private final UserSer userSer;
    private final ActivityLogsRep activityLogsRep;

    public CategoriesSer(CategoriesRep categoriesRep, UserSer userSer, ActivityLogsRep activityLogsRep){
        this.categoriesRep = categoriesRep;
        this.userSer = userSer;
        this.activityLogsRep = activityLogsRep;
    }

    @Cacheable(cacheNames = "getAllCategories")
    public CategoriesResponse getAllCategories(){
        List<Categories> parentCategories = categoriesRep.findParentCategories();
        List<CategoriesDTO> parentCategoriesDTO = new ArrayList<>();
        parentCategories.stream().forEach(parentCategory -> {
            List<String> itself = List.of(parentCategory.name());
            List<CategoriesDTO> subCategories = getSubCategories(parentCategory, itself);
            CategoriesDTO parentCategoryDTO = new CategoriesDTO(List.of(parentCategory.name()), subCategories);
            parentCategoriesDTO.add(parentCategoryDTO);
        });
        return new CategoriesResponse(parentCategoriesDTO);
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO updateCategoryName(String categoryName, String newName){
        Categories category = getCategoryByName(categoryName);
        categoriesRep.save(new Categories(category.id(), newName, category.idParentCategory()));

        Users user = userSer.getUserAuthenticated();

        ActivityLogs log = new ActivityLogs(null, "El usuario actualizo el nombre de la categoria "+categoryName+" a "+newName+" "+category.id(), LocalDateTime.now(), user.id());
        activityLogsRep.save(log);

        return new MessageDTO("Category updated");
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO updateCategoryPosition(String categoryName, String newPosition){
        Users user = userSer.getUserAuthenticated();

        String logMessage = "";

        Categories category = getCategoryByName(categoryName);
        if (newPosition.equals("-")) {
            Categories categoryUpdated = new Categories(category.id(), category.name(), null);
            categoriesRep.save(categoryUpdated);
            logMessage = "El usuario cambio la posicion de la categoria "+categoryName+" al de una categoria principal "+category.id();
        }else{
            Categories newParentCategory = getCategoryByName(newPosition);

            String currentParentCategory = category.idParentCategory() != null ? 
                categoriesRep.findById(category.idParentCategory())
                    .orElseThrow(() -> new RuntimeException("Category not found")).name()
                : "Categoria principal";
            
            Categories categoryUpdated = new Categories(category.id(), category.name(), newParentCategory.id());
            categoriesRep.save(categoryUpdated);
            logMessage = "El usuario cambio la posicion de la categoria "+categoryName+" de "+currentParentCategory+" a "+newPosition+" "+category.id();
        }

        ActivityLogs log = new ActivityLogs(null, logMessage, LocalDateTime.now(), user.id());
        activityLogsRep.save(log);

        return new MessageDTO("Category updated");
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO addCategory(String parentCategoryName, String newCategoryName){
        Users user = userSer.getUserAuthenticated();
        String logMessage = "";

        if (parentCategoryName.equals("-")) {
            categoriesRep.save(new Categories(null, newCategoryName, null));
            logMessage = "El usuario creo la categoria "+newCategoryName+" como categoria principal";
        }else{
            Categories parentCategory = getCategoryByName(parentCategoryName);
            categoriesRep.save(new Categories(null, newCategoryName, parentCategory.id()));
            logMessage = "El usuario creo la categoria "+newCategoryName+" como subcategoria de "+parentCategory.name();
        }

        ActivityLogs log = new ActivityLogs(null, logMessage, LocalDateTime.now(), user.id());
        activityLogsRep.save(log);

        return new MessageDTO("Category created");
    }

    @CacheEvict(cacheNames = {"getAllCategories", "productsByCategory"}, allEntries = true)
    public MessageDTO deleteCategory(String categoryName){
        Categories category = getCategoryByName(categoryName);
        Users user = userSer.getUserAuthenticated();
        String logMessage = "";

        if (category.idParentCategory() != null) {
            logMessage = "El usuario elimino la categoria principal "+categoryName;
        } else {
            String parentCategoryName = categoriesRep.findById(category.idParentCategory())
                .orElseThrow(() -> new RuntimeException("Category not found")).name();
            logMessage = "El usuario elimino la subCategoria "+categoryName+" de "+parentCategoryName;
        }

        ActivityLogs log = new ActivityLogs(null, logMessage, LocalDateTime.now(), user.id());
        activityLogsRep.save(log);

        categoriesRep.delete(category);
        return new MessageDTO("Category deleted");
    }

    private List<CategoriesDTO> getSubCategories(Categories parentCategory, List<String> parents) {
        List<Categories> subCategories = categoriesRep.findByParentCategory(parentCategory.id());
        List<CategoriesDTO> subCategoriesDTO = new ArrayList<>();
        if(subCategories.isEmpty()) return subCategoriesDTO;  

        subCategories.stream().forEach(subCategory ->{
            List<String> itselfAndParents = new ArrayList<>(parents);
            itselfAndParents.add(0, subCategory.name());
            List<CategoriesDTO> subcategoriesOfSubcategory = getSubCategories(subCategory, itselfAndParents);
            CategoriesDTO subCategoryDTO = new CategoriesDTO(itselfAndParents, subcategoriesOfSubcategory);
            subCategoriesDTO.add(subCategoryDTO);
        });
        return subCategoriesDTO;
    }

    private Categories getCategoryByName(String categoryName){
        return categoriesRep.findByName(categoryName)
            .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
