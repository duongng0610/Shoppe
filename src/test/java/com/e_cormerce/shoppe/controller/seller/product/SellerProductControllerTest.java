package com.e_cormerce.shoppe.controller.seller.product;

import com.e_cormerce.shoppe.dto.common.product.TypeDto;
import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.request.product.VariantRequest;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.service.seller.SellerService;
import com.e_cormerce.shoppe.util.CreateMockMultipartFile;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc//mặc định addFilters = true.
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    SellerService sellerService;

//   ===========FAIL============

    /**
     * thiếu thumbnail param.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    public void missingThumbnailCreateProductRequest() throws Exception {
        var request = DataTestCreateProductRequestHelper.validNoVariantRequest();
        mockMvc.perform(multipart("/seller/product")

                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.MISSING_REQUIRED_PARAMS.getMessage()));
    }


    /**
     * Name sai.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void nameIsBlank() throws Exception {
        var request = DataTestCreateProductRequestHelper.validNoVariantRequest();
        request.setName("");

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name is required"));
    }

    /**
     * Giá sai.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void originPriceInvalid() throws Exception {
        var request = DataTestCreateProductRequestHelper.validNoVariantRequest();
        request.setOriginPrice(BigDecimal.valueOf(-1));
        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("originPrice must be greater than 0"));
    }

    /**
     * Quantity sai.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void totalQuantityIsNull() throws Exception {
        var request = DataTestCreateProductRequestHelper.validNoVariantRequest();
        request.setTotalQuantity(null);

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("totalQuantity is required"));
    }

    /**
     * CategoryId sai.
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void categoryIdIsBlank() throws Exception {
        var request = DataTestCreateProductRequestHelper.validNoVariantRequest();
        request.setCategoryId(null);

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("categoryId is required"));
    }

    /**
     * Sai type.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void typeNameInvalid() throws Exception {
        var type = new TypeDto();
        type.setName(""); // ❌
        type.setValues(List.of("A"));

        var request = CreateProductRequest.builder()
                .name("abc")
                .categoryId("id")
                .hasVariant(false)
                .description("desc")
                .originPrice(BigDecimal.valueOf(10))
                .totalQuantity(10L)
                .types(List.of(type))
                .build();

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("type name is required"));
    }

    //    ======Xung đột variant

    /**
     * 1.sai field variant.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void hasVariant_true_missingVariantImages() throws Exception {

        var request = CreateProductRequest.builder()
                .name("abc")
                .description("desc")
                .categoryId("id")
                .hasVariant(true)
                .originPrice(BigDecimal.valueOf(10))
                .totalQuantity(10L)
                .types(List.of(new TypeDto("Color", List.of("Red"))))
                .variantRequests(List.of(new VariantRequest()))
                .build();

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(Matchers.anyOf(
                                Matchers.is("quantity of variant is required"),
                                Matchers.is("variantValues is required"),
                                Matchers.is("price of variant is required")
                        )));
    }

    /**
     * 2. hasVariant = true nhưng thiếu variantImages
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void missingVariantImages() throws Exception {

        var request = DataTestCreateProductRequestHelper.validVariantRequest(); // ✔ có type + variant

        mockMvc.perform(multipart("/seller/product")
                                .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                                .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                        // ❌ thiếu variantImages

                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.CONFLICT_VARIANT_DATA.getMessage()));
    }


    /**
     * 3. size variantImages != variantRequests
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void variantImageSizeNotMatch() throws Exception {

        var request = DataTestCreateProductRequestHelper.variantRequestWithSize(2); // 2 variants

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                        .file(CreateMockMultipartFile.createMockImageFile("variantImages")) // ❌ chỉ 1 ảnh
                        .file(CreateMockMultipartFile.createMockImageFile("extraImages"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.CONFLICT_VARIANT_DATA.getMessage()));
    }

    /**
     * 4.Có variant nhưng KHÔNG có type
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void hasVariantButMissingTypes() throws Exception {

        var request = DataTestCreateProductRequestHelper.validVariantRequest();
        request.setTypes(null); // ❌ vi phạm rule

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                        .file(CreateMockMultipartFile.createMockImageFile("variantImages"))
                        .file(CreateMockMultipartFile.createMockImageFile("extraImages"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.CONFLICT_VARIANT_DATA.getMessage()));
    }

    /**
     * 5. hasVariant = false nhưng vẫn gửi variantRequests
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void hasVariantFalseButHasVariantRequests() throws Exception {

        var request = DataTestCreateProductRequestHelper.validVariantRequest();
        request.setHasVariant(false); // ❌
        // vẫn còn variantRequests

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.CONFLICT_VARIANT_DATA.getMessage()));
    }

    /**
     * 6. hasVariant = false nhưng vẫn gửi variantImages
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")
    void hasVariantFalseButHasVariantImages() throws Exception {

        var request = DataTestCreateProductRequestHelper.validNoVariantRequest(); // ✔ không variant

        mockMvc.perform(multipart("/seller/product")
                        .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                        .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
                        .file(CreateMockMultipartFile.createMockImageFile("variantImages")) // ❌
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(ErrorCode.CONFLICT_VARIANT_DATA.getMessage()));
    }
//    =======SUCCESS===========


    /**
     * Dữ liệu đúng (ko có variants)
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "PERMISSION_CREATE_PRODUCT")//tương ứng với hasAuthority ở PreAuthority
    public void ValidCreateProductWithoutVariantRequest() throws Exception {
        var request = DataTestCreateProductRequestHelper.validNoVariantRequest();
        mockMvc.perform(multipart("/seller/product")
                .file(CreateMockMultipartFile.createMultipartJsonObject("request", request))
                .file(CreateMockMultipartFile.createMockImageFile("thumbnail"))
        ).andExpect(status().isCreated());
    }


}
