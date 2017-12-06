package com.squad.looper;

import com.squad.entity.Message;

public interface IMessageNotifier {
	public void sendMessage( Message message ) throws Exception;
}
