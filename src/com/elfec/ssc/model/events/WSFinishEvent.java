package com.elfec.ssc.model.events;

public interface WSFinishEvent<TResult> {

	public void executeOnFinished(TResult result); 
}
