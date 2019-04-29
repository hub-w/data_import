package cn.ac.iie.feign;

import cn.ac.iie.dto.requests.SimilarRequest;
import cn.ac.iie.dto.requests.SimilarUpdateRequest;
import cn.ac.iie.dto.response.SimilarResponse;
import cn.ac.iie.dto.response.SimilarUpdateResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * @ClassName SimilarService
 * @Author tjh
 * @Date 19/4/16 下午3:41
 * @Version 1.0
 **/
public interface SimilarService {
    @RequestLine("POST /similar/filter")
    @Headers("Content-Type: application/json")
    SimilarResponse similar(SimilarRequest similarRequest);

    @RequestLine("POST /similar/update")
    @Headers("Content-Type: application/json")
    SimilarUpdateResponse update(SimilarUpdateRequest similarUpdateRequest);
}
