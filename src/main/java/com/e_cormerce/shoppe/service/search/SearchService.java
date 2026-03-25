package com.e_cormerce.shoppe.service.search;

import com.e_cormerce.shoppe.dto.common.search.CategoryDto;
import com.e_cormerce.shoppe.repository.catgory.SynonymsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {
    SynonymsRepository synonymsRepository;

    public List<CategoryDto> search(String keyword, int num) {
        return synonymsRepository.getCategorySynonyms(keyword, num);
    }

}
