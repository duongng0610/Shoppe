package com.e_cormerce.shoppe.service;

import com.e_cormerce.shoppe.cache.TernaryTrie;
import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.dto.response.search.SearchResponse;
import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.enums.search.MatchType;
import com.e_cormerce.shoppe.repository.product.CategoryRepository;
import com.e_cormerce.shoppe.repository.search.KeyWordCategoryRepository;
import com.e_cormerce.shoppe.service.search.SearchService;
import com.e_cormerce.shoppe.util.cache.TrieSnapShotUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class SearchServiceTest {
    @InjectMocks
    SearchService searchService;

    @Mock
    TrieSnapShotUtil trieSnapShotUtil;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    KeyWordCategoryRepository keyWordCategoryRepository;

    @BeforeEach
    public void setup() {
        when(trieSnapShotUtil.load()).thenReturn(null);

        CategoryProjection p1 = mock(CategoryProjection.class);
        when(p1.getName()).thenReturn("Laptop Dell");
        when(p1.getCategoryId()).thenReturn("L1");

        when(categoryRepository.findAllCategoryNames()).thenReturn(List.of(p1));

        searchService.init();
    }

    @Test
    public void search_ReturnCombinedResults() {
        String keyword = "lap";
        int num = 10;

        CategoryProjection p2 = mock(CategoryProjection.class);
        when(p2.getName()).thenReturn("Macbook Pro");
        when(p2.getCategoryId()).thenReturn("L2");

        when(keyWordCategoryRepository.getRelatedCategoryNames(eq("lap"), anyInt()))
                .thenReturn(List.of(p2));

        SearchResponse response = searchService.search(keyword, num);

        assertThat(response.getSuggestions()).hasSize(2);

        assertThat(response.getSuggestions().get(0).getMatchType()).isEqualTo(MatchType.PREFIX);
        assertThat(response.getSuggestions().get(0).getCategoryName()).isEqualTo("laptop dell");
        assertThat(response.getSuggestions().get(0).getCategoryId()).isEqualTo("L1");

        assertThat(response.getSuggestions().get(1).getMatchType()).isEqualTo(MatchType.RELATED);
        assertThat(response.getSuggestions().get(1).getCategoryName()).isEqualTo("macbook pro");
        assertThat(response.getSuggestions().get(1).getCategoryId()).isEqualTo("L2");

    }

    @Test
    void search_RemoveDuplicates() {
        String keyword = "laptop";

        CategoryProjection duplicate = mock(CategoryProjection.class);
        when(duplicate.getName()).thenReturn("Laptop Dell");

        when(keyWordCategoryRepository.getRelatedCategoryNames(anyString(), anyInt()))
                .thenReturn(List.of(duplicate));

        SearchResponse response = searchService.search(keyword, 5);

        assertThat(response.getSuggestions()).hasSize(1);
    }

}
