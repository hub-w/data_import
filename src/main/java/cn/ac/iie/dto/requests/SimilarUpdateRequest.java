package cn.ac.iie.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName SimilarUpdateRequest
 * @Author tjh
 * @Date 19/4/17 上午10:04
 * @Version 1.0
 **/
@Data
public class SimilarUpdateRequest {
    @JsonProperty("data")
    private List<SimilarUpdateData> datas;

    public void addData(SimilarUpdateData data) {
        if(datas == null) {
            datas = new LinkedList<>();
        }

        datas.add(data);
    }
}
