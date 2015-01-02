package com.elfec.ssc.model.events;

public interface IWSFinishEvent<TResult> {

	public void executeOnFinished(TResult result); 
}
