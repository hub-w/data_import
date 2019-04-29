package cn.ac.iie.dto.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName SynthesizeRequest
 * @Author tjh
 * @Date 19/4/15 下午2:57
 * @Version 1.0
 **/
public class SynthesizeRequest {
    @JsonProperty("u_ch_id")
    private String uChId;
    @JsonProperty("u_name")
    private String uName;
    @JsonProperty("m_ch_id")
    private String mChId;
    @JsonProperty("m_content")
    private String mContent;
    @JsonProperty("g_asp")
    private String gAsp;
    @JsonProperty("m_publish_time")
    private String mPublishTime;
    @JsonProperty("keyword_json")
    private String keywordJson;

    public String getuChId() {
        return uChId;
    }

    public void setuChId(String uChId) {
        this.uChId = uChId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getmChId() {
        return mChId;
    }

    public void setmChId(String mChId) {
        this.mChId = mChId;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getgAsp() {
        return gAsp;
    }

    public void setgAsp(String gAsp) {
        this.gAsp = gAsp;
    }

    public String getmPublishTime() {
        return mPublishTime;
    }

    public void setmPublishTime(String mPublishTime) {
        this.mPublishTime = mPublishTime;
    }

    public String getKeywordJson() {
        return keywordJson;
    }

    public void setKeywordJson(String keywordJson) {
        this.keywordJson = keywordJson;
    }
}
