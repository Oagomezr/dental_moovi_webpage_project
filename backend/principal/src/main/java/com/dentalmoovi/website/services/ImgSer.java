package com.dentalmoovi.website.services;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dentalmoovi.website.Utils;
import com.dentalmoovi.website.models.dtos.ImagesDTO;
import com.dentalmoovi.website.models.dtos.MessageDTO;
import com.dentalmoovi.website.models.entities.ActivityLogs;
import com.dentalmoovi.website.models.entities.Images;
import com.dentalmoovi.website.models.entities.Users;
import com.dentalmoovi.website.models.exceptions.ImageLoadingException;
import com.dentalmoovi.website.models.responses.ImgResponse;
import com.dentalmoovi.website.repositories.ActivityLogsRep;
import com.dentalmoovi.website.repositories.ImgRep;

@Service
public class ImgSer {
    private ImgRep imgRep;
    private UserSer userSer;
    private ActivityLogsRep activityLogsRep;

    public ImgResponse getCarouselImgs(){
        List<Images> imgs = imgRep.findCarouselImgs();
        List<ImagesDTO> imgDTO = new ArrayList<>();

        imgs.stream().forEach(img->{
            String base64Image = Base64.getEncoder().encodeToString(img.data());
            imgDTO.add(new ImagesDTO(img.id(), img.name(), img.contentType(), base64Image));
        });

        return new ImgResponse(imgDTO);
    }

    @CacheEvict(
        cacheNames = {"getProducsByContaining", "getProduct", "productsByCategory"}, 
        allEntries = true)
    public MessageDTO uploadImage(MultipartFile file) throws IOException{
        class UploadImage{
            MessageDTO uploadImage() throws IOException{
                
                // Read original image
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                
                //rescale the image
                byte[] resizedImageData = convert(originalImage);

                Users user = userSer.getUserAuthenticated();

                ActivityLogs log = new ActivityLogs(null, "El usuario agrego una nueva foto al carrucel", Utils.getNow(), user.id());
                activityLogsRep.save(log);
                
                // Create and save the new image
                return createImage(file, resizedImageData);
            }

            private MessageDTO createImage(MultipartFile file, byte[] resizedImageData) throws IOException {
                
                String originalFileName = file.getOriginalFilename();
                String contentType = file.getContentType();

                if(originalFileName == null || contentType == null) throw new IOException("Empty file");
                
                contentType = contentType.replace("image/", "");
                originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
                byte[] imageData = resizedImageData != null ? resizedImageData : file.getBytes();

                imgRep.save(new Images(null, originalFileName, contentType, imageData, null));
                
                return new MessageDTO("Image created");
            }

            private byte[] convert(BufferedImage originalImage) throws IOException {
            
                // Convert cropped image to bytes
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ImageIO.write(originalImage, "jpg", outputStream);
                byte[] resizedImageData = outputStream.toByteArray();
                outputStream.close();
            
                return resizedImageData;
            }
        }
        UploadImage innerClass = new UploadImage();
        return innerClass.uploadImage();
    }

    public MessageDTO deleteImg(Long idImage) throws ImageLoadingException{

        Users user = userSer.getUserAuthenticated();

        Images img = imgRep.findById(idImage)
            .orElseThrow(() -> new ImageLoadingException("Image not found"));

        ActivityLogs log = new ActivityLogs(null, "El usuario elimino una foto del carousel: "+img.name(), Utils.getNow(), user.id());
        activityLogsRep.save(log);

        imgRep.deleteById(idImage);

        return new MessageDTO("Image deleted");
    }


    public ImgSer(ImgRep imgRep, UserSer userSer, ActivityLogsRep activityLogsRep) {
        this.imgRep = imgRep;
        this.userSer = userSer;
        this.activityLogsRep = activityLogsRep;
    }
}
