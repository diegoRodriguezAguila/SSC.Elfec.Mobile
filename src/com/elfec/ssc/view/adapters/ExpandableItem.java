package com.elfec.ssc.view.adapters;

/**
 * Representa un item expandible
 * 
 * @author drodriguez
 *
 */
public class ExpandableItem {
	private boolean isExpanded;
	private int expandedSize;

	public ExpandableItem() {
		this(false);
	}

	public ExpandableItem(boolean isExpanded) {
		this.isExpanded = isExpanded;
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
