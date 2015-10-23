package com.elfec.ssc.model.events;

import com.elfec.ssc.model.webservices.WSResponse;

public interface IWSFinishEvent<TResult> {

	public void executeOnFinished(WSResponse<TResult> result); 
}
