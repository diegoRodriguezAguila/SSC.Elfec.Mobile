package com.elfec.ssc.model;

import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Notification {
    @SerializedName("Key")
    private short Key;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Content")
    private String Content;
    @SerializedName("Type")
    private short Type;
    @SerializedName("InsertDate")
    private DateTime InsertDate;
    @SerializedName("UpdateDate")
    private DateTime UpdateDate;

    public Notification() {
    }

    public Notification(String title, String content, NotificationType type, NotificationKey key) {
        this.Title = title;
        this.Content = content;
        this.setType(type);
        this.setKey(key);
    }
    //region getters and setters

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
    //endregion

}
