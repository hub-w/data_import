package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.core.session.receive.REAbstractReceiveMessageHandler;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.REMessage;
import cn.ac.iie.domain.NewsComment;
import cn.ac.iie.dto.requests.*;
import cn.ac.iie.dto.response.*;
import cn.ac.iie.feign.*;
import cn.ac.iie.utils.CommonConstant;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.*;

public class SuccessHandler extends REAbstractReceiveMessageHandler<REMessage> {
    private static final Logger logger = Logger.getLogger(SuccessHandler.class);
    private Map<String, Integer> colNameIndex = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private InsertService insertService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(InsertService.class, CommonConstant.get("insert_url"));
    private FilterService filterService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(FilterService.class, CommonConstant.get("filter_url"));
    private SynthesizeService synthesizeService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(SynthesizeService.class, CommonConstant.get("synthesize_url"));
    private SimilarService similarService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(SimilarService.class, CommonConstant.get("similar_url"));
    private AnalysisService analysisService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(AnalysisService.class, CommonConstant.get("analysis_url"));



    @Override
    public boolean handle(REMessage message) {
        long handleStart = System.currentTimeMillis();
        logger.info("handle开始：：：获取数据：" + message.data.size() + "条");

        try {
            logger.info("数据获取成功" + message.data.size());
            //如果colNameIndex为空，则遍历colNames，建立字段名和索引之间的映射
            if (colNameIndex.isEmpty()) {
                logger.info("建立字段与索引之间的映射");
                for (int i = 0; i < message.colNames.length; i++) {
                    colNameIndex.put(message.colNames[i], i);
                }
                logger.info("映射建立成功");
                System.out.println(colNameIndex);
            }

            //获取数据并存入Map
            //遍历数据集合，获得每条数据的数组
            Request<NewsComment> request = new Request<>();
            int count = 0;
            SimilarUpdateRequest similarUpdateRequest = new SimilarUpdateRequest();

            for (Object[] data : message.data) {
                logger.info("data::::::::::::"+data);
                long allStart = System.currentTimeMillis();
                count++;
                if (count % Integer.parseInt(CommonConstant.get("deal_num")) == 0) {
                    // 读取数据
                    String[] colNames = message.colNames;
                    Map<String, Object> propertiesMap = new HashMap<>();
                    for (String colName : colNames) {
                        String col = CommonConstant.get(colName);
                        if(col != null) {
                            Object value = null;
                            if(colName.endsWith("time")) {
                                value = new Date((Long) data[colNameIndex.get(colName)] * 1000);
                            } else {
                                value = data[colNameIndex.get(colName)];
                            }
                            propertiesMap.put(col, value);
                        }
                    }

                    logger.info("数据：：："+propertiesMap);
                    
                    NewsComment temp = new NewsComment();
                    BeanUtils.populate(temp, propertiesMap);

                    String contentId = (String) propertiesMap.get("gChKey");
                    String content = (String) propertiesMap.get("mContent");

                    // 1 filter hitblack
                    FilterRequest filterRequest = new FilterRequest();
                    // 构建过滤请求参数
                    filterRequest.setSysCode(CommonConstant.get("filter_sys_code"));
                    filterRequest.setId(contentId);
                    filterRequest.setText(content);

                    logger.info("过滤请求参数：：："+new ObjectMapper().writeValueAsString(filterRequest));
                    FilterResponse filterResponse = filterService.filter(filterRequest);
                    String filterResponseJson = new ObjectMapper().writeValueAsString(filterResponse);
                    logger.info("过滤返回：：："+filterResponseJson);
                    if(filterResponse.isStatus()) {
                        FilterRes filterRes = filterResponse.getFilterRes();
                        if(filterRes.getIsPush()) {
                            // 2、综合引擎
                            SynthesizeRequest synthesizeRequest = new SynthesizeRequest();

                            synthesizeRequest.setuChId(temp.getuGChKey());
                            synthesizeRequest.setmChId(temp.getuGChKey());
                            synthesizeRequest.setuName(temp.getuName());
//                            synthesizeRequest.setKeywordJson(filterResponseJson);
                            synthesizeRequest.setmContent(content);
                            synthesizeRequest.setmPublishTime(sdf.format(temp.getmPublishTime()));
                            synthesizeRequest.setgAsp(temp.getgAsp());

                            logger.info("综合引擎请求参数:::"+new ObjectMapper().writeValueAsString(synthesizeRequest));
                            SynthesizeResponse synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
                            logger.info("综合引擎返回：：："+new ObjectMapper().writeValueAsString(synthesizeResponse));

                            if("harm".equalsIgnoreCase(synthesizeResponse.getSuggest())) {
//                            if(true) {
                                logger.info("数据判定有害，执行相似、情感分析");
                                // 3、情感分析
                                AnalysisRequest analysisRequest = new AnalysisRequest();
                                analysisRequest.setData(content);
                                logger.info("情感分析请求:::"+new ObjectMapper().writeValueAsString(analysisRequest));
                                AnalysisResponse analysisResponse = analysisService.analysis(analysisRequest);
                                logger.info("情感分析返回:::"+new ObjectMapper().writeValueAsString(analysisResponse));
                                Integer sentiment = analysisResponse.getSentiment();
                                // 4、相似
                                SimilarRequest similarRequest = new SimilarRequest();
                                similarRequest.setId(contentId);
                                similarRequest.setContent(content);
                                logger.info("相似请求:::"+new ObjectMapper().writeValueAsString(similarRequest));
                                SimilarResponse similarResponse = similarService.similar(similarRequest);
                                logger.info("相似返回:::"+new ObjectMapper().writeValueAsString(similarResponse));
                                List<String> similarIds = similarResponse.getIdList();

                                // 4.2、更新相似
                                SimilarUpdateData similarUpdateData = new SimilarUpdateData();

                                similarUpdateData.setId(contentId);
                                similarUpdateData.setContent(content);

                                similarUpdateRequest.addData(similarUpdateData);


                                // 构建数据
                                List<Result> results = filterRes.getDescs();
                                for (Result result : results) {
                                    String hitBlack = result.getHitBlack();
                                    List<String> subjectIds = result.getSubjectIds();
                                    for (String subjectId : subjectIds) {
                                        NewsComment newsComment = new NewsComment();
                                        BeanUtils.populate(newsComment, propertiesMap);
                                        newsComment.setHitKeyword(hitBlack);
                                        newsComment.setTopicId(subjectId);
                                        newsComment.setEmotion(sentiment.toString());
                                        newsComment.setSimilarIds(similarIds);
                                        newsComment.setDataType(Integer.parseInt(CommonConstant.get("data_type")));

                                        if(StringUtils.isBlank(newsComment.getmAbstract())) {
                                            newsComment.setmAbstract(result.getHitAbstract());
                                        }

                                        logger.info("构建后数据为：：："+newsComment);

                                        request.addData(newsComment);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    logger.info("丢弃数据" + data[colNameIndex.get("g_ch_key")]);
                }

                logger.info(data[colNameIndex.get("g_ch_key")] + ":::数据总耗时：：：" + (System.currentTimeMillis() - allStart));
            }

            logger.info("调用插入接口：：："+new ObjectMapper().writeValueAsString(request));
            Response response = insertService.insertNewsComment(request);
            logger.info("插入结果：：：："+response);


            logger.info("相似更新请求:::"+new ObjectMapper().writeValueAsString(similarUpdateRequest));
            SimilarUpdateResponse similarUpdateResponse = similarService.update(similarUpdateRequest);
            logger.info("相似更新返回:::"+new ObjectMapper().writeValueAsString(similarUpdateResponse));


            logger.info("数据处理成功：" + message.data.size() + "条");
        } catch (Exception e) {
            logger.error("数据处理失败", e);
            return false;
        }

        logger.info(":::::::" + Thread.currentThread().getName() + "：：：：handle结束：：：耗时：：：" + (System.currentTimeMillis() - handleStart));
        return true;
    }
}
