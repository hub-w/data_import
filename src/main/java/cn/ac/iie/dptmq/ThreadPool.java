package cn.ac.iie.dptmq;

import cn.ac.iie.utils.CommonConstant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author Geg
 * @Date 2019/11/18  15:43
 *
 * 构造一个任务池
 * 参数说明：
 * corePoolSize - 池中所保存的线程数，包括空闲线程。
 * maximumPoolSize - 池中允许的最大线程数。
 * keepAliveTime - 当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间。
 * unit - keepAliveTime 参数的时间单位。
 * workQueue - 执行前用于保持任务的队列。此队列仅保持由 execute 方法提交的 Runnable 任务。
 * threadFactory - 执行程序创建新线程时使用的工厂。
 * handler - 由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序
 **/
public class ThreadPool {
    private static ExecutorService sender = null;

    public synchronized static ExecutorService getThreadPool() {
        if (sender == null) {
            try {
                sender = Executors.newFixedThreadPool(Integer.parseInt(CommonConstant.get("poolNum")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sender;
    }
}
