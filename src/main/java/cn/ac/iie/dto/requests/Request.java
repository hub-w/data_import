package cn.ac.iie.dto.requests;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wlt
 * @date2019-4-10 10:04
 */
@Data
public class Request<T> {
    private Integer dataNum;
    private List<T> data;

    public void addData(T t) {
        if(data == null) {
            data = new LinkedList<>();
        }
        data.add(t);
    }
}
