package org.backend.cloud.order.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import org.backend.cloud.order.client.UserServiceClient;
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
  public Map<String, Object> getUser(@PathVariable String orderId) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("orderId", orderId);
    response.put("itemName", "Iphone 14");
    response.put("itemPrice", "$1500");

    Map<String, Object> user = userServiceClient.getUser("1");
    response.put("buyer", user);
    return response;
  }

}
