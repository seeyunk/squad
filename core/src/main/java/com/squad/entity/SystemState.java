package com.squad.entity;

import java.io.Serializable;

import com.squad.protobuf.packet.Squad.SystemStatePacket;

public class SystemState implements Serializable {
	private static final long serialVersionUID = -8161494825320443056L;

	//cpu info.
	private int availableProcessor;
	private double cpuLoad;
	private double systemLoad;
	private long upTime;
	
	//memory info.
	private long maxHeap;
	private long totalHeap;
	private long usedHeap;
	private long usedPerm;
	private long maxPerm;
	private long committedPerm;
	
	public SystemState() {
		this( 0, 0.0f, 0.0f, 0L, 0L, 0L, 0L, 0L, 0L, 0L );
	}
	
	public SystemState( int availableProcessor, double cpuLoad, double systemLoad, long upTime,
					long maxHeap, long totalHeap, long usedHeap, long usedPerm, long maxPerm, long committedPerm ) {
		this.availableProcessor = availableProcessor;
		this.cpuLoad = cpuLoad;
		this.systemLoad = systemLoad;
		this.upTime = upTime;
		this.maxHeap = maxHeap;
		this.totalHeap = totalHeap;
		this.usedHeap = usedHeap;
		this.usedPerm = usedPerm;
		this.maxPerm = maxPerm;
		this.committedPerm = committedPerm;	
	}
	
	public int getAvailableProcessor() {
		return availableProcessor;
	}
	public void setAvailableProcessor(int availableProcessor) {
		this.availableProcessor = availableProcessor;
	}
	public double getCpuLoad() {
		return cpuLoad;
	}
	public void setCpuLoad(double cpuLoad) {
		this.cpuLoad = cpuLoad;
	}
	public double getSystemLoad() {
		return systemLoad;
	}
	public void setSystemLoad(double systemLoad) {
		this.systemLoad = systemLoad;
	}
	public long getUpTime() {
		return upTime;
	}
	public void setUpTime(long upTime) {
		this.upTime = upTime;
	}
	public long getTotalHeap() {
		return totalHeap;
	}
	public void setTotalHeap(long totalHeap) {
		this.totalHeap = totalHeap;
	}
	public long getUsedHeap() {
		return usedHeap;
	}
	public void setUsedHeap(long usedHeap) {
		this.usedHeap = usedHeap;
	}
	public long getUsedPerm() {
		return usedPerm;
	}
	public void setUsedPerm(long usedPerm) {
		this.usedPerm = usedPerm;
	}
	public long getMaxPerm() {
		return maxPerm;
	}
	public void setMaxPerm(long maxPerm) {
		this.maxPerm = maxPerm;
	}
	public long getCommittedPerm() {
		return committedPerm;
	}
	public void setCommittedPerm(long committedPerm) {
		this.committedPerm = committedPerm;
	}
	public long getMaxHeap() {
		return maxHeap;
	}

	public void setMaxHeap(long maxHeap) {
		this.maxHeap = maxHeap;
	}
	
	public static SystemStatePacket toPacket( String scoutId, SystemState systemState ) {
		SystemStatePacket packet = SystemStatePacket.newBuilder()
									.setScoutId( scoutId )
									.setAvailableProcessor( systemState.getAvailableProcessor() )
									.setCpuLoad( systemState.getCpuLoad() )
									.setSystemLoad( systemState.getSystemLoad() )
									.setUpTime( systemState.getUpTime() )
									.setMaxHeap( systemState.getMaxHeap() )
									.setTotalHeap( systemState.getTotalHeap() )
									.setUsedHeap( systemState.getUsedHeap() )
									.setUsedPerm( systemState.getUsedPerm() )
									.setMaxPerm( systemState.getMaxPerm() )
									.setCommittedPerm( systemState.getCommittedPerm() )
									.build();
		return packet;
	}
	
	public static SystemState readPacket( SystemStatePacket packet ) {
		return new SystemState( packet.getAvailableProcessor(), packet.getCpuLoad(), 
							packet.getSystemLoad(), packet.getUpTime(),
							packet.getMaxHeap(), packet.getTotalHeap(), 
							packet.getUsedHeap(), packet.getUsedPerm(), 
							packet.getMaxPerm(), packet.getCommittedPerm() );
	}

	
}
