package cn.ac.iie.dto.response;

import lombok.Data;

/**
 * @ClassName AnalysisResponse
 * @Author tjh
 * @Date 19/4/16 下午4:49
 * @Version 1.0
 **/
@Data
public class AnalysisResponse {
    private Integer message;
    private Float prob;
    private Integer sentiment;
}
