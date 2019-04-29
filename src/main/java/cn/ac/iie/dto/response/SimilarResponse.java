package cn.ac.iie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName SimilarResponse
 * @Author tjh
 * @Date 19/4/16 下午4:49
 * @Version 1.0
 **/
@Data
public class SimilarResponse {
    private Integer num;
    @JsonProperty("id_list")
    private List<String> idList;
    private String id;
}
