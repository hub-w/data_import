package cn.ac.iie.domain;

import lombok.Data;

import java.sql.Timestamp;


@Data
public class SearchHistory {

    /**
     * id
     */
    private Integer id;
    /**
     * 搜索关键词
     */
    private String searchKeyword;

    /**
     * 关键词搜索历史次数
     */
    private Integer searchKeywordCount = 0;

    /**
     * 搜索具体参数
     */
    private String searchParams;

    /**
     * 触发搜索时间
     */
    private Timestamp searchTime;

    /**
     * 操作人id
     */
    private String searchUserId;

    /**
     * 操作人名称
     */
    private String searchUserName;

    /**
     * 搜索记录更新时间
     */
    private Timestamp updateTime;

    /**
     * 删除标记,0：未删除;1:删除
     */
    private Integer isDelete = 0;

    /**
     * 是否推送监测主题，0：未推送；1：已经推送
     */
    private Integer isPush = 0;

}
