package cn.ac.iie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsComment {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date insertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer markType;

    private String markUserId;

    private Date markTime;

    private String similarity;

    private Integer similarCnt;

    private Integer dataType;

    private String topicId;








    private String uName;
    private String gChKey;
    private String mRootGChKey;
    private String mContent;
    private String uSendIp;
    private String uLocCounty;
    private String msgVersion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mInsertTime;
    private String uGChKey;
    private String uChId;
    private String mRiskLabel;
    private String uSendLocation;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mSnapshotTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mPublishTime;
    private String mParentId;
    private String msgType;
    private String mStatus;
    private String mRootChId;
    private String mIsPtGenerated;
    private String gAsp;
    private String uLocProvince;
    private String key;

    private String[] mTags;
    private String mTagsString;
    private String mChId;
    private String[] mVideoUrls;
    private String mVideoUrlsString;
    private String[] mVideoFiles;
    private String mVideoFilesString;
    private String mTitle;
    private String mType;
    private int mPlayCnt;
    private int mLikeCnt;
    private int mReplyCnt;
    private int mReportCnt;
    private int mForwardCnt;




    private String hitKeyword;
    private String mAbstract;
    private Integer isDelete;



    private NewsComment parent;
    private int newest;
    private List<String> similarIds;
    private List<String> similarUrls;
    private String publishTime;
    private String gkStatus;
    private String type;
    private List<String> ids;
    private String emotion;

    public String getmTagsString() {
        return mTagsString;
    }

    public void setmTagsString(String mTagsString) {
        this.mTagsString = mTagsString;
    }

    public String getmVideoUrlsString() {
        return mVideoUrlsString;
    }

    public void setmVideoUrlsString(String mVideoUrlsString) {
        this.mVideoUrlsString = mVideoUrlsString;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMarkType() {
        return markType;
    }

    public void setMarkType(Integer markType) {
        this.markType = markType;
    }

    public String getMarkUserId() {
        return markUserId;
    }

    public void setMarkUserId(String markUserId) {
        this.markUserId = markUserId;
    }

    public Date getMarkTime() {
        return markTime;
    }

    public void setMarkTime(Date markTime) {
        this.markTime = markTime;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public Integer getSimilarCnt() {
        return similarCnt;
    }

    public void setSimilarCnt(Integer similarCnt) {
        this.similarCnt = similarCnt;
    }

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getgChKey() {
        return gChKey;
    }

    public void setgChKey(String gChKey) {
        this.gChKey = gChKey;
    }

    public String getuSendIp() {
        return uSendIp;
    }

    public void setuSendIp(String uSendIp) {
        this.uSendIp = uSendIp;
    }

    public String getuLocCounty() {
        return uLocCounty;
    }

    public void setuLocCounty(String uLocCounty) {
        this.uLocCounty = uLocCounty;
    }

    public String getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(String msgVersion) {
        this.msgVersion = msgVersion;
    }

    public Date getmInsertTime() {
        return mInsertTime;
    }

    public void setmInsertTime(Date mInsertTime) {
        this.mInsertTime = mInsertTime;
    }

    public String getuGChKey() {
        return uGChKey;
    }

    public void setuGChKey(String uGChKey) {
        this.uGChKey = uGChKey;
    }

    public String getuChId() {
        return uChId;
    }

    public void setuChId(String uChId) {
        this.uChId = uChId;
    }

    public String getmRiskLabel() {
        return mRiskLabel;
    }

    public void setmRiskLabel(String mRiskLabel) {
        this.mRiskLabel = mRiskLabel;
    }

    public String getuSendLocation() {
        return uSendLocation;
    }

    public void setuSendLocation(String uSendLocation) {
        this.uSendLocation = uSendLocation;
    }

    public Date getmSnapshotTime() {
        return mSnapshotTime;
    }

    public void setmSnapshotTime(Date mSnapshotTime) {
        this.mSnapshotTime = mSnapshotTime;
    }

    public Date getmPublishTime() {
        return mPublishTime;
    }

    public void setmPublishTime(Date mPublishTime) {
        this.mPublishTime = mPublishTime;
    }

    public String getmParentId() {
        return mParentId;
    }

    public void setmParentId(String mParentId) {
        this.mParentId = mParentId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmRootChId() {
        return mRootChId;
    }

    public void setmRootChId(String mRootChId) {
        this.mRootChId = mRootChId;
    }

    public String getmIsPtGenerated() {
        return mIsPtGenerated;
    }

    public void setmIsPtGenerated(String mIsPtGenerated) {
        this.mIsPtGenerated = mIsPtGenerated;
    }

    public String getgAsp() {
        return gAsp;
    }

    public void setgAsp(String gAsp) {
        this.gAsp = gAsp;
    }

    public String getuLocProvince() {
        return uLocProvince;
    }

    public void setuLocProvince(String uLocProvince) {
        this.uLocProvince = uLocProvince;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String[] getmTags() {
        return mTags;
    }

    public void setmTags(String[] mTags) {
        this.mTags = mTags;
    }

    public String[] getmVideoFiles() {
        return mVideoFiles;
    }

    public void setmVideoFiles(String[] mVideoFiles) {
        this.mVideoFiles = mVideoFiles;
    }

    public String getmVideoFilesString() {
        return mVideoFilesString;
    }

    public void setmVideoFilesString(String mVideoFilesString) {
        this.mVideoFilesString = mVideoFilesString;
    }

    public int getmPlayCnt() {
        return mPlayCnt;
    }

    public void setmPlayCnt(int mPlayCnt) {
        this.mPlayCnt = mPlayCnt;
    }

    public int getmLikeCnt() {
        return mLikeCnt;
    }

    public void setmLikeCnt(int mLikeCnt) {
        this.mLikeCnt = mLikeCnt;
    }

    public int getmReplyCnt() {
        return mReplyCnt;
    }

    public void setmReplyCnt(int mReplyCnt) {
        this.mReplyCnt = mReplyCnt;
    }

    public int getmReportCnt() {
        return mReportCnt;
    }

    public void setmReportCnt(int mReportCnt) {
        this.mReportCnt = mReportCnt;
    }

    public int getmForwardCnt() {
        return mForwardCnt;
    }

    public void setmForwardCnt(int mForwardCnt) {
        this.mForwardCnt = mForwardCnt;
    }

    public String getmChId() {
        return mChId;
    }

    public void setmChId(String mChId) {
        this.mChId = mChId;
    }


    public String[] getmVideoUrls() {
        return mVideoUrls;
    }

    public void setmVideoUrls(String[] mVideoUrls) {
        this.mVideoUrls = mVideoUrls;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getHitKeyword() {
        return hitKeyword;
    }

    public void setHitKeyword(String hitKeyword) {
        this.hitKeyword = hitKeyword;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAbstract() {
        return mAbstract;
    }

    public void setmAbstract(String mAbstract) {
        this.mAbstract = mAbstract;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getmRootGChKey() {
        return mRootGChKey;
    }

    public void setmRootGChKey(String mRootGChKey) {
        this.mRootGChKey = mRootGChKey;
    }

    public NewsComment getParent() {
        return parent;
    }

    public void setParent(NewsComment parent) {
        this.parent = parent;
    }

    public int getNewest() {
        return newest;
    }

    public void setNewest(int newest) {
        this.newest = newest;
    }

    public List<String> getSimilarIds() {
        return similarIds;
    }

    public void setSimilarIds(List<String> similarIds) {
        this.similarIds = similarIds;
    }

    public List<String> getSimilarUrls() {
        return similarUrls;
    }

    public void setSimilarUrls(List<String> similarUrls) {
        this.similarUrls = similarUrls;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getGkStatus() {
        return gkStatus;
    }

    public void setGkStatus(String gkStatus) {
        this.gkStatus = gkStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}