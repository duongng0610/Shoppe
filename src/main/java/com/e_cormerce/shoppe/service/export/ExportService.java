package com.e_cormerce.shoppe.service.export;

import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.jpa.QueryHints;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExportService {
  @PersistenceContext EntityManager em;

  @Transactional(readOnly = true)
  public void exportOrdersCsv(HttpServletResponse response, String sellerId) {

    // Setup response:
    response.setContentType("text/csv");
    response.setHeader("Content-Disposition", "attachment; filename=orders.csv");

    String jpql =
        "Select o from Order o Left join  Fetch o.variant left join fetch o.client"
            + (sellerId != null ? " where o.seller.id = :sellerId" : "");

    TypedQuery<Order> query =
        em.createQuery(jpql, Order.class).setHint(QueryHints.HINT_FETCH_SIZE, 1000);

    if (sellerId != null) {
      query.setParameter("sellerId", sellerId);
    }

    try (var printWriter = response.getWriter();
        Stream<Order> stream = query.getResultStream()) {

      printWriter.println(
          """
                    id,quantity,ship_cost,price_each,total_price,
                    status,payment_status,variant_id,client_id,created_at
                    """
              .replace("\n", ""));

      final int[] count = {0};

      stream.forEach(
          o -> {
            printWriter.println(
                o.getId()
                    + ", "
                    + o.getQuantity()
                    + ", "
                    + o.getShipCost()
                    + ", "
                    + o.getPriceEach()
                    + ", "
                    + o.getTotalPrice()
                    + ", "
                    + escapeCsv(o.getStatus().name())
                    + ", "
                    + escapeCsv(o.getPaymentStatus().name())
                    + ", "
                    + escapeCsv(o.getVariant().getId())
                    + ", "
                    + escapeCsv(o.getClient().getId())
                    + ", "
                    + o.getCreatedAt());
            em.detach(o); // tranh memory leak

            count[0]++;
            if (count[0] % 1000 == 0) {
              printWriter.flush();
              count[0] = 0;
            }
          });

      printWriter.flush();
    } catch (IOException e) {
      throw new AppException(ErrorCode.ERROR_EXPORT_CSV);
    }
  }

  // Escape ki tu dac biet
  private String escapeCsv(String value) {
    if (value == null) {
      return "";
    }
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
      return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    return value;
  }
}
