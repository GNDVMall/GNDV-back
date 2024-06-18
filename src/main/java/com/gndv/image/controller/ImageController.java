package com.gndv.image.controller;

import com.gndv.common.CustomResponse;
import com.gndv.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/images")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Image API", description = "이미지 관련 API")
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "이미지 조회", description = "S3에서 이미지를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 조회 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("{image_type}/{image_id}")
    public CustomResponse<Object> getImage(
            @Parameter(description = "이미지 타입") @PathVariable String image_type,
            @Parameter(description = "이미지 ID") @PathVariable Long image_id) {
        log.info("Get Image at S3");
        Object imageStream = imageService.getImage(image_type, image_id);
        return CustomResponse.ok("Get Image URL", imageStream);
    }

    @Operation(summary = "이미지 업로드", description = "S3에 이미지를 업로드합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 업로드 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/upload/{image_type}")
    public CustomResponse<String> uploadImage(
            @Parameter(description = "이미지 타입") @PathVariable String image_type,
            @Parameter(description = "업로드할 파일", required = true) @RequestParam MultipartFile file) throws IOException {
        log.info("Upload a image file");
        String fileUrl = imageService.uploadCloud(image_type, file);
        return CustomResponse.ok("Upload a file", fileUrl);
    }
}
