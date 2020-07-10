package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.data.*;
import cn.ac.iie.domain.NewsComment;
import cn.ac.iie.dto.requests.Request;
import cn.ac.iie.dto.response.*;
import cn.ac.iie.feign.FilterService;
import cn.ac.iie.feign.InsertService;
import cn.ac.iie.feign.InsertVideoService;
import cn.ac.iie.feign.SynthesizeService;
import cn.ac.iie.utils.CommonConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.beanutils.BeanUtils;

import org.apache.rocketmq.common.message.MessageExt;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import org.apache.log4j.Logger;

public class SuccessHandler {
    private static final Logger logger = Logger.getLogger(SuccessHandler.class);
    //    private Map<String, Integer> colNameIndex = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Integer pointsDataLimit = Integer.parseInt(CommonConstant.get("pointsDataLimit"));

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


    public BiConsumer<MessageExt, REPac> handler = (msg, pac) -> {
        long handleStart = System.currentTimeMillis();
        List<RERecord> recs = pac.getRecs();
        logger.info("handle开始：：：获取数据：" + recs.size() + "条");

        try {
            Request<NewsComment> request = new Request();
            ExecutorService threadPool = ThreadPool.getThreadPool();
            long messageTime = System.currentTimeMillis();
            List<Future<List<NewsComment>>> futures = new ArrayList<Future<List<NewsComment>>>();
            JSONArray array = new JSONArray();
            for (int i = 0; i < recs.size(); i++) {
                RERecord rec = recs.get(i);
                //}
                //for (RERecord rec : recs) {
                long l = System.currentTimeMillis();
                JSONObject object = transform(pac, rec);
                array.add(object);
                //if (array.size() == 10 || i == recs.size() - 1) {
                //    futures.add(threadPool.submit(new SuccessHandlerThread(array, pac, logger, filterService, sdf)));
                //    array.clear();
                //}
            }

            if(array.size()>pointsDataLimit){
                int part = array.size()/pointsDataLimit;//分批数
                for (int i = 0; i < part; i++) {
                    List<Object> templte = array.subList(0,pointsDataLimit);
                    JSONArray arraytemplete = JSONArray.parseArray(JSON.toJSONString(templte));
                    futures.add(threadPool.submit(new SuccessHandlerThread(arraytemplete, pac, logger, filterService, sdf)));
                    array.subList(0,pointsDataLimit).clear();
                }
                if(!array.isEmpty()){
                    futures.add(threadPool.submit(new SuccessHandlerThread(array, pac, logger, filterService, sdf)));
                }
            }else{
                futures.add(threadPool.submit(new SuccessHandlerThread(array, pac, logger, filterService, sdf)));
            }

            logger.info("遍历评论耗时:" + (System.currentTimeMillis() - messageTime));
            long addTime = System.currentTimeMillis();
            for (Future<List<NewsComment>> future : futures) {
                logger.info(futures.size());
                try {
                    long oneFuture = System.currentTimeMillis();
                    List<NewsComment> newsComments = future.get();
                    //logger.info("newsComment>>>>gchkey>>>" + newsComment.getgChKey());
                    //logger.info("future.get耗时" + (System.currentTimeMillis() - oneFuture));
                    //if (newsComment.getgChKey() != null) {
                    if (newsComments != null) {
                        for (NewsComment newsComment : newsComments) {
                            request.addData(newsComment);
                        }
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

                logger.info("数据处理成功：" + recs.size() + "条");
            }
            logger.info("getData插入耗时:" + (System.currentTimeMillis() - insertTime));
        } catch (Exception e) {
            logger.error("数据处理失败", e);
//            return false;
        }


        logger.info(":::::::" + Thread.currentThread().getName() + "：：：：handle结束：：：耗时：：：" + (System.currentTimeMillis() - handleStart));
//        return true;
    };

    private JSONObject transform(REPac pac, RERecord rec) {
        JSONObject object = new JSONObject();
        //handle the record by iterator
        rec.forEach((RERecord.Field f) -> {
            String key = f.getKey();
            REValue value = f.getValue();
            REFieldType type = value.getType();
            switch (type) {
                /// Basic Value
                case Boolean: {
                    //get value
                    Boolean v = ((REBasicValue.REBoolean) value).getValue();
                    //do something
                    object.put(key, v);
                    break;
                }
                case Int: {
                    Integer v = ((REBasicValue.REInt) value).getValue();
                    object.put(key, v);
                    break;
                }
                case Long: {
                    Long v = ((REBasicValue.RELong) value).getValue();
                    object.put(key, v);
                    break;
                }
                case Float: {
                    Float v = ((REBasicValue.REFloat) value).getValue();
                    object.put(key, v);
                    break;
                }
                case Double: {
                    Double v = ((REBasicValue.REDouble) value).getValue();
                    object.put(key, v);
                    break;
                }
                case String: {
                    String v = ((REBasicValue.REString) value).getValue();
                    object.put(key, v);
                    break;
                }
                case Binary: {
                    byte[] v = ((REBasicValue.REBinary) value).getValue();
                    object.put(key, v);
                    break;
                }
                case Struct: {
                    //get map value
                    Map<String, ?> mapValue = ((REBasicValue.REStruct) value).getValue();
                    //get type info of this map, which describes the type of each value in mapValue.
                    Map<String, REFieldType> detailType = pac.getDetail(key);
                    //do some with the type information
                    mapValue.forEach((k, v) -> {
                        switch (detailType.get(k)) {
                            case Int:
                            case Long:
                            default:
                        }
                    });
                    object.put(key, mapValue);
                    break;
                }
                /// Arrays
                case Array: {
                    Object obj = value.getValue();

                    List<Object> list = new ArrayList<Object>();
                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(obj));
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jObj = array.getJSONObject(i);
                        String t = jObj.getString("value");
                        list.add(t);
                    }
                    object.put(key, list);

                    break;
                }
                default:
            }
        });
        return object;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3};
        try {
            int m= arr[4];
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("catch之后");

        String aaa = "";
        parse();
        Map<String, Object> propertiesMap = new HashMap<>();
        List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        propertiesMap.put("mTags", a);
        logger.info(propertiesMap.toString());
        NewsComment temp = new NewsComment();
        try {
            BeanUtils.populate(temp, propertiesMap);
            System.out.println(temp);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println("catch");

        ExecutorService threadPool = ThreadPool.getThreadPool();
        long messageTime = System.currentTimeMillis();
        List<Future<Integer>> futures = new ArrayList<>();
        JSONArray array = new JSONArray();
        for (int i = 0; i < 11; i++) {
            JSONObject object = new JSONObject();
            object.put("i",i);
            array.add(object);
        }
        if(array.size()>2){
            int part = array.size()/2;//分批数
            for (int i = 0; i < part; i++) {
                System.out.println("array"+array);
                List<Object> templte = array.subList(0,2);
                JSONArray arraytemplete = JSONArray.parseArray(JSON.toJSONString(templte));
                futures.add(threadPool.submit(new Thread1(arraytemplete)));
                array.subList(0,2).clear();
            }
            if(!array.isEmpty()){
                futures.add(threadPool.submit(new Thread1(array)));
            }
        }else{
            futures.add(threadPool.submit(new Thread1(array)));
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

class SuccessHandlerThread implements Callable<List<NewsComment>> {
    JSONArray array;
    REPac pac;
    Logger logger;
    SimpleDateFormat sdf;
    FilterService filterService;

    public SuccessHandlerThread(JSONArray array, REPac pac1, Logger logger1, FilterService filterService1, SimpleDateFormat sdf1) {
        super();
        this.array = array;
        this.pac = pac1;
        this.logger = logger1;
        this.filterService = filterService1;
        this.sdf = sdf1;
    }

    @Override
    public List<NewsComment> call() throws Exception {
        //logger.info("线程:" + Thread.currentThread().getName() + "数据:" + array);
        long allStart = System.currentTimeMillis();
        //logger.info("<<<<<<<" + array.toString());

        List<NewsComment> newsComments = new ArrayList<>();
        JSONArray arrayRequest = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String contentId = object.getString("g_ch_key");
            String content = object.getString("m_content");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",contentId);
            jsonObject.put("text",content);
            jsonObject.put("sys_code",CommonConstant.get("filter_sys_code"));
            arrayRequest.add(jsonObject);
        }
/*        if (StringUtils.isBlank(content)) {
            content = object.getString("m_title");
        }*/

        long filterStart = System.currentTimeMillis();
        //logger.info("过滤请求参数：：：" + new ObjectMapper().writeValueAsString(filterRequest));
        //JSONArray arrayRequest= JSONArray.parseArray(JSON.toJSONString(filterRequests));
        logger.info("过滤请求参数：：：" + arrayRequest);
        //JSONObject filterResponses = null;
        FilterResult filterResponses = null;
        try {
            filterResponses = filterService.filter(arrayRequest);
        } catch (Exception e) {
            logger.info("报错：" + e);
            logger.info("报错对象：" + array.toString());
            e.printStackTrace();
        }
        logger.info("过滤总耗时:" + (System.currentTimeMillis() - filterStart));
        //String filterResponseJson = new ObjectMapper().writeValueAsString(filterResponse);

        logger.info("过滤返回：：：" + filterResponses);
        long san = System.currentTimeMillis();

        //String filterResponsesDecode = Base64.decode(filterResponses);
        //JSONObject jsonObject = JSONObject.parseObject(filterResponses);
        //JSONArray array = filterResponses.getJSONArray("result");
        //List<FilterResponse> filterResponseList = JSONObject.parseArray(array.toJSONString(), FilterResponse.class);
        List<FilterResponse> filterResponseList = filterResponses.getResult();
        for (int j = 0; j < filterResponseList.size(); j++) {
            FilterResponse filterResponse = filterResponseList.get(j);
            JSONObject object = array.getJSONObject(j);
            NewsComment newsComment = new NewsComment();
            if (filterResponse.isStatus()) {
                FilterRes filterRes = filterResponse.getFilterRes();
                if (filterRes.getIsPush()) {
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
                                newsComment = JSON.parseObject(object.toJSONString(), NewsComment.class);
                                newsComment.setmInsertTime(new Date(Long.parseLong(object.getString("m_insert_time")) * 1000));
                                newsComment.setmPublishTime(new Date(Long.parseLong(object.getString("m_publish_time")) * 1000));
                                newsComment.setmSnapshotTime(new Date(Long.parseLong(object.getString("m_snapshot_time")) * 1000));
                                logger.info("json转实体类耗时" + (System.currentTimeMillis() - wu));
                                newsComment.setHitKeyword(hitBlack);
                                newsComment.setTopicId(subjectId);
                                String content = object.getString("m_content");
                                if (content.length() < 150) {
                                    newsComment.setmAbstract(content);
                                } else {
                                    newsComment.setmAbstract(result.getHitAbstract());
                                }
                                logger.info("构建后数据为：：：" + newsComment);
                                newsComments.add(newsComment);
                            }
                        }
                    }
                    logger.info("遍历results耗时" + (System.currentTimeMillis() - si));
                }
            }
        }
        logger.info("第三块判定有害耗时" + (System.currentTimeMillis() - san));

        logger.info(array.getJSONObject(0).getString("g_ch_key") + "所在批次:::数据总耗时：：：" + (System.currentTimeMillis() - allStart));
        logger.info("线程封装newscomment:" + Arrays.asList(newsComments));
        return newsComments;
    }
}
class Thread1 implements Callable<Integer> {

    JSONArray array;
    Logger logger;

    public Thread1(JSONArray array) {
        super();
        this.array = array;
    }
    @Override
    public Integer call() throws Exception {
        System.out.println("<<<<<<<" + array.toString());

        return 1;
    }
}

