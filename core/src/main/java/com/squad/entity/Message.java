package com.squad.entity;

import java.io.Serializable;

import com.squad.looper.AsyncLooper.MSG;

public class Message implements Serializable {
	private static final long serialVersionUID = 5886030280119432065L;
	private final MSG msg;
	private final Object[] params;
	
	public Message( MSG msg, Object... params ) {
		this.msg = msg;
		this.params = params;
	}
	
	public MSG getMessage() {
		return msg;
	}

	public Object[] getParams() {
		return params;
	}
	
	public boolean equals( MSG msg ) {
		return this.msg.equals( msg );
	}
}
