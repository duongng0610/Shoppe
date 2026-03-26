package com.e_cormerce.shoppe.repository.synonyms;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SynonymsRepositoryTest {

  //    @Autowired
  //    SynonymsRepository synonymsRepository;
  //
  //    @Autowired
  //    TestEntityManager entityManager;
  //
  //    @Test
  //    public void getCategorySynonyms_WhenValidKeyword_ReturnsCorrectProjections() {
  //        Category cat1 = createCategory("Electronics");
  //        Category cat2 = createCategory("Laptops");
  //        Category cat3 = createCategory("Home Appliances");
  //
  //        createKeyWordCategory(cat1.getId(), "macbook air", 0.9F);
  //        createKeyWordCategory(cat2.getId(), "macbook pro", 0.8F);
  //        createKeyWordCategory(cat3.getId(), "fridge", 0.7F);
  //
  //        entityManager.flush();
  //        entityManager.clear();
  //
  //        List<CategoryDto> result =
  //                synonymsRepository.getCategorySynonyms("macbook", 5);
  //
  //        assertThat(result).hasSize(2);
  //
  //        assertThat(result.get(0).getVal()).isEqualTo("Electronics");
  //        assertThat(result.get(1).getVal()).isEqualTo("Laptops");
  //    }
  //
  //    @Test
  //    public void getCategorySynonyms_WhenNoMatch_ReturnsEmpty() {
  //        List<CategoryDto> result =
  //                synonymsRepository.getCategorySynonyms("xyzxyz", 5);
  //
  //        assertThat(result).isEmpty();
  //    }
  //
  //    @Test
  //    public void getCategorySynonyms_WhenLimitIsOne_ReturnsOnlyOneResult() {
  //        // GIVEN
  //        Category tech = createCategory("Tech");
  //        createKeyWordCategory(tech.getId(), "iphone", 0.9F);
  //        createKeyWordCategory(tech.getId(), "ipad", 0.8F);
  //
  //        entityManager.flush();
  //        entityManager.clear();
  //
  //        List<CategoryDto> result = synonymsRepository.getCategorySynonyms("i", 1);
  //
  //        assertThat(result).hasSize(1);
  //    }
  //
  //    private Category createCategory(String name) {
  //        Category cat = new Category();
  //        cat.setVal(name);
  //        cat.setDeleted(false);
  //        return entityManager.persistFlushFind(cat);
  //    }
  //
  //    private void createKeyWordCategory(String catId, String keyword, float weight) {
  //        var kc = new CategorySynonyms();
  //        kc.setCategory_id(catId);
  //        kc.setKeyword(keyword);
  //        kc.setWeight(weight);
  //        entityManager.persist(kc);
  //    }
}
