package com.squad.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.sun.management.OperatingSystemMXBean;


public class CpuUtil {
	private static final OperatingSystemMXBean osBean = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
	private static RuntimeMXBean runBean = (RuntimeMXBean)ManagementFactory.getRuntimeMXBean();
	public static long prevProcessCputTime = 0L;
	public static long prevUptime = 0L;
	
	public static int getAvailableProcessors() {
		return osBean.getAvailableProcessors();
	}
	
	public static long getUpTime() {
		//return runtimeBean.getUptime();
		return osBean.getProcessCpuTime();
	}
	
	public static double getCpuLoad() {
		return osBean.getProcessCpuLoad();
	}
	public static double getSystemLoadAverage() {
		return osBean.getSystemLoadAverage();
	}
	
	public static double getCpuUsage() {
		long cpuTime = osBean.getProcessCpuTime();
		long upTime = runBean.getUptime();
		
		long elapsedCpuTime =  cpuTime - prevProcessCputTime;
		long elapsedTime = upTime - prevUptime;
		
		float cal = elapsedCpuTime / ( ( elapsedTime )  * 10000f * getAvailableProcessors() );
		float cpuUsage = Math.min( 99f,  cal );
		cpuUsage = Math.max( 0f, cpuUsage );
		
		prevProcessCputTime = cpuTime;
		prevUptime = upTime;
		
		return Math.round( cpuUsage * Math.pow( 10.0, 1 ) );
		//return cpuUsage;
	}
	
}
