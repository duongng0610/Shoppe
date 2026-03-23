package com.e_cormerce.shoppe.repository;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.entity.search.KeyWordCategory;
import com.e_cormerce.shoppe.repository.search.KeyWordCategoryRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyWordCategoryRepositoryTest {

    @Autowired
    KeyWordCategoryRepository keyWordCategoryRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void getRelatedCategoryNames_WhenValidKeyword_ReturnsCorrectProjections() {
        Category cat1 = createCategory("Electronics");
        Category cat2 = createCategory("Laptops");
        Category cat3 = createCategory("Home Appliances");

        createKeyWordCategory(cat1.getId(), "macbook air", 0.9F);
        createKeyWordCategory(cat2.getId(), "macbook pro", 0.8F);
        createKeyWordCategory(cat3.getId(), "fridge", 0.7F);

        entityManager.flush();
        entityManager.clear();

        List<CategoryProjection> result =
                keyWordCategoryRepository.getRelatedCategoryNames("macbook", 5);

        assertThat(result).hasSize(2);

        assertThat(result.get(0).getName()).isEqualTo("Electronics");
        assertThat(result.get(1).getName()).isEqualTo("Laptops");
    }

    @Test
    public void getRelatedCategoryNames_WhenNoMatch_ReturnsEmpty() {
        List<CategoryProjection> result =
                keyWordCategoryRepository.getRelatedCategoryNames("xyzxyz", 5);

        assertThat(result).isEmpty();
    }

    @Test
    public void getRelatedCategoryNames_WhenLimitIsOne_ReturnsOnlyOneResult() {
        // GIVEN
        Category tech = createCategory("Tech");
        createKeyWordCategory(tech.getId(), "iphone", 0.9F);
        createKeyWordCategory(tech.getId(), "ipad", 0.8F);

        entityManager.flush();
        entityManager.clear();

        List<CategoryProjection> result =
                keyWordCategoryRepository.getRelatedCategoryNames("i", 1);

        assertThat(result).hasSize(1);
    }


    private Category createCategory(String name) {
        Category cat = new Category();
        cat.setVal(name);
        cat.setDeleted(false);
        return entityManager.persistFlushFind(cat);
    }

    private void createKeyWordCategory(String catId, String keyword, float weight) {
        KeyWordCategory kc = new KeyWordCategory();
        kc.setCategory_id(catId);
        kc.setKeyword(keyword);
        kc.setWeight(weight);
        entityManager.persist(kc);
    }
}