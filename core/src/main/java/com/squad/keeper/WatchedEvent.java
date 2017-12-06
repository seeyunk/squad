package com.squad.keeper;

import java.io.Serializable;

public class WatchedEvent implements Serializable {

	private static final long serialVersionUID = -2558699420378883846L;
	private EventType eventType;
	private String node;
	private byte[] value;
	
	public WatchedEvent( EventType eventType, String node, byte[] value ) {
		this.eventType = eventType;
		this.node = node;
		this.value = value;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}
}
