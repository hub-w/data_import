package cn.ac.iie.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName FilterRequest
 * @Author tjh
 * @Date 19/4/15 下午2:55
 * @Version 1.0
 **/
@Data
public class FilterRequest {
    @JsonProperty("id")
    private String id;
    @JsonProperty("text")
    private String text;
    @JsonProperty("sys_code")
    private String sysCode;
}
