package com.squad.context;

import com.squad.manager.SquadNodeManager;
import com.squad.manager.SquadServiceManager;
import com.squad.rpc.client.ScoutClient;

public interface ISquadContext extends IConfigurable {
	public SquadServiceManager getServiceManager();
	public SquadNodeManager getNodeManager();
	public Config getConfig();
	public ScoutClient getClient();
}
