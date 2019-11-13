package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.core.session.receive.REAbstractReceiveMessageHandler;
import cn.ac.iie.di.datadock.rdata.exchange.client.exception.REConnectionException;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.ConsumePosition;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.FailureMessage;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.REMessage;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.connecotor.RERocketmqReceiver;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.connection.REConnection;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.session.REReceiveSession;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.session.REReceiveSessionBuilder;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.session.RESendSessionBuilder;
import cn.ac.iie.utils.CommonConstant;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BaseRecevier {
	private static final Logger logger = Logger.getLogger(BaseRecevier.class);
	public void receiver(String group, String topic, REAbstractReceiveMessageHandler<REMessage> success, REAbstractReceiveMessageHandler<FailureMessage> fail) throws REConnectionException {
		//RERocketmqReceiver.callBackThreadNum = Integer.parseInt(CommonConstant.get("callBackNum"));
		//生成连接，参数为rocketmq的nameserver串。可以多个nameserver，分号隔开。由具体业务决定
		REConnection conn = new REConnection(CommonConstant.get("dpt_mq_address"));
		//生成builder，参数为topic，由具体业务决定
		REReceiveSessionBuilder builder = (REReceiveSessionBuilder) conn.getReceiveSessionBuilder(topic);
		//设置receiver 的两个处理函数：
		builder.setHandler(success);	//成功
		builder.setFailureHandler(fail);//失败
		//设置组名。相同的组名处理的是同一份数据，不同的组名处理的是不同的数据备份。
		builder.setGroupName(group);
		//设置从处理起始位置。如果设置的是CONSUME_FROM_TIMESTAMP，需要同时设置起始时间戳

		String mqType = CommonConstant.get("mq_type");
		switch (mqType){
			case "timestamp":
				builder.setConsumPosition(ConsumePosition.CONSUME_FROM_TIMESTAMP);
				String timeStr = CommonConstant.get("timeStr");
				System.out.println(timeStr);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


				try {
					builder.setConsumeTimestamp(format.parse(timeStr).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case "last":
				builder.setConsumPosition(ConsumePosition.CONSUME_FROM_LAST_OFFSET);

				break;
			case "first":
				builder.setConsumPosition(ConsumePosition.CONSUME_FROM_FIRST_OFFSET);
				break;
		}

		//设置消费线程数
		builder.setConsumeThreadNum(Integer.parseInt(CommonConstant.get("dpt_mq_thread_num")));
		//生成session
		REReceiveSession session = (REReceiveSession) builder.build();
		//session开始，自动接收数据，并回调处理函数。非阻塞
		session.start();
		logger.info("session开始，自动接收数据");
	}
}
