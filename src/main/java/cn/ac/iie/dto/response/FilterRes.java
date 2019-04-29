package cn.ac.iie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName FilterRes
 * @Author tjh
 * @Date 19/4/16 下午4:24
 * @Version 1.0
 **/
@Data
public class FilterRes {
    @JsonProperty("isPush")
    private Boolean isPush;
    private List<Result> descs;
}
