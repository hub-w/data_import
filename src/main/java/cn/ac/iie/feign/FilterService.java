package cn.ac.iie.feign;

import cn.ac.iie.dto.requests.FilterRequest;
import cn.ac.iie.dto.response.FilterResponse;
import cn.ac.iie.dto.response.FilterResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import feign.Headers;
import feign.RequestLine;

import java.util.List;

/**
 * @ClassName FilterService
 * @Author tjh
 * @Date 19/2/27 下午5:42
 * @Version 1.0
 **/
public interface FilterService {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /filter")
    FilterResult filter(JSONArray param);
}
