package com.e_cormerce.shoppe.service.search;

import com.e_cormerce.shoppe.cache.TernaryTrie;
import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.dto.common.search.CategorySuggestionDTO;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.dto.response.search.SearchResponse;
import com.e_cormerce.shoppe.dto.response.search.SearchResultResponse;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.enums.search.MatchType;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.repository.product.CategoryRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.search.KeyWordCategoryRepository;
import com.e_cormerce.shoppe.util.cache.TrieSnapShotUtil;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {
    TrieSnapShotUtil trieSnapShotUtil;
    CategoryRepository categoryRepository;
    KeyWordCategoryRepository keyWordCategoryRepository;
    ProductMapper productMapper;

    @NonFinal
    TernaryTrie ternaryTrie;
    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        ternaryTrie = new TernaryTrie();
        TernaryTrie loaded = trieSnapShotUtil.load();

        if (loaded != null) {
            ternaryTrie = loaded;
        } else {
            List<CategoryProjection> words = categoryRepository.findAllCategoryNames();

            for (CategoryProjection projection : words) {
                String name = projection.getName();
                ternaryTrie.add(normalize(name), projection.getCategoryId());
            }

            trieSnapShotUtil.save(ternaryTrie);
        }
    }

    @Scheduled(fixedDelayString = "${app.snapshot.delay}")
    public void snapshot() {
        trieSnapShotUtil.save(ternaryTrie);
    }

    public SearchResponse search(String keyword, int num) {
        int max_num = num;

        List<CategorySuggestionDTO> result = new ArrayList<>();
        Set<String> existingNames = new HashSet<>();

        Iterable<CategoryProjection> prefix = ternaryTrie.searchWithPrefix(keyword);

        if (prefix != null) {
            for (CategoryProjection projection : prefix) {

                String name = projection.getName().toLowerCase();

                CategorySuggestionDTO suggestion = CategorySuggestionDTO.builder()
                        .categoryName(name)
                        .categoryId(projection.getCategoryId())
                        .matchType(MatchType.PREFIX)
                        .build();

                result.add(suggestion);
                existingNames.add(name);
            }
        }

        int remain_num = max_num - result.size();

        if (remain_num > 0) {
            List<CategoryProjection> related = keyWordCategoryRepository.getRelatedCategoryNames(keyword, remain_num);

            for (CategoryProjection projection : related) {
                String name = projection.getName().toLowerCase();

                if (!existingNames.contains(name)) {
                    CategorySuggestionDTO suggestion = CategorySuggestionDTO.builder()
                            .categoryName(name)
                            .matchType(MatchType.RELATED)
                            .categoryId(projection.getCategoryId())
                            .build();

                    result.add(suggestion);
                    existingNames.add(name);

                    if (result.size() == max_num) break;
                }
            }
        }

        return SearchResponse.builder()
                .suggestions(result)
                .build();
    }

    public SearchResultResponse searchProductInCategory(String category_id) {
        List<Product> products = productRepository.findProductsInCategory(category_id);

        List<ProductCardResponse> responses = products.stream().map(product -> {
            ProductCardResponse response = productMapper.toProductDTO(product);
            return response;
        }).toList();

        return SearchResultResponse.builder()
                .responses(responses)
                .build();
    }


    private String normalize(String word) {
        return word.toLowerCase()
                .trim()
                .replace("//s+", " ");
    }

}
