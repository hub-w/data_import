package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.core.session.receive.REAbstractReceiveMessageHandler;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.REMessage;
import cn.ac.iie.domain.NewsComment;
import cn.ac.iie.dto.requests.FilterRequest;
import cn.ac.iie.dto.requests.Request;
import cn.ac.iie.dto.response.FilterRes;
import cn.ac.iie.dto.response.FilterResponse;
import cn.ac.iie.dto.response.Response;
import cn.ac.iie.dto.response.Result;
import cn.ac.iie.feign.FilterService;
import cn.ac.iie.feign.InsertService;
import cn.ac.iie.feign.InsertVideoService;
import cn.ac.iie.feign.SynthesizeService;
import cn.ac.iie.utils.CommonConstant;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.awt.im.spi.InputMethod;
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


    public boolean handle(REMessage message) {
        long handleStart = System.currentTimeMillis();
        logger.info("handle开始：：：获取数据：" + message.data.size() + "条");

        try {
            logger.info("数据获取成功" + message.data.size());
            if (colNameIndex.isEmpty()) {
                logger.info("建立字段与索引之间的映射");
                for (int i = 0; i < message.colNames.length; i++) {
                    colNameIndex.put(message.colNames[i], i);
                }
                logger.info("映射建立成功");
                System.out.println(colNameIndex);
            }
            logger.info("映射耗时:"+(System.currentTimeMillis()-handleStart));
            Request<NewsComment> request = new Request();
            ExecutorService threadPool = ThreadPool.getThreadPool();
            long messageTime = System.currentTimeMillis();
            List<Future<NewsComment>> futures = new ArrayList<Future<NewsComment>>();
            for (Object[] data : message.data) {
                futures.add(threadPool.submit(new SuccessHandlerThread(data, message, colNameIndex, logger, filterService, sdf)));
            }

            logger.info("遍历评论耗时:" + (System.currentTimeMillis() - messageTime));
            long addTime = System.currentTimeMillis();
            for (Future<NewsComment> future : futures) {
                try {
                    long oneFuture = System.currentTimeMillis();
                    NewsComment newsComment = future.get();
                    logger.info("future.get耗时" + (System.currentTimeMillis() - oneFuture));
                    if (newsComment.getgChKey() != null) {
                        request.addData(newsComment);
                        logger.info("添加单条消息耗时:" + (System.currentTimeMillis() - oneFuture));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.info("报错:" + e);
                }
            }
            logger.info("addData遍历添加耗时:" + (System.currentTimeMillis() - addTime));


            long insertTime = System.currentTimeMillis();
            if (request.getData() != null) {
                logger.info("调用插入接口：：：" + new ObjectMapper().writeValueAsString(request));
                Response response = new Response();
                if (CommonConstant.get("dpt_mq_topic").contains("comment")) {
                    response = insertService.insertNewsComment(request);
                } else {
                    response = insertVideoService.insertNewsComment(request);
                }
                logger.info("插入结果：：：：" + response);

                logger.info("数据处理成功：" + message.data.size() + "条");
            }
            logger.info("getData插入耗时:" + (System.currentTimeMillis() - insertTime));
        } catch (Exception e) {
            logger.error("数据处理失败", e);
            return false;
        }


        logger.info(":::::::" + Thread.currentThread().getName() + "：：：：handle结束：：：耗时：：：" + (System.currentTimeMillis() - handleStart));
        return true;
    }

    public static void main(String[] args) {
        String aaa = "";
        parse();
        Map<String, Object> propertiesMap = new HashMap<>();
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        propertiesMap.put("mTags", a);
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
        List<String> ruids = new ArrayList<>(Arrays.asList("wrafdsgagag"));
        result.setRuleIds(ruids);
        List<String> subids = new ArrayList<>(Arrays.asList("fgsggagaaa"));
        result.setSubjectIds(subids);
        results.add(result);
        logger.info("results:" + results);
        int a = 0;
        List<String> maxSubjectIds = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            Result re = results.get(i);
            String hitBlack = re.getHitBlack();
            List<String> subjectIds = re.getSubjectIds();
            int b = subjectIds.size();
            if (b > a) {
                a = b;
                maxSubjectIds = subjectIds;
            }
            System.out.println();
            if (i == (results.size() - 1)) {
                for (String subjectId : maxSubjectIds) {
                    System.out.println(subjectId);
                }
            }
        }
    }


}
class SuccessHandlerThread implements Callable<NewsComment> {
    Object[] data;
    REMessage message;
    Map<String, Integer> colNameIndex;
    Logger logger;
    SimpleDateFormat sdf;
    FilterService filterService;

    public SuccessHandlerThread(Object[] data1, REMessage message1, Map<String, Integer> colNameIndex1, Logger logger1, FilterService filterService1, SimpleDateFormat sdf1) {
        super();
        this.data = data1;
        this.message = message1;
        this.colNameIndex = colNameIndex1;
        this.logger = logger1;
        this.filterService = filterService1;
        this.sdf = sdf1;
    }

    @Override
    public NewsComment call() throws Exception {
        logger.info("线程:" + Thread.currentThread().getName() + "数据:" + data);
        long allStart = System.currentTimeMillis();
        String[] colNames = message.colNames;
        logger.info(Arrays.deepToString(colNames));
        Map<String, Object> propertiesMap = new HashMap<>();
        for (String colName : colNames) {
            String col = CommonConstant.get(colName);

            if (col != null) {
                Object value = null;
                if (colName.endsWith("time")) {
                    value = new Date((Long) data[colNameIndex.get(colName)] * 1000);
                } else {
                    value = data[colNameIndex.get(colName)];
                }
                if (colName.equals("m_risk_label")) {
                    value = null;
                }
                propertiesMap.put(col, value);
            }
        }
        logger.info("第一块遍历耗时" + (System.currentTimeMillis() - allStart));
        long er = System.currentTimeMillis();

        NewsComment newsComment = new NewsComment();
        logger.info("<<<<<<<" + propertiesMap);

        String contentId = (String) propertiesMap.get("gChKey");
        String content = (String) propertiesMap.get("mContent");
        if (StringUtils.isBlank(content)) {
            content = (String) propertiesMap.get("mTitle");
        }

        FilterRequest filterRequest = new FilterRequest();

        filterRequest.setSysCode(CommonConstant.get("filter_sys_code"));
        filterRequest.setId(contentId);
        filterRequest.setText(content);
        long filterStart = System.currentTimeMillis();
        logger.info("过滤请求参数：：：" + new ObjectMapper().writeValueAsString(filterRequest));
        FilterResponse filterResponse = filterService.filter(filterRequest);
        logger.info("过滤总耗时:" + (System.currentTimeMillis() - filterStart));
        String filterResponseJson = new ObjectMapper().writeValueAsString(filterResponse);

        logger.info("过滤返回：：：" + filterResponseJson);
        long san = System.currentTimeMillis();
        if (filterResponse.isStatus()) {
            FilterRes filterRes = filterResponse.getFilterRes();
            if (filterRes.getIsPush()) {
                JSONObject synthesizeRequest = new JSONObject();
                JSONArray datas = new JSONArray();
                JSONObject json = new JSONObject();
                json.put("m_content", propertiesMap.get("mContent"));
                json.put("m_publish_time", sdf.format(propertiesMap.get("mPublishTime")));
                json.put("u_name", propertiesMap.get("uName"));
                json.put("u_ch_id", propertiesMap.get("uChId"));
                json.put("m_ch_id", propertiesMap.get("mChId"));
                json.put("g_asp", propertiesMap.get("gAsp"));
                datas.add(json);
                logger.info("添加json耗时" + (System.currentTimeMillis() - san));
                synthesizeRequest.put("data", datas);
                logger.info("数据判定有害");
                List<Result> results = filterRes.getDescs();
                logger.info("results:" + results);
                int a = 0;
                List<String> maxSubjectIds = new ArrayList<>();
                long si = System.currentTimeMillis();
                for (int i = 0; i < results.size(); i++) {
                    Result result = results.get(i);
                    String hitBlack = result.getHitBlack();
                    List<String> subjectIds = result.getSubjectIds();
                    int b = subjectIds.size();
                    if (b > a) {
                        a = b;
                        maxSubjectIds = subjectIds;
                    }
                    if (i == (results.size() - 1)) {
                        for (String subjectId : maxSubjectIds) {
                            long wu = System.currentTimeMillis();
                            BeanUtils.populate(newsComment, propertiesMap);
                            logger.info("BeanUtils耗时" + (System.currentTimeMillis() - wu));
                            newsComment.setHitKeyword(hitBlack);
                            newsComment.setTopicId(subjectId);
                            if (content.length() < 150) {
                                newsComment.setmAbstract(content);
                            } else {
                                newsComment.setmAbstract(result.getHitAbstract());
                            }
                            logger.info("构建后数据为：：：" + newsComment);
                        }
                    }
                }
                logger.info("遍历results耗时" + (System.currentTimeMillis() - si));
            }
        }
        logger.info("第三块判定有害耗时" + (System.currentTimeMillis() - san));

        logger.info(data[colNameIndex.get("g_ch_key")] + ":::数据总耗时：：：" + (System.currentTimeMillis() - allStart));
        return newsComment;
    }
}
