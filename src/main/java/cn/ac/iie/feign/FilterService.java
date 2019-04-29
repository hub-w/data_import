package cn.ac.iie.feign;

import cn.ac.iie.dto.requests.FilterRequest;
import cn.ac.iie.dto.response.FilterResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * @ClassName FilterService
 * @Author tjh
 * @Date 19/2/27 下午5:42
 * @Version 1.0
 **/
public interface FilterService {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /filter")
    FilterResponse filter(FilterRequest param);
}
