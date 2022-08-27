package org.backend.cloud.model.order.dto;

import lombok.Data;

@Data
public class SimpleOrder {

  private Long orderId;
  private String itemName;
  private String buyerName;
  private Long buyerUserId;

}
