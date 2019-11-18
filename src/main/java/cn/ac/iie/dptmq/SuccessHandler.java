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
import java.util.concurrent.*;

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

           static Request<NewsComment> request = new Request<>();
           static int count = 0;
    @Override
    public boolean handle(REMessage message) {
        long handleStart = System.currentTimeMillis();
        logger.info("handle开始：：：获取数据：" + message.data.size() + "条");
        //总数据
        List<Object[]> messageList = message.data;
        List<Object[]> messageList1 = new ArrayList<>();
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

           /* //总条数
            int size = messageList.size();
            //获取结果中的一部分
            int dataNum =Integer.parseInt(CommonConstant.get("dataNum"));
            int num = size / dataNum;
            //以总条数为范围,随机生成一部分的条数索引,用于监测随机评论
            int[] messageIndex = getRandomArrayByIndex(num,size);
            Object[] objects = null;
            for (int i = 0; i < messageIndex.length; i++) {
                objects = messageList.get(i);
                messageList1.add(objects);
            }
            logger.info("处理的部分数据：" + messageList1.size() + "条");*/
//            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor();
//            ExecutorService pool = Executors.newFixedThreadPool(10);// 创建一个固定大小为5的线程池
            ExecutorService threadPool = ThreadPool.getThreadPool();
           for (Object[] data : message.data) {
               threadPool.execute(new SuccessHandlerThread(data,message,colNameIndex,logger,filterService,sdf));
            }

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

        } catch (Exception e) {
            logger.error("数据处理失败", e);
            return false;
        }

        logger.info(":::::::" + Thread.currentThread().getName() + "：：：：handle结束：：：耗时：：：" + (System.currentTimeMillis() - handleStart));
        return true;
    }

    public static int[] getRandomArrayByIndex(int num, int scope) {
        //1.获取scope范围内的所有数值，并存到数组中
        int[] randomArray = new int[scope];
        for (int i = 0; i < randomArray.length; i++) {
            randomArray[i] = i;
        }

        //2.从数组random中取数据，取过后的数改为-1
        int[] numArray = new int[num];//存储num个随机数
        int i = 0;
        while (i < numArray.length) {
            int index = (int) (Math.random() * scope);
            if (randomArray[index] != -1) {
                numArray[i] = randomArray[index];
                randomArray[index] = -1;
                i++;
            }
        }

        return numArray;
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

