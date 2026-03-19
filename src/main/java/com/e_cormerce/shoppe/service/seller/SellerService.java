package com.e_cormerce.shoppe.service.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.service.product.ProductService;
import com.e_cormerce.shoppe.service.seller.helper.ProductImagesUrl;
import com.e_cormerce.shoppe.service.seller.helper.UploadProductImagesHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {
    UploadProductImagesHelper uploadProductImagesHelper;
    ProductService productService;


    public void createProduct(
            CreateProductRequest request) {
        List<MultipartFile> variantImages = null;
        if (request.getHasVariant()) {
            variantImages = request.getVariantRequests().stream().map(variant->variant.getThumbnail()).toList();
        }

        ProductImagesUrl urls =
                uploadProductImagesHelper.uploadImagesOfProduct(request.getThumbnail(), request.getExtraImages(), variantImages);

        productService.persistProduct(request, urls);
    }
}
