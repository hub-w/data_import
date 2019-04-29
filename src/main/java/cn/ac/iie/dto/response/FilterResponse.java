package cn.ac.iie.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName FilterResponse
 * @Author tjh
 * @Date 19/4/15 下午2:55
 * @Version 1.0
 **/
//@Data
public class FilterResponse {
    private boolean status;
    private String msg;
    @JsonProperty("result")
    private FilterRes filterRes;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FilterRes getFilterRes() {
        return filterRes;
    }

    public void setFilterRes(FilterRes filterRes) {
        this.filterRes = filterRes;
    }
}
