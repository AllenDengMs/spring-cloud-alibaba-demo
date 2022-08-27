package org.backend.cloud.common.web.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;

public class JSON {

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Java对象序列化成JSON字符串
   * @param obj
   * @return
   */
  public static String toJson(Object obj){
    String json = null;
    try {
      json = mapper.writeValueAsString(obj);
    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return json;
  }

  public static <T> T jsonToObject(String jsonStr, Type targetType) {
    try {
      JavaType javaType = TypeFactory.defaultInstance().constructType(targetType);
      return mapper.readValue(jsonStr, javaType);
    } catch (IOException e) {
      throw new IllegalArgumentException("将JSON转换为对象时发生错误:" + jsonStr, e);
    }
  }
}
