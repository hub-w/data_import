package cn.ac.iie.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @ClassName Process
 * @Author tjh
 * @Date 19/4/4 上午9:15
 * @Version 1.0
 **/
public class Process {
    public static final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println(runtimeMXBean.getName());
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
    }

    public static void main(String[] args) {
        System.out.println(Process.getProcessID());
    }
}
