package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.core.session.receive.REAbstractReceiveMessageHandler;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.REMessage;
import cn.ac.iie.domain.NewsComment;
import cn.ac.iie.dto.requests.*;
import cn.ac.iie.dto.response.*;
import cn.ac.iie.feign.*;
import cn.ac.iie.utils.CommonConstant;
import cn.ac.iie.utils.FileUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SuccessHandler extends REAbstractReceiveMessageHandler<REMessage> {
    private static final Logger logger = Logger.getLogger(SuccessHandler.class);
    private Map<String, Integer> colNameIndex = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private InsertService insertService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(InsertService.class, CommonConstant.get("insert_url"));
    private InsertVideoService insertVideoService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(InsertVideoService.class, CommonConstant.get("insert_url"));
    private FilterService filterService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(FilterService.class, CommonConstant.get("filter_url"));
    private SynthesizeService synthesizeService = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(SynthesizeService.class, CommonConstant.get("synthesize_url"));



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

            for (Object[] data : message.data) {
                //logger.info("data::::::::::::"+data);
                long allStart = System.currentTimeMillis();
                count++;

//                if (count % Integer.parseInt(CommonConstant.get("deal_num")) == 0) {

                    // 读取数据
                    String[] colNames = message.colNames;
                    logger.info(Arrays.deepToString(colNames));
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
                            if(colName.equals("m_risk_label")){
                                value = null;
                            }
                            propertiesMap.put(col, value);
                        }
                    }

                    //logger.info("数据：：："+propertiesMap);
                    // ==============================================================================
                    
                    NewsComment temp = new NewsComment();
                    BeanUtils.populate(temp, propertiesMap);


                    logger.info("格式"+temp);

                    //下载
                    /*Integer xiazaicount=0;
                    String[] videoFiles= temp.getmVideoFiles();
                    String url = "http://10.136.134.35:20099/get?key="+videoFiles[0];
                    logger.info("下载地址"+url);
                    String tmpDir = "/home/iie/videodownload/" + "video/"  + "douyin/";
                    File folder = new File(tmpDir);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                    String filePath = tmpDir + File.separator +url.substring(url.lastIndexOf("@") + 1);
                    File f = new File(filePath);
                    try {
                        FileUtils.downloadFile(url, f);
                        logger.info("下载成功");
                    } catch (Exception e) {
                        logger.info(e.toString());
                        logger.info("下载失败");
                    }*/

                    //request.addData(temp);//不过引擎直接入库

                    String contentId = (String) propertiesMap.get("gChKey");
                    String content = (String) propertiesMap.get("mContent");
                    if(StringUtils.isBlank(content)){
                        content = (String) propertiesMap.get("mTitle");
                    }

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
//                            SynthesizeRequest synthesizeRequest = new SynthesizeRequest();
//                            synthesizeRequest.setuChId(temp.getuChId());
//                            synthesizeRequest.setmChId(temp.getmChId());
//                            synthesizeRequest.setuName(temp.getuName());
//                            synthesizeRequest.setmContent(content);
//                            synthesizeRequest.setmPublishTime(sdf.format(temp.getmPublishTime()));
//                            synthesizeRequest.setgAsp(temp.getgAsp());
                            JSONObject synthesizeRequest = new JSONObject();
                            JSONArray datas = new JSONArray();
                            JSONObject json= new JSONObject();
                            json.put("m_content",temp.getmContent());
                            json.put("m_publish_time",sdf.format(temp.getmPublishTime()));
                            json.put("u_name",temp.getuName());
                            json.put("u_ch_id",temp.getuChId());
                            json.put("m_ch_id",temp.getmChId());
                            json.put("g_asp",temp.getgAsp());
                            datas.add(json);
                            synthesizeRequest.put("data",datas);
//                            logger.info("综合引擎请求参数:::"+synthesizeRequest);
//                            //SynthesizeResponse synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
//                            JSONObject synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
//                            logger.info("综合引擎返回：：："+synthesizeResponse);
//                            JSONArray redatas = synthesizeResponse.getJSONArray("data");
//                            JSONObject  redata = redatas.getJSONObject(0);
//                            String suggest = redata.getString("suggest");
//
//                            if("Harm".equalsIgnoreCase(suggest)) {
                                //if(true) {
                                logger.info("数据判定有害");

                                // 构建数据
                                List<Result> results = filterRes.getDescs();
                                logger.info("results:"+results);
                                int a=0;
                                List<String> maxSubjectIds = new ArrayList<>();
                                for (int i = 0; i < results.size(); i++) {
                                    Result result=results.get(i);
                                    String hitBlack = result.getHitBlack();
                                    List<String> subjectIds = result.getSubjectIds();
                                    int b= subjectIds.size();
                                    if(b>a){
                                        a=b;
                                        maxSubjectIds = subjectIds;
                                    }
                                    if(i==(results.size()-1)){
                                        for (String subjectId : maxSubjectIds) {
                                            //只存第一个关键词和
                                            //String subjectId=subjectIds.get(0);
                                            NewsComment newsComment = new NewsComment();
                                            BeanUtils.populate(newsComment, propertiesMap);
                                            newsComment.setHitKeyword(hitBlack);
                                            newsComment.setTopicId(subjectId);

                                            if(content.length() < 150) {
                                                newsComment.setmAbstract(content);
                                            } else {
                                                newsComment.setmAbstract(result.getHitAbstract());
                                            }



                                            logger.info("构建后数据为：：："+newsComment);

                                            request.addData(newsComment);
                                        }
                                    }
                                }
//                            }
                        }
                    }
//                } else {
//                    logger.info("丢弃数据" + data[colNameIndex.get("g_ch_key")]);
//                }

                logger.info(data[colNameIndex.get("g_ch_key")] + ":::数据总耗时：：：" + (System.currentTimeMillis() - allStart));

            }

            if (request.getData()!=null) {
                logger.info("调用插入接口：：："+new ObjectMapper().writeValueAsString(request));
                Response response=new Response();
                if(CommonConstant.get("dpt_mq_topic").contains("comment")){
                    response = insertService.insertNewsComment(request);
                }else{
                    response = insertVideoService.insertNewsComment(request);
                }
                logger.info("插入结果：：：："+response);

                logger.info("数据处理成功：" + message.data.size() + "条");
            }

        } catch (Exception e) {
            logger.error("数据处理失败", e);
            return false;
        }

        logger.info(":::::::" + Thread.currentThread().getName() + "：：：：handle结束：：：耗时：：：" + (System.currentTimeMillis() - handleStart));
        return true;
    }

    public static void main(String[] args) {
        String aaa= "";
        parse();
        Map<String, Object> propertiesMap = new HashMap<>();
        List<String> a=new ArrayList<>();
        a.add("1");
        a.add("2");
        propertiesMap.put("mTags",a);
        logger.info(propertiesMap);
        NewsComment temp = new NewsComment();
        try {
            BeanUtils.populate(temp, propertiesMap);
            System.out.println(temp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void parse() {
        List<Result> results = new ArrayList<>();
        Result result = new Result();
        result.setHitBlack("主席");
        result.setHitAbstract("毛主席逝世43周年");
        List<String> ruids=new ArrayList<>(Arrays.asList("wrafdsgagag"));
        result.setRuleIds(ruids);
        List<String> subids=new ArrayList<>(Arrays.asList("fgsggagaaa"));
        result.setSubjectIds(subids);
        results.add(result);
        logger.info("results:"+results);
        int a=0;
        List<String> maxSubjectIds = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Result re=results.get(i);
            String hitBlack = re.getHitBlack();
            List<String> subjectIds = re.getSubjectIds();
            int b= subjectIds.size();
            if(b>a){
                a=b;
                maxSubjectIds = subjectIds;
            }
            System.out.println();
            if(i==(results.size()-1)){
                for (String subjectId : maxSubjectIds) {
                    System.out.println(subjectId);
                }
                    //只存第一个关键词和
                    //String subjectId=subjectIds.get(0);
//                    NewsComment newsComment = new NewsComment();
//                    BeanUtils.populate(newsComment, propertiesMap);
//                    newsComment.setHitKeyword(hitBlack);
//                    newsComment.setTopicId(subjectId);
//
//                    if(content.length() < 150) {
//                        newsComment.setmAbstract(content);
//                    } else {
//                        newsComment.setmAbstract(result.getHitAbstract());
//                    }
//
//
//
//                    logger.info("构建后数据为：：："+newsComment);
//
//                    request.addData(newsComment);
                }
            }
        }


}

