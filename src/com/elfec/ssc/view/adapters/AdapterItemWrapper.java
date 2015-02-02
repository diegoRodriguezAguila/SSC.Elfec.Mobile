package com.elfec.ssc.view.adapters;

public class AdapterItemWrapper<T> {
	private T wrappedObject;
	private boolean isExpanded;
	private int expandedSize;
	
	public AdapterItemWrapper(T wrappedObject) {
		super();
		this.wrappedObject = wrappedObject;
		this.isExpanded = false;
	}
	
	public T getWrappedObject() {
		return wrappedObject;
	}
	public void setWrappedObject(T wrappedObject) {
		this.wrappedObject = wrappedObject;
	}
	public boolean isExpanded() {
		return isExpanded;
	}
	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}

	public int getExpandedSize() {
		return expandedSize;
	}

	public void setExpandedSize(int expandedSize) {
		this.expandedSize = expandedSize;
	}

}
