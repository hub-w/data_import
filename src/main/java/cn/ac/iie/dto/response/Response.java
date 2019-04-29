package cn.ac.iie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 接口公共返回类
 * @param:
 * @return:
 * @author: WangXiaoyu
 * @Date: 2018/5/31
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {
    private String state;
    private String message;
    private Object data;
    private Long sumCount;
    private Integer pageNum;

    public static Response operateSucessAndHaveDataAndPage(Object data,Long sumCount,Integer pageNum) {
        return new Response(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMsg(), data,sumCount,pageNum);
    }
    public static Response operateSucessAndNoDataAndPage(Object data,Long sumCount,Integer pageNum) {
        return new Response(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMsg(), data,sumCount,pageNum);
    }
    public static Response normResponse(String state, String message, Object data) {
        return new Response(state, message, data,null,null);
    }


    public static Response operateSucessAndHaveData(Object data) {
        return new Response(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMsg(), data,null,null);
    }


    public static Response operateSucessNoData() {
        return new Response(ReturnCode.NODATA.getCode(), ReturnCode.SUCCESS.getMsg(), null,null,null);
    }


    public static Response databaseError(String msg) {
        return new Response(ReturnCode.DATABASE_ERROE.getCode(), msg, null,null,null);
    }


    public static Response paramError(String msg) {
        return new Response(ReturnCode.PARAM_ERROR.getCode(), msg, null,null,null);
    }


}
