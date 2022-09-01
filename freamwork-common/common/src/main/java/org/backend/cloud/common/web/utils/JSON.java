package org.backend.cloud.common.web.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;

public class JSON {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Object to json
   * @param obj
   * @return
   */
  public static String stringify(Object obj){
    try {
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new IllegalArgumentException("对象转化成json字符串出错", e);
    }
  }

  /**
   * json to Object
   * @param json
   * @param targetType
   * @return
   * @param <T>
   */
  public static <T> T parse(String json, Type targetType) {
    try {
      return mapper.readValue(json, TypeFactory.defaultInstance().constructType(targetType));
    } catch (IOException e) {
      throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + json, e);
    }
  }
}
