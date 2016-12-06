package com.elfec.ssc.model;

import android.os.Bundle;

import com.elfec.ssc.model.enums.NotificationKey;
import com.elfec.ssc.model.enums.NotificationType;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Notification {
    @SerializedName("Key")
    private short key;
    @SerializedName("Title")
    private String title;
    @SerializedName("Content")
    private String content;
    @SerializedName("Type")
    private short type;
    @SerializedName("InsertDate")
    private DateTime insertDate;
    @SerializedName("UpdateDate")
    private DateTime updateDate;

    public Notification() {
    }

    public Notification(String title, String content, NotificationType type, NotificationKey key) {
        this.title = title;
        this.content = content;
        this.setType(type);
        this.setKey(key);
        insertDate = DateTime.now();
    }

    public Notification(Bundle payload) {
        this(payload.getString("title"), payload.getString("message"),
                NotificationType.get(Short.parseShort(payload.getString("type"))),
                NotificationKey.get(payload.getString("key")));
    }

    //region getters and setters

    public NotificationKey getKey() {
        return NotificationKey.get(key);
    }

    public void setKey(NotificationKey key) {
        this.key = key.toShort();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationType getType() {
        return NotificationType.get(type);
    }

    public void setType(NotificationType type) {
        this.type = type.toShort();
    }

    public DateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(DateTime insertDate) {
        this.insertDate = insertDate;
    }

    public DateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(DateTime updateDate) {
        this.updateDate = updateDate;
    }
    //endregion

}
