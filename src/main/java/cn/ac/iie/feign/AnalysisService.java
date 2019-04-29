package cn.ac.iie.feign;

import cn.ac.iie.dto.requests.AnalysisRequest;
import cn.ac.iie.dto.response.AnalysisResponse;
import feign.Headers;
import feign.RequestLine;

/**
 * @ClassName AnalysisService
 * @Author tjh
 * @Date 19/4/16 下午3:41
 * @Version 1.0
 **/
public interface AnalysisService {
    @RequestLine("POST /sentiment/analysis/sentence")
    @Headers("Content-Type: application/json")
    AnalysisResponse analysis(AnalysisRequest analysisRequest);
}
