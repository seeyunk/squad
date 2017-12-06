package com.squad.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MemoryUtil {
	private static final Logger logger = LoggerFactory.getLogger( MemoryUtil.class );
	private static final Runtime runtime = Runtime.getRuntime();
	private static MemoryPoolMXBean permGen;
	private static MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
	private static MemoryUsage mu;
	static {
		mu = memBean.getHeapMemoryUsage();
		final List<MemoryPoolMXBean> memBeans = ManagementFactory.getMemoryPoolMXBeans();
		for ( MemoryPoolMXBean memBean : memBeans ) {
			String name = memBean.getName().toLowerCase();
			if ( name.indexOf( "perm gen" ) >= 0 ) {
				permGen = memBean;
				break;
			}
		}
		
	}
	
	public static long getTotalMemory() {
		return runtime.totalMemory();
	}
	
	public static long getUsedMemory() {
		//return runtime.totalMemory() - runtime.freeMemory();
		return mu.getUsed();
	}
	
	public static long getMaxMemory() {
		//return runtime.maxMemory();
		return mu.getMax();
	}
	
	public static long getPermMax() {
		return permGen.getUsage().getMax();
	}
	
	public static long getPermUsed() {
		return permGen.getUsage().getUsed();
	}
	
	public static long getPermCommitted() {
		return permGen.getUsage().getCommitted();
	}
}
