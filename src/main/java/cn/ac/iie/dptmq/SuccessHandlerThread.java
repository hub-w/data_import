//package cn.ac.iie.dptmq;
//
//import cn.ac.iie.di.datadock.rdata.exchange.client.v1.REMessage;
//import cn.ac.iie.domain.NewsComment;
//import cn.ac.iie.dto.requests.FilterRequest;
//import cn.ac.iie.dto.response.FilterRes;
//import cn.ac.iie.dto.response.FilterResponse;
//import cn.ac.iie.dto.response.Result;
//import cn.ac.iie.feign.FilterService;
//import cn.ac.iie.utils.CommonConstant;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * @Author Geg
// * @Date 2019/11/17  17:53
// **/
//public class SuccessHandlerThread implements Runnable {
//    public Object[] data;
//
//    public REMessage message;
//
//    public Map<String, Integer> colNameIndex;
//
//    public Logger logger;
//
//    public SimpleDateFormat sdf;
//
//    public FilterService filterService;
//
//    public SuccessHandlerThread(Object[] data1, REMessage message1,Map<String, Integer> colNameIndex1,Logger logger1,FilterService filterService1,SimpleDateFormat sdf1) {
//        this.data = data1;
//        this.message = message1;
//        this.colNameIndex=colNameIndex1;
//        this.logger=logger1;
//        this.filterService=filterService1;
//        this.sdf=sdf1;
//    }
//
//    @Override
//    public void run() {
//        //logger.info("data::::::::::::"+data);
//        logger.info("线程:" + Thread.currentThread().getName()+"数据:"+data);
//        long allStart = System.currentTimeMillis();
//        synchronized (SuccessHandler.class) {
//            SuccessHandler.count++;
//        }
////               if (count % Integer.parseInt(CommonConstant.get("deal_num")) == 0) {
//
//        // 读取数据
//        String[] colNames = message.colNames;
//        logger.info(Arrays.deepToString(colNames));
//        Map<String, Object> propertiesMap = new HashMap<>();
//        for (String colName : colNames) {
//            String col = CommonConstant.get(colName);
//            if (col != null) {
//                Object value = null;
//                if (colName.endsWith("time")) {
//                    value = new Date((Long) data[colNameIndex.get(colName)] * 1000);
//                } else {
//                    value = data[colNameIndex.get(colName)];
//                }
//                if (colName.equals("m_risk_label")) {
//                    value = null;
//                }
//                propertiesMap.put(col, value);
//            }
//        }
//
//        //logger.info("数据：：："+propertiesMap);
//        // ==============================================================================
//
//        NewsComment temp = new NewsComment();
//        try {
//            BeanUtils.populate(temp, propertiesMap);
//
//
//            logger.info("格式" + temp);
//
//            //下载
//                    /*Integer xiazaicount=0;
//                    String[] videoFiles= temp.getmVideoFiles();
//                    String url = "http://10.136.134.35:20099/get?key="+videoFiles[0];
//                    logger.info("下载地址"+url);
//                    String tmpDir = "/home/iie/videodownload/" + "video/"  + "douyin/";
//                    File folder = new File(tmpDir);
//                    if (!folder.exists()) {
//                        folder.mkdirs();
//                    }
//                    String filePath = tmpDir + File.separator +url.substring(url.lastIndexOf("@") + 1);
//                    File f = new File(filePath);
//                    try {
//                        FileUtils.downloadFile(url, f);
//                        logger.info("下载成功");
//                    } catch (Exception e) {
//                        logger.info(e.toString());
//                        logger.info("下载失败");
//                    }*/
//
//            //request.addData(temp);//不过引擎直接入库
//
//            String contentId = (String) propertiesMap.get("gChKey");
//            String content = (String) propertiesMap.get("mContent");
//            if (StringUtils.isBlank(content)) {
//                content = (String) propertiesMap.get("mTitle");
//            }
//
//            // 1 filter hitblack
//            FilterRequest filterRequest = new FilterRequest();
//            // 构建过滤请求参数
//            filterRequest.setSysCode(CommonConstant.get("filter_sys_code"));
//            filterRequest.setId(contentId);
//            filterRequest.setText(content);
//            long filterStart = System.currentTimeMillis();
//            logger.info("过滤请求参数：：：" + new ObjectMapper().writeValueAsString(filterRequest));
//            FilterResponse filterResponse = filterService.filter(filterRequest);
//            logger.info("过滤总耗时:"+(System.currentTimeMillis()-filterStart));
//            String filterResponseJson = new ObjectMapper().writeValueAsString(filterResponse);
//
//            logger.info("过滤返回：：：" + filterResponseJson);
//            if (filterResponse.isStatus()) {
//                FilterRes filterRes = filterResponse.getFilterRes();
//                if (filterRes.getIsPush()) {
//                    // 2、综合引擎
////                            SynthesizeRequest synthesizeRequest = new SynthesizeRequest();
////                            synthesizeRequest.setuChId(temp.getuChId());
////                            synthesizeRequest.setmChId(temp.getmChId());
////                            synthesizeRequest.setuName(temp.getuName());
////                            synthesizeRequest.setmContent(content);
////                            synthesizeRequest.setmPublishTime(sdf.format(temp.getmPublishTime()));
////                            synthesizeRequest.setgAsp(temp.getgAsp());
//                    JSONObject synthesizeRequest = new JSONObject();
//                    JSONArray datas = new JSONArray();
//                    JSONObject json = new JSONObject();
//                    json.put("m_content", temp.getmContent());
//                    json.put("m_publish_time", sdf.format(temp.getmPublishTime()));
//                    json.put("u_name", temp.getuName());
//                    json.put("u_ch_id", temp.getuChId());
//                    json.put("m_ch_id", temp.getmChId());
//                    json.put("g_asp", temp.getgAsp());
//                    datas.add(json);
//                    synthesizeRequest.put("data", datas);
////                            logger.info("综合引擎请求参数:::"+synthesizeRequest);
////                            //SynthesizeResponse synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
////                            JSONObject synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
////                            logger.info("综合引擎返回：：："+synthesizeResponse);
////                            JSONArray redatas = synthesizeResponse.getJSONArray("data");
////                            JSONObject  redata = redatas.getJSONObject(0);
////                            String suggest = redata.getString("suggest");
////
////                            if("Harm".equalsIgnoreCase(suggest)) {
//                    //if(true) {
//                    logger.info("数据判定有害");
//
//                    // 构建数据
//                    List<Result> results = filterRes.getDescs();
//                    logger.info("results:" + results);
//                    int a = 0;
//                    List<String> maxSubjectIds = new ArrayList<>();
//                    for (int i = 0; i < results.size(); i++) {
//                        Result result = results.get(i);
//                        String hitBlack = result.getHitBlack();
//                        List<String> subjectIds = result.getSubjectIds();
//                        int b = subjectIds.size();
//                        if (b > a) {
//                            a = b;
//                            maxSubjectIds = subjectIds;
//                        }
//                        if (i == (results.size() - 1)) {
//                            for (String subjectId : maxSubjectIds) {
//                                //只存第一个关键词和
//                                //String subjectId=subjectIds.get(0);
//                                NewsComment newsComment = new NewsComment();
//                                BeanUtils.populate(newsComment, propertiesMap);
//                                newsComment.setHitKeyword(hitBlack);
//                                newsComment.setTopicId(subjectId);
//
//                                if (content.length() < 150) {
//                                    newsComment.setmAbstract(content);
//                                } else {
//                                    newsComment.setmAbstract(result.getHitAbstract());
//                                }
//
//                                logger.info("构建后数据为：：：" + newsComment);
//                                synchronized (SuccessHandler.request){
//                                    SuccessHandler.request.addData(newsComment);
//                                }
//                            }
//                        }
//                    }
////                            }
//                }
//            }
////                } else {
////                    logger.info("丢弃数据" + data[colNameIndex.get("g_ch_key")]);
////                }
//
//            logger.info(data[colNameIndex.get("g_ch_key")] + ":::数据总耗时：：：" + (System.currentTimeMillis() - allStart));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
//    }
//
//}
//
//
//
///*public class SuccessHandlerThread extends Thread {
//    public Object[] data;
//
//    public REMessage message;
//
//    public Map<String, Integer> colNameIndex;
//
//    public Logger logger;
//
//    public SimpleDateFormat sdf;
//
//    public FilterService filterService;
//
//    public SuccessHandlerThread(Object[] data1, REMessage message1,Map<String, Integer> colNameIndex1,Logger logger1,FilterService filterService1,SimpleDateFormat sdf1) {
//           this.data = data1;
//           this.message = message1;
//           this.colNameIndex=colNameIndex1;
//           this.logger=logger1;
//           this.filterService=filterService1;
//           this.sdf=sdf1;
//    }
//
//    @Override
//    public void run() {
//        //logger.info("data::::::::::::"+data);
//        logger.info("线程:" + Thread.currentThread().getName()+"数据:"+data);
//        long allStart = System.currentTimeMillis();
//        synchronized (SuccessHandler.class) {
//            SuccessHandler.count++;
//        }
////               if (count % Integer.parseInt(CommonConstant.get("deal_num")) == 0) {
//
//        // 读取数据
//        String[] colNames = message.colNames;
//        logger.info(Arrays.deepToString(colNames));
//        Map<String, Object> propertiesMap = new HashMap<>();
//        for (String colName : colNames) {
//            String col = CommonConstant.get(colName);
//            if (col != null) {
//                Object value = null;
//                if (colName.endsWith("time")) {
//                    value = new Date((Long) data[colNameIndex.get(colName)] * 1000);
//                } else {
//                    value = data[colNameIndex.get(colName)];
//                }
//                if (colName.equals("m_risk_label")) {
//                    value = null;
//                }
//                propertiesMap.put(col, value);
//            }
//        }
//
//        //logger.info("数据：：："+propertiesMap);
//        // ==============================================================================
//
//        NewsComment temp = new NewsComment();
//        try {
//            BeanUtils.populate(temp, propertiesMap);
//
//
//        logger.info("格式" + temp);
//
//        //下载
//                    *//*Integer xiazaicount=0;
//                    String[] videoFiles= temp.getmVideoFiles();
//                    String url = "http://10.136.134.35:20099/get?key="+videoFiles[0];
//                    logger.info("下载地址"+url);
//                    String tmpDir = "/home/iie/videodownload/" + "video/"  + "douyin/";
//                    File folder = new File(tmpDir);
//                    if (!folder.exists()) {
//                        folder.mkdirs();
//                    }
//                    String filePath = tmpDir + File.separator +url.substring(url.lastIndexOf("@") + 1);
//                    File f = new File(filePath);
//                    try {
//                        FileUtils.downloadFile(url, f);
//                        logger.info("下载成功");
//                    } catch (Exception e) {
//                        logger.info(e.toString());
//                        logger.info("下载失败");
//                    }*//*
//
//        //request.addData(temp);//不过引擎直接入库
//
//        String contentId = (String) propertiesMap.get("gChKey");
//        String content = (String) propertiesMap.get("mContent");
//        if (StringUtils.isBlank(content)) {
//            content = (String) propertiesMap.get("mTitle");
//        }
//
//        // 1 filter hitblack
//        FilterRequest filterRequest = new FilterRequest();
//        // 构建过滤请求参数
//        filterRequest.setSysCode(CommonConstant.get("filter_sys_code"));
//        filterRequest.setId(contentId);
//        filterRequest.setText(content);
//        long filterStart = System.currentTimeMillis();
//        logger.info("过滤请求参数：：：" + new ObjectMapper().writeValueAsString(filterRequest));
//        FilterResponse filterResponse = filterService.filter(filterRequest);
//        logger.info("过滤总耗时:"+(System.currentTimeMillis()-filterStart));
//        String filterResponseJson = new ObjectMapper().writeValueAsString(filterResponse);
//
//        logger.info("过滤返回：：：" + filterResponseJson);
//        if (filterResponse.isStatus()) {
//            FilterRes filterRes = filterResponse.getFilterRes();
//            if (filterRes.getIsPush()) {
//                // 2、综合引擎
////                            SynthesizeRequest synthesizeRequest = new SynthesizeRequest();
////                            synthesizeRequest.setuChId(temp.getuChId());
////                            synthesizeRequest.setmChId(temp.getmChId());
////                            synthesizeRequest.setuName(temp.getuName());
////                            synthesizeRequest.setmContent(content);
////                            synthesizeRequest.setmPublishTime(sdf.format(temp.getmPublishTime()));
////                            synthesizeRequest.setgAsp(temp.getgAsp());
//                JSONObject synthesizeRequest = new JSONObject();
//                JSONArray datas = new JSONArray();
//                JSONObject json = new JSONObject();
//                json.put("m_content", temp.getmContent());
//                json.put("m_publish_time", sdf.format(temp.getmPublishTime()));
//                json.put("u_name", temp.getuName());
//                json.put("u_ch_id", temp.getuChId());
//                json.put("m_ch_id", temp.getmChId());
//                json.put("g_asp", temp.getgAsp());
//                datas.add(json);
//                synthesizeRequest.put("data", datas);
////                            logger.info("综合引擎请求参数:::"+synthesizeRequest);
////                            //SynthesizeResponse synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
////                            JSONObject synthesizeResponse = synthesizeService.synthesize(synthesizeRequest);
////                            logger.info("综合引擎返回：：："+synthesizeResponse);
////                            JSONArray redatas = synthesizeResponse.getJSONArray("data");
////                            JSONObject  redata = redatas.getJSONObject(0);
////                            String suggest = redata.getString("suggest");
////
////                            if("Harm".equalsIgnoreCase(suggest)) {
//                //if(true) {
//                logger.info("数据判定有害");
//
//                // 构建数据
//                List<Result> results = filterRes.getDescs();
//                logger.info("results:" + results);
//                int a = 0;
//                List<String> maxSubjectIds = new ArrayList<>();
//                for (int i = 0; i < results.size(); i++) {
//                    Result result = results.get(i);
//                    String hitBlack = result.getHitBlack();
//                    List<String> subjectIds = result.getSubjectIds();
//                    int b = subjectIds.size();
//                    if (b > a) {
//                        a = b;
//                        maxSubjectIds = subjectIds;
//                    }
//                    if (i == (results.size() - 1)) {
//                        for (String subjectId : maxSubjectIds) {
//                            //只存第一个关键词和
//                            //String subjectId=subjectIds.get(0);
//                            NewsComment newsComment = new NewsComment();
//                            BeanUtils.populate(newsComment, propertiesMap);
//                            newsComment.setHitKeyword(hitBlack);
//                            newsComment.setTopicId(subjectId);
//
//                            if (content.length() < 150) {
//                                newsComment.setmAbstract(content);
//                            } else {
//                                newsComment.setmAbstract(result.getHitAbstract());
//                            }
//
//                            logger.info("构建后数据为：：：" + newsComment);
//                            synchronized (SuccessHandler.request){
//                            SuccessHandler.request.addData(newsComment);
//                            }
//                        }
//                    }
//                }
////                            }
//            }
//        }
////                } else {
////                    logger.info("丢弃数据" + data[colNameIndex.get("g_ch_key")]);
////                }
//
//        logger.info(data[colNameIndex.get("g_ch_key")] + ":::数据总耗时：：：" + (System.currentTimeMillis() - allStart));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
//    }
//}*/
