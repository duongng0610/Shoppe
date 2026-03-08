package com.e_cormerce.shoppe.service.seller.helper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadUrls {

    String thumbnailUrl;
    List<String> extraImageUrls;
    List<String> variantImageUrls;

    public UploadUrls(String thumbnailUrl,
                        List<String> extraImageUrls,
                        List<String> variantImageUrls) {

        if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
            throw new IllegalArgumentException("thumbnailUrl cannot be null or blank");
        }

        this.thumbnailUrl = thumbnailUrl;

        this.extraImageUrls = extraImageUrls != null
                ? List.copyOf(extraImageUrls)
                : List.of();

        this.variantImageUrls = variantImageUrls != null
                ? List.copyOf(variantImageUrls)
                : List.of();
    }

    public static UploadUrls thumbnailOnly(String thumbnailUrl) {
        return new UploadUrls(thumbnailUrl, List.of(), List.of());
    }
}