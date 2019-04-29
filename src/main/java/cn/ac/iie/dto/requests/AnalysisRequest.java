package cn.ac.iie.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName AnalysisRequest
 * @Author tjh
 * @Date 19/4/16 下午4:49
 * @Version 1.0
 **/
@Data
public class AnalysisRequest {
    @JsonProperty("data")
    private String data;
}
