/*
package cn.ac.iie.dptmq;

import cn.ac.iie.di.datadock.rdata.exchange.client.core.session.receive.REAbstractReceiveMessageHandler;
import cn.ac.iie.di.datadock.rdata.exchange.client.v1.FailureMessage;
import org.apache.log4j.Logger;

public class FailHandler extends REAbstractReceiveMessageHandler<FailureMessage> {
	private static final Logger logger = Logger.getLogger(FailHandler.class);
	@Override
	public boolean handle(FailureMessage message) {
		// TODO Auto-generated method stub
		logger.error("数据接收错误：");
		message.getEx().printStackTrace();
		return true;
	}
}
*/
