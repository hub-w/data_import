package cn.ac.iie.feign;

import cn.ac.iie.domain.NewsComment;
import cn.ac.iie.dto.requests.Request;
import cn.ac.iie.dto.response.Response;
import feign.Headers;
import feign.RequestLine;

/**
 * @ClassName InsertService
 * @Author tjh
 * @Date 19/4/15 下午2:51
 * @Version 1.0
 **/
public interface InsertService {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /dsp/Comment/addTComment")
    Response insertNewsComment(Request<NewsComment> request);
}
