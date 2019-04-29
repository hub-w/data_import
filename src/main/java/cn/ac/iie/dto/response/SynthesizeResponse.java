package cn.ac.iie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName SynthesizeResponse
 * @Author tjh
 * @Date 19/4/15 下午2:58
 * @Version 1.0
 **/
@Data
public class SynthesizeResponse {
    @JsonProperty(value = "oneday_gkTimes")
    private String onedayGkTimes;
    @JsonProperty("threeday_gkTimes")
    private String threedayGkTimes;
    @JsonProperty("sevenday_gkTimes")
    private String sevendayGkTimes;
    @JsonProperty("similar_num")
    private String similarNum;
    @JsonProperty("gk_num")
    private String gkNum;
    @JsonProperty("gkTimes")
    private String gkTimes;
    @JsonProperty("suggest")
    private String suggest;
    @JsonProperty("category")
    private Integer category;
    @JsonProperty("path_length")
    private String pathLength;
    @JsonProperty("text_harm_score")
    private String textHarmScore;
    @JsonProperty("all_gkTimes")
    private String allGkTimes;
}
