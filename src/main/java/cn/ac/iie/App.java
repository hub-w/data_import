package cn.ac.iie;

import cn.ac.iie.di.datadock.rdata.exchange.client.exception.REConnectionException;
import cn.ac.iie.dptmq.BaseRecevier;
import cn.ac.iie.utils.CommonConstant;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;

/**
 * Hello world!
 */
public class App {
    static {
        DOMConfigurator.configure(System.getProperty("user.dir") + File.separator + "conf" + File.separator
                + "log4j.xml");
    }
//    private static Logger logger = Logger.getLogger(App.class);
    public static void main(String[] args) throws REConnectionException {
        BaseRecevier recevier = new BaseRecevier();
//        recevier.receiver(CommonConstant.get("dpt_mq_group"), CommonConstant.get("dpt_mq_topic"), new SuccessHandler(), new FailHandler());
        recevier.receiver(CommonConstant.get("dpt_mq_group"), CommonConstant.get("dpt_mq_topic"));
    }
}
