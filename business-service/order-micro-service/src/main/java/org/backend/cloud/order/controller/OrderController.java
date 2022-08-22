package org.backend.cloud.order.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  private RestTemplate restTemplate;

  @GetMapping("/{orderId}")
  public Map<String, Object> getUser(@PathVariable String orderId) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("orderId", orderId);
    response.put("itemName", "Iphone 14");
    response.put("itemPrice", "$1500");

//    String url = "http://localhost:8099/users/1";
    String url = "http://user-service/users/1";
    Map user = restTemplate.getForEntity(url, Map.class).getBody();
    response.put("buyer", user);
    response.put("url", url);
    return response;
  }

}
