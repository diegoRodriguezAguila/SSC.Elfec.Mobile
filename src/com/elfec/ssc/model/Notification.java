package com.elfec.ssc.model;

import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Notifications")
public class Notification extends Model {
	@Column(name = "Title")
    private String Title;
	@Column(name = "Content")
    private String Content;
	@Column(name = "Type", notNull = true)
    private Short Type;
	@Column(name = "InsertDate", notNull=true)
	private DateTime InsertDate;    
	@Column(name = "UpdateDate")
	private DateTime UpdateDate;
	public Notification()
	{
		super();
	}
	public Notification(String Title,String Content,short Type)
	{
		super();
		this.Title=Title;
		this.Content=Content;
		this.Type=Type;
	}
	//#region getters and setters
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public Short getType() {
		return Type;
	}
	public void setType(Short type) {
		Type = type;
	}
	public DateTime getInsertDate() {
		return InsertDate;
	}
	public void setInsertDate(DateTime insertDate) {
		InsertDate = insertDate;
	}
	public DateTime getUpdateDate() {
		return UpdateDate;
	}
	public void setUpdateDate(DateTime updateDate) {
		UpdateDate = updateDate;
	}
	//#endregion
	public static List<Notification> getAccountNotifications()
	{
		return  new Select()
        .from(Notification.class).as("n").where("n.Type=1")
        .execute();
	}
	public static List<Notification> getCutNotifications()
	{
		return  new Select()
        .from(Notification.class).as("n").where("n.Type=0")
        .execute();
	}
}
