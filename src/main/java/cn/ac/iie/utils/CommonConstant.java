package cn.ac.iie.utils;


import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommonConstant {
    private static final Logger logger = Logger.getLogger(CommonConstant.class);

    private static Properties prop;
    public static final Map<String, String> DATA_MAP = new HashMap<String, String>();

    public final static String test = "/history/add";

    static {
        prop = new Properties();
        try {
            logger.info("CommonConstant:开始加载配置文件");
//			prop.load(CommonConstant.class.getClassLoader().getResourceAsStream("common.properties"));
            InputStream in = new FileInputStream("conf/common.properties");
            prop.load(in);
            logger.info("CommonConstant:配置文件加载完毕");
        } catch (IOException e) {
            logger.error("CommonConstant:配置文件加载失败");
            e.printStackTrace();
        }

        setDataMap();

    }

    private static void setDataMap() {
        DATA_MAP.put("g_ch_key", "g_ch_key");
        DATA_MAP.put("g_id", "g_id");
        DATA_MAP.put("m_publish_time", "m_publish_time");
        DATA_MAP.put("m_insert_time", "m_insert_time");
        DATA_MAP.put("m_ch_id", "m_ch_id");
        DATA_MAP.put("m_url", "m_url");
        DATA_MAP.put("m_type", "m_type");
        DATA_MAP.put("m_content", "m_content");
        DATA_MAP.put("m_language", "m_language");
        DATA_MAP.put("mo_status", "mo_status");
        DATA_MAP.put("mo_time", "mo_time");
        DATA_MAP.put("u_g_ch_key", "u_g_ch_key");
        DATA_MAP.put("u_ch_id", "u_ch_id");
        DATA_MAP.put("u_name", "u_name");
        DATA_MAP.put("u_send_ip", "u_send_ip");
        DATA_MAP.put("u_friends_cnt", "u_friends_cnt");
        DATA_MAP.put("u_fans_cnt", "u_fans_cnt");
        DATA_MAP.put("m_forward_cnt", "m_forward_cnt");
        DATA_MAP.put("m_reply_cnt", "m_reply_cnt");
        DATA_MAP.put("m_root_g_ch_key", "m_root_g_ch_key");
        DATA_MAP.put("m_root_ch_id", "m_root_ch_id");
        DATA_MAP.put("m_root_u_g_ch_key", "m_root_u_g_ch_key");
        DATA_MAP.put("m_root_reply_cnt", "m_root_reply_cnt");
        DATA_MAP.put("m_root_forward_cnt", "m_root_forward_cnt");
        DATA_MAP.put("ma_picture_cnt", "ma_picture_cnt");
        DATA_MAP.put("ma_picture_urls", "ma_picture_urls");
        DATA_MAP.put("ma_picture_files", "ma_picture_files");
        DATA_MAP.put("u_loc_county", "u_loc_county");
        DATA_MAP.put("u_loc_province", "u_loc_province");
    }

    private CommonConstant() {
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(CommonConstant.get("LTTB.subject"));
    }
}
