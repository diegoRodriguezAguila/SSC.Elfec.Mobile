package com.elfec.ssc.model;

import java.util.List;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;

@Table(name = "Notifications")
public class Notification extends Model {
	@Column(name = "Key")
	private short Key;
	@Column(name = "Title")
    private String Title;
	@Column(name = "Content")
    private String Content;
	@Column(name = "Type", notNull = true)
    private short Type;
	@Column(name = "InsertDate", notNull=true)
	private DateTime InsertDate;    
	@Column(name = "UpdateDate")
	private DateTime UpdateDate;
	public Notification()
	{
		super();
	}
	public Notification(String title,String content,NotificationType type, NotificationKey key)
	{
		super();
		this.Title=title;
		this.Content=content;
		this.setType(type);
		this.setKey(key);
	}
	//#region getters and setters
	
	public NotificationKey getKey() {
		return NotificationKey.get(Key);
	}
	public void setKey(NotificationKey key) {
		Key = key.toShort();
	}
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
	public NotificationType getType() {
		return NotificationType.get(Type);
	}
	public void setType(NotificationType type) {
		Type = type.toShort();
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
	
	/**
	 * Obtiene las notificaciones ordenadas en orden de fechas
	 * que correspondan al tipo indicado
	 * @param type
	 * @return
	 */
	public static List<Notification> getNotificationsByType(NotificationType type)
	{
		return getNotificationsByType(type, -1);
	}
	/**
	 * Obtiene las notificaciones ordenadas en orden de fechas, con el numero maximo de notificaciones indicado y
	 * que correspondan al tipo indicado
	 * @param type
	 * @param limit si el limite es -1 devuelve todas las cuentas
	 * @return
	 */
	public static List<Notification> getNotificationsByType(NotificationType type, int limit)
	{
		From query =  new Select()
        .from(Notification.class).where("Type=?", type.toShort()).orderBy("InsertDate DESC");
		if(limit>0)
			query.limit(limit);
        return query.execute();
	}
}
