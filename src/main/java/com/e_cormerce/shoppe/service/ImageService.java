package com.e_cormerce.shoppe.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageService {
    CloudinaryService cloudinaryService;

    /**
     * đưa tất cả itemImage vào thread pool..
     *
     * @param files
     * @return
     */
    public CompletableFuture<List<String>> uploadImageList(List<MultipartFile> files) {
        List<CompletableFuture<String>> futures = files.stream().map(cloudinaryService::uploadFile).toList();
        return CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v ->
                        futures.stream()
                                .map(CompletableFuture::join)
                                .toList()
                );
    }

}
