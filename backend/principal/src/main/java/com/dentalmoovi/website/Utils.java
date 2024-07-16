package com.dentalmoovi.website;

import com.dentalmoovi.website.models.cart.CartRequest;
import com.dentalmoovi.website.models.entities.Addresses;
import com.dentalmoovi.website.models.entities.Enterprises;
import com.dentalmoovi.website.models.entities.Orders;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.entities.enums.Departaments;
import com.dentalmoovi.website.models.entities.enums.MunicipalyCity;
import com.dentalmoovi.website.models.entities.enums.StatusOrderList;
import com.dentalmoovi.website.models.exceptions.ALotTriesException;
import com.dentalmoovi.website.models.exceptions.ImageLoadingException;
import com.dentalmoovi.website.models.exceptions.JsonConversionException;
import com.dentalmoovi.website.repositories.AddressesRep;
import com.dentalmoovi.website.repositories.EnterprisesRep;
import com.dentalmoovi.website.repositories.OrdersRep;
import com.dentalmoovi.website.repositories.UserRep;
import com.dentalmoovi.website.repositories.enums.DepartamentsRep;
import com.dentalmoovi.website.repositories.enums.MunicipalyRep;
import com.dentalmoovi.website.services.cache.CacheSer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.web.util.WebUtils;

public class Utils {

    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void showMessage(String message){
        logger.info(message);
    }

    private static Random random = new Random();

    public static void createCookie(HttpServletResponse hsr, String name, String value, 
        Boolean secure, Integer maxAge, String domain){
            Cookie cookie = new Cookie(name, value);
            cookie.setSecure(secure);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(maxAge);
            cookie.setDomain(domain);
            cookie.setPath("/");
            hsr.addCookie(cookie);
    }

    @CacheEvict(cacheNames = "getUserAuthenticated", allEntries = true)
    public static void clearCookie(HttpServletResponse hsr, String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(1);
        cookie.setDomain("localhost");
        hsr.addCookie(cookie);
    }

    public static byte[] loadImageData(String imagePath) throws ImageLoadingException {
        try {
            Path path = Paths.get(imagePath);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ImageLoadingException("Error to load the image: " + imagePath, e);
        }
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static String transformToJSON(Object object) throws JsonConversionException {
        try {
            return objectMapper.writeValueAsString(object)
                    .replaceAll("[áÁäÄâÂàÀãÃ]", "Ã")
                    .replaceAll("[éÉëËêÊèÈẽẼ]", "Ã")
                    .replaceAll("[íÍïÏîÎìÌĩĨ]", "Ã")
                    .replaceAll("[óÓöÖôÔòÒõÕ]", "Ã")
                    .replaceAll("[úÚüÜûÛùÙũŨ]", "Ã");
        } catch (JsonProcessingException e) {
            throw new JsonConversionException("Error al convertir a JSON: " + e.getMessage(), e);
        }
    }

    public static Orders setOrder(StatusOrderList status, LocalDateTime date, long idUser, long idAddress, CartRequest req, OrdersRep rep){
        Orders order = new Orders(null, null, status, date, idUser, idAddress, null);
        req.data().forEach(elem ->
            order.addProduct(elem.id(), elem.amount()));
        return rep.save(order);
    }

    public static String getToken(@NonNull HttpServletRequest request, @NonNull String  cookieName){
        Cookie cookie = WebUtils.getCookie( request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    @Cacheable(value = "getUserByEmail", key = "#email")
    public static Users getUserByEmail(String email, UserRep userRep){
        return userRep.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User "+email+" not found"));
    }

    public static void addTriesCache(String label, String key, int nTries, CacheSer cacheSer){
        String keyCache = label+" "+key;
        Integer tries = cacheSer.getFromNumberTries(keyCache);
        tries = tries == null ? 0 : tries;

        if (tries > nTries) 
            throw new ALotTriesException("Many tries");

        cacheSer.addToOrUpdateNumberTries(keyCache, tries+1);
    }

    public static String generateRandomString(int length){
        // Characters that will use to generate random string
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~%$#|!&/()='";
        StringBuilder stringBuilder = new StringBuilder();

        // Generate random string
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    @Cacheable(value = "getAddress", key = "#id")
    public static Addresses getAddress(Long id, AddressesRep addressesRep){
        return addressesRep.findById(id)
            .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Cacheable(value = "getDepartament", key = "#id")
    public static Departaments getDepartament(Integer id, DepartamentsRep departamentsRep){
        return departamentsRep.findById(id)
            .orElseThrow(() -> new RuntimeException("Departament not found"));
    }

    @Cacheable(value = "getMunicipalyCity", key = "#id")
    public static MunicipalyCity getMunicipalyCity(Integer id, MunicipalyRep municipalyRep){
        return municipalyRep.findById(id)
            .orElseThrow(() -> new RuntimeException("Municipaly not found"));
    }

    public static Enterprises getEnterprise(Long id, EnterprisesRep enterprisesRep){
        return enterprisesRep.findById(id)
            .orElseThrow(() -> new RuntimeException("Enterprise not found"));
    }
}
