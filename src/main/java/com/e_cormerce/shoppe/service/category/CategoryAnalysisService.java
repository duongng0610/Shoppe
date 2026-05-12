package com.e_cormerce.shoppe.service.category;

import com.e_cormerce.shoppe.projection.category.CategoryDailyProjection;
import com.e_cormerce.shoppe.projection.category.CategoryGrowthProjection;
import com.e_cormerce.shoppe.projection.category.CategoryStatisticsProjection;
import com.e_cormerce.shoppe.projection.category.TopSearchedCategoryProjection;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryAnalysisService {
    CategoryRepository categoryRepository;

    public List<TopSearchedCategoryProjection> getTopSearchedCategories(int days, int limit) {
        return categoryRepository.findTopSearchedCategories(days, limit);
    }

    public List<CategoryStatisticsProjection> getStatisticCategories(int limit, int offset) {
        return categoryRepository.findStatisticCategories(limit, offset);
    }

    public List<CategoryDailyProjection> getCategoryDailyRecent(String categoryId, int days) {
        return categoryRepository.getCategoryDailyRecent(categoryId, days);
    }

    public List<CategoryGrowthProjection> getCategoryGrowthAnalysis(
            String categoryId, int days
    ) {
        return categoryRepository.getCategoryGrowthAnalysis(categoryId, days);
    }
}
