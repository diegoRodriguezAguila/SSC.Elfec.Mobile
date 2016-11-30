package com.elfec.ssc.model.events;

import com.elfec.ssc.model.security.SscToken;
import com.elfec.ssc.model.webservices.WSResponse;

public interface SscTokenReceivedCallback {
	void onSscTokenReceived(WSResponse<SscToken> wsTokenResult);
}
