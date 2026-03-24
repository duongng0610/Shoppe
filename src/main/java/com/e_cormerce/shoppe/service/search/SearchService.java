package com.e_cormerce.shoppe.service.search;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.repository.search.KeyWordCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {
    KeyWordCategoryRepository keyWordCategoryRepository;

    public List<CategoryProjection> search(String keyword, int num) {
        return keyWordCategoryRepository.getRelatedCategoryNames(keyword, num);
    }

}
