package com.squad.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadUtil {
	private static final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
	
	public static int getThreadCount() {
		return threadBean.getThreadCount();
	}
	
	public static int getPeakThreadCount() {
		return threadBean.getPeakThreadCount();
	}
	
}
