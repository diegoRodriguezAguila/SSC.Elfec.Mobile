package com.elfec.ssc.model.events;

import com.elfec.ssc.model.security.WSToken;
import com.elfec.ssc.model.webservices.WSResponse;

public interface WSTokenReceivedCallback {
	public void onWSTokenReceived(WSResponse<WSToken> wsTokenResult);
}
