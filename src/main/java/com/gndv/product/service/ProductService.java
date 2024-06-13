package com.gndv.product.service;

import com.gndv.image.domain.dto.ImageRequest;
import com.gndv.image.mapper.ImageMapper;
import com.gndv.image.service.ImageService;
import com.gndv.product.domain.dto.request.ProductInsertRequest;
import com.gndv.product.domain.dto.request.ProductListPagingRequest;
import com.gndv.product.domain.dto.request.ProductUpdateRequest;
import com.gndv.product.domain.dto.response.ProductDetailResponse;
import com.gndv.product.domain.entity.ProductDetail;
import com.gndv.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    public ProductDetailResponse getProduct(Long product_id) throws Exception {
        Optional<ProductDetailResponse> findById = productMapper.findById(product_id);

        if (findById.isPresent()) {
            return findById.get();
        }
        throw new Exception();
    }

    public List<ProductDetail> getProducts(ProductListPagingRequest pagingRequest) {
        List<ProductDetail> findList = productMapper.findAllById(pagingRequest);
        return findList;
    }

    @Transactional
    @PreAuthorize("#request.email == authentication.name")
    public void insertProduct(ProductInsertRequest request) throws Exception {
        productMapper.insert(request);
        Long id = request.getProduct_id();

        for (MultipartFile file : request.getImages()) {
            // 클라우드에 업로드해서 url 가져오기
            String url = imageService.uploadCloud("product", file);

            // ImageRequest 객체 생성하기
            ImageRequest imageRequest = ImageRequest.builder()
                    .content_type(file.getContentType())
                    .use_id(id)
                    .original_name(file.getOriginalFilename())
                    .real_filename(url)
                    .size(file.getSize())
                    .image_type("product").build();

            // 생성된 객체를 DB에 저장하기
            int updated = productMapper.insertImages(imageRequest);
            if (updated != 1) throw new Exception("이미지 업로드 실패");
        }

    }

    @Transactional
    @PreAuthorize("#request.email == authentication.name")
    public int updateProduct(ProductUpdateRequest request) throws Exception {
        int updated = productMapper.update(request);

        // 이미지 업로드하기
        if (request.getImages() != null && request.getImages().size() != 0) {
            for (MultipartFile file : request.getImages()) {
                // 클라우드에 업로드해서 url 가져오기
                String url = imageService.uploadCloud("product", file);

                // ImageRequest 객체 생성하기
                ImageRequest imageRequest = ImageRequest.builder()
                        .content_type(file.getContentType())
                        .use_id(request.getProduct_id())
                        .original_name(file.getOriginalFilename())
                        .real_filename(url)
                        .size(file.getSize())
                        .image_type("product").build();

                // 생성된 객체를 DB에 저장하기
                int updatedImage = productMapper.insertImages(imageRequest);
                if (updatedImage != 1) throw new Exception("이미지 업로드 실패");
            }
        }

        // 사용 안하는 이미지 삭제하기
        if (request.getDelete_images() != null && request.getDelete_images().size() > 0) {
            List<String> deleteURLs = new ArrayList<>();
            for (String url : request.getDelete_images()) {
                // 클라우드에서 삭제
                imageService.deleteCloud(url);
                deleteURLs.add(url);
            }
            // DB에서 삭제
            imageMapper.deleteImages(deleteURLs);
        }

        return updated;
    }

    @Transactional
//    @PreAuthorize("isAuthenticated()")
    public int deleteProduct(Long product_id, String email) {
        int update = productMapper.delete(product_id, email);
        return update;
    }
}