package cn.ac.iie.feign;

import cn.ac.iie.dto.requests.SynthesizeRequest;
import cn.ac.iie.dto.response.SynthesizeResponse;
import com.alibaba.fastjson.JSONObject;
import feign.Headers;
import feign.RequestLine;

/**
 * @ClassName SynthesizeService
 * @Author tjh
 * @Date 19/2/27 下午5:20
 * @Version 1.0
 **/
public interface SynthesizeService {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /synthesizeEngine")
    JSONObject synthesize(JSONObject param);
}
