package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.connector.impl.RERmqConnector;
import cn.ac.iie.di.datadock.rdata.exchange.client.connector.impl.RERmqReceiverBuilder;
import cn.ac.iie.di.datadock.rdata.exchange.client.exception.REConnectionException;
import cn.ac.iie.utils.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.BiConsumer;

@Slf4j
public class BaseRecevier {
//	private static final Logger logger = Logger.getLogger(BaseRecevier.class);
	public void receiver(String group, String topic) throws REConnectionException {
		String address = CommonConstant.get("dpt_mq_address");

		RERmqReceiverBuilder builder = new RERmqConnector().receiverBuilder(address);
		builder.setGroup(group)
//				.setInstanceName("asef")
				.setConsumeThreadNum(Integer.parseInt(CommonConstant.get("dpt_mq_thread_num")));
//				.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

//        builder.registerMessageListener(topic, SuccessHandler.handler, failureHandler);
		SuccessHandler successHandler  = new SuccessHandler();

		builder.registerMessageListener(topic, successHandler.handler, failureHandler);


		String mqType = CommonConstant.get("mq_type");
		switch (mqType){
			case "timestamp":
				builder.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
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
				builder.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

				break;
			case "first":
				builder.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
				break;
		}

		builder.build().start();
		log.info("开始了");
	}

	private static BiConsumer<MessageExt, Exception> failureHandler = (msg, ex) -> {
		StringBuilder sb = new StringBuilder();
		sb.append("*** *** errorpac *** ***\n");
		sb.append("\ttopic:").append(msg.getTopic());

		sb.append("\ttags:").append(msg.getTags());
		sb.append("\tmsgId:").append(msg.getMsgId()).append("\n");
		System.out.println(sb.toString());
		ex.printStackTrace();
	};
}
