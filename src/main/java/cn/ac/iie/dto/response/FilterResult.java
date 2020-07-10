package cn.ac.iie.dto.response;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author wdc
 * @Date 2020-7-5 22:05
 * @Version 1.0
 */
@Data
public class FilterResult {
    private List<FilterResponse> result;
}
