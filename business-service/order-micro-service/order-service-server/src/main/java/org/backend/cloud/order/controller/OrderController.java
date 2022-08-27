package org.backend.cloud.order.controller;

import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.model.order.dto.SimpleOrder;
import org.backend.cloud.model.user.dto.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private UserServiceClient userServiceClient;

  @GetMapping("/{orderId}")
  public SimpleOrder getUser(@PathVariable Long orderId) {
    SimpleOrder order = new SimpleOrder();
    order.setOrderId(orderId);
    order.setItemName("Iphone 14");

    SimpleUser user = userServiceClient.getUser("1");
    order.setBuyerUserId(user.getUserId());
    order.setBuyerName(user.getNickname());
    return order;
  }

}
