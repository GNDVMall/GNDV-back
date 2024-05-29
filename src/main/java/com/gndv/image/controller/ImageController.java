package com.gndv.image.controller;

import com.gndv.common.CustomResponse;
import com.gndv.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @GetMapping("{image_type}/{image_id}")
    public CustomResponse<Object> getImage(@PathVariable String image_type, @PathVariable Long image_id){
        log.info("Get Image at S3");
        Object imageStream = imageService.getImage(image_type, image_id);

        return CustomResponse.ok("Get Image URL", imageStream);
    }
}
