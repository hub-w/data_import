package cn.ac.iie.feign;

import cn.ac.iie.domain.SearchHistory;
import cn.ac.iie.dto.requests.Request;
import cn.ac.iie.dto.response.Response;
import cn.ac.iie.utils.CommonConstant;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * @ClassName TestService
 * @Author tjh
 * @Date 19/2/27 下午5:08
 * @Version 1.0
 **/
public interface TestService {

    @RequestLine("POST /history/add")
    @Headers("Content-Type: application/json")
    Response test3(Request<SearchHistory> request);

    @RequestLine("GET /history/search?pageNum={pageNum}&pageSize={pageSize}")
    Response test4(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize);
}
