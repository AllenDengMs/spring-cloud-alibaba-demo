package org.backend.cloud.model.order.entity;

import lombok.Data;

/**
 * 和数据表有一一对应的关系
 */
@Data
public class Order {

  private Long orderId;
  private String itemName;
  private Long BuyerUserId;

}
