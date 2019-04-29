package cn.ac.iie.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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

    private String mUrlDomain;

    private String gChKey;

    private String mUrl;

    private String uGChKey;

    private String uName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mPublishTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mUploadTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mDownTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mDelTime;

    private Integer moStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date moTime;

    private Long mReplyCnt;

    private Long mJoinCnt;

    private Long mForwardCnt;

    private Long uFavouriteCnt;

    private Long mReportCnt;

    private Long mLikeCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mInsertTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mGatherTime;

    private Integer mTitleLen;

    private Integer mContentLen;

    private String gAsp;

    private String gAdp;

    private Integer mSentiment;

    private Integer mType;

    private String mChId;

    private Integer uType;

    private String mMentionedSubjects;

    private Long mReadCnt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date mUpdateTime;

    private String mCategoryTag;


    private String mSource;
    private String hitKeyword;
    private String mTitle;
    private String mAbstract;
    private String mContent;
    private Integer isDelete;
    private String mRootGChKey;


    private NewsComment parent;
    private int newest;
    private List<String> similarIds;
    private List<String> similarUrls;
    private String publishTime;
    private String gkStatus;
    private String type;
    private List<String> ids;
    private String emotion;

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

    public String getmUrlDomain() {
        return mUrlDomain;
    }

    public void setmUrlDomain(String mUrlDomain) {
        this.mUrlDomain = mUrlDomain;
    }

    public String getgChKey() {
        return gChKey;
    }

    public void setgChKey(String gChKey) {
        this.gChKey = gChKey;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getuGChKey() {
        return uGChKey;
    }

    public void setuGChKey(String uGChKey) {
        this.uGChKey = uGChKey;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public Date getmPublishTime() {
        return mPublishTime;
    }

    public void setmPublishTime(Date mPublishTime) {
        this.mPublishTime = mPublishTime;
    }

    public Date getmUploadTime() {
        return mUploadTime;
    }

    public void setmUploadTime(Date mUploadTime) {
        this.mUploadTime = mUploadTime;
    }

    public Date getmDownTime() {
        return mDownTime;
    }

    public void setmDownTime(Date mDownTime) {
        this.mDownTime = mDownTime;
    }

    public Date getmDelTime() {
        return mDelTime;
    }

    public void setmDelTime(Date mDelTime) {
        this.mDelTime = mDelTime;
    }

    public Integer getMoStatus() {
        return moStatus;
    }

    public void setMoStatus(Integer moStatus) {
        this.moStatus = moStatus;
    }

    public Date getMoTime() {
        return moTime;
    }

    public void setMoTime(Date moTime) {
        this.moTime = moTime;
    }

    public Long getmReplyCnt() {
        return mReplyCnt;
    }

    public void setmReplyCnt(Long mReplyCnt) {
        this.mReplyCnt = mReplyCnt;
    }

    public Long getmJoinCnt() {
        return mJoinCnt;
    }

    public void setmJoinCnt(Long mJoinCnt) {
        this.mJoinCnt = mJoinCnt;
    }

    public Long getmForwardCnt() {
        return mForwardCnt;
    }

    public void setmForwardCnt(Long mForwardCnt) {
        this.mForwardCnt = mForwardCnt;
    }

    public Long getuFavouriteCnt() {
        return uFavouriteCnt;
    }

    public void setuFavouriteCnt(Long uFavouriteCnt) {
        this.uFavouriteCnt = uFavouriteCnt;
    }

    public Long getmReportCnt() {
        return mReportCnt;
    }

    public void setmReportCnt(Long mReportCnt) {
        this.mReportCnt = mReportCnt;
    }

    public Long getmLikeCnt() {
        return mLikeCnt;
    }

    public void setmLikeCnt(Long mLikeCnt) {
        this.mLikeCnt = mLikeCnt;
    }

    public Date getmInsertTime() {
        return mInsertTime;
    }

    public void setmInsertTime(Date mInsertTime) {
        this.mInsertTime = mInsertTime;
    }

    public Date getmGatherTime() {
        return mGatherTime;
    }

    public void setmGatherTime(Date mGatherTime) {
        this.mGatherTime = mGatherTime;
    }

    public Integer getmTitleLen() {
        return mTitleLen;
    }

    public void setmTitleLen(Integer mTitleLen) {
        this.mTitleLen = mTitleLen;
    }

    public Integer getmContentLen() {
        return mContentLen;
    }

    public void setmContentLen(Integer mContentLen) {
        this.mContentLen = mContentLen;
    }

    public String getgAsp() {
        return gAsp;
    }

    public void setgAsp(String gAsp) {
        this.gAsp = gAsp;
    }

    public String getgAdp() {
        return gAdp;
    }

    public void setgAdp(String gAdp) {
        this.gAdp = gAdp;
    }

    public Integer getmSentiment() {
        return mSentiment;
    }

    public void setmSentiment(Integer mSentiment) {
        this.mSentiment = mSentiment;
    }

    public Integer getmType() {
        return mType;
    }

    public void setmType(Integer mType) {
        this.mType = mType;
    }

    public String getmChId() {
        return mChId;
    }

    public void setmChId(String mChId) {
        this.mChId = mChId;
    }

    public Integer getuType() {
        return uType;
    }

    public void setuType(Integer uType) {
        this.uType = uType;
    }

    public String getmMentionedSubjects() {
        return mMentionedSubjects;
    }

    public void setmMentionedSubjects(String mMentionedSubjects) {
        this.mMentionedSubjects = mMentionedSubjects;
    }

    public Long getmReadCnt() {
        return mReadCnt;
    }

    public void setmReadCnt(Long mReadCnt) {
        this.mReadCnt = mReadCnt;
    }

    public Date getmUpdateTime() {
        return mUpdateTime;
    }

    public void setmUpdateTime(Date mUpdateTime) {
        this.mUpdateTime = mUpdateTime;
    }

    public String getmCategoryTag() {
        return mCategoryTag;
    }

    public void setmCategoryTag(String mCategoryTag) {
        this.mCategoryTag = mCategoryTag;
    }

    public String getmSource() {
        return mSource;
    }

    public void setmSource(String mSource) {
        this.mSource = mSource;
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