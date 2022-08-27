package org.backend.cloud.openfegin.util;

import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import java.io.IOException;
import java.lang.reflect.Type;
import org.backend.cloud.common.web.model.Result;
import org.backend.cloud.common.web.utils.JSON;

public class FeignResultDecoder implements Decoder {

  @Override
  public Object decode(Response response, Type type)
      throws IOException, DecodeException, FeignException {
    if (response.body() == null) {
      return null;
    }
    String bodyStr = Util.toString(response.body().asReader(Util.UTF_8));
    //对结果进行转换
    Result result = JSON.jsonToObject(bodyStr, Result.class);
    return JSON.jsonToObject(JSON.toJson(result.getData()), type);
  }
}