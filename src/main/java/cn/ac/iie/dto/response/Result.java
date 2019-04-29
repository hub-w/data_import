package cn.ac.iie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName Result
 * @Author tjh
 * @Date 19/4/16 下午4:24
 * @Version 1.0
 **/
@Data
public class Result {
    @JsonProperty("hit_black")
    private String hitBlack;
    @JsonProperty("m_abstract")
    private String hitAbstract;
    @JsonProperty("rule_id")
    private List<String> ruleIds;
    @JsonProperty("subject_id")
    private List<String> subjectIds;
}
