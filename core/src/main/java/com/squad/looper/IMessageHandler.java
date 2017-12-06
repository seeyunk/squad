package com.squad.looper;

import com.squad.entity.Message;

public interface IMessageHandler {
	public boolean handleMessage( Message message );
}
