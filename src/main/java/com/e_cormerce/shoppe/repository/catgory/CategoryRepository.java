package com.e_cormerce.shoppe.repository.catgory;

import com.e_cormerce.shoppe.dto.response.category.CategoryDetailResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.projection.category.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
  Optional<Category> findById(String id);

  Category findByVal(String name);

  boolean existsByVal(String val);

  @Query(value = "SELECT * FROM categories WHERE path_to_parent IS NULL", nativeQuery = true)
  List<Category> findDefault();

  @Query(value = "call ecommerce.get_direct_children_of_category(:id)", nativeQuery = true)
  List<CategoryProjection> findChildren(@Param("id") String id);

  @Query(
      value =
          """
                            SELECT c1.id,
                                   c1.val,
                                   c1.thumbnail,
                                   c1.deleted,
                                   c1.created_at as createdAt,
                                   c2.val AS parentVal
                            FROM categories c1
                            LEFT JOIN categories c2
                              ON c1.path_to_parent LIKE '%/%'
                             AND c2.id = SUBSTRING_INDEX(c1.path_to_parent, '/', -1)
                            WHERE c1.id = :id
                            """,
      nativeQuery = true)
  Optional<CategoryDetailResponse> findCategoryDetailsById(@Param("id") String id);

  @Query(value = """
            call get_top_searched_categories(:days,:x)""", nativeQuery = true)
  List<TopSearchedCategoryProjection> findTopSearchedCategories(int days, int x);

  @Query(
      value =
          """
                            SELECT * FROM category_statistics_view  LIMIT :limit OFFSET :offset""",
      nativeQuery = true)
  List<CategoryStatisticsProjection> findStatisticCategories(int limit, int offset);

  @Query(
      value =
          """
                            CALL get_category_daily_recent(
                                :categoryId,
                                :days
                            )
                            """,
      nativeQuery = true)
  List<CategoryDailyProjection> getCategoryDailyRecent(
      @Param("categoryId") String categoryId, @Param("days") int days);

  @Query(
      value =
          """
                    call analyze_category_growth_full(:categoryId,:days)
                    """,
      nativeQuery = true)
  List<CategoryGrowthProjection> getCategoryGrowthAnalysis(
      @Param("categoryId") String categoryId, @Param("days") int days);

  @Query(
      value =
          """
                            call count_products_by_status_of_category(:categoryId,null)
                            """,
      nativeQuery = true)
  int countProducts(@Param("categoryId") String categoryId);

  @Query(
      value =
          """
                     select count(*) from categories where val =:val
                    """,
      nativeQuery = true)
  int countCategoryByVal(@Param("val") String val);

  @Query(
      value =
          """
                             select count(*) from categories where val =:val and deleted = 1
                            """,
      nativeQuery = true)
  int countDeletedCategoryByVal(@Param("val") String val);

  @Query(
      value =
          """
                             select * from categories where id =:id and deleted = 1
                            """,
      nativeQuery = true)
  Optional<Category> findAllById(@Param("id") String id);
}
