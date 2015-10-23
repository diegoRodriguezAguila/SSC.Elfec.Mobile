package com.elfec.ssc.view.adapters;

public class AdapterItemWrapper<T> {
	private T wrappedObject;
	private boolean isExpanded;
	private int expandedSize;
	private int imageResourceId;
	
	public AdapterItemWrapper(T wrappedObject, int imageResourceId) {
		super();
		this.wrappedObject = wrappedObject;
		this.isExpanded = false;
		this.imageResourceId = imageResourceId;
	}
	
	public AdapterItemWrapper(T wrappedObject, boolean isExpanded, int imageResourceId) {
		super();
		this.wrappedObject = wrappedObject;
		this.isExpanded = isExpanded;
		this.imageResourceId = imageResourceId;
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

	public int getImageResourceId() {
		return imageResourceId;
	}

	public void setImageResourceId(int imageResourceId) {
		this.imageResourceId = imageResourceId;
	}

}
