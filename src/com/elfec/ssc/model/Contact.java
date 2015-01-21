package com.elfec.ssc.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Guarda la información del contacto de la aplicacion
 * @author Diego
 */
@Table(name = "Contacts")
public class Contact extends Model{
	@Column(name = "Phone")
	private String Phone;
	@Column(name = "Address")
	private String Address;
	@Column(name = "Email")
	private String Email;
	@Column(name = "WebPage")
	private String WebPage;
	@Column(name = "Facebook")
	private String Facebook;
	@Column(name = "InsertDate", notNull = true)
	private DateTime InsertDate;
	
	@Column(name = "UpdateDate")
	private DateTime UpdateDate;

	
	public Contact()
	{
		super();
	}
	public Contact(String Phone,String Address,String Email,String WebPage,String Facebook)
	{
		super();
		this.Phone=Phone;
		this.Address=Address;
		this.Email=Email;
		this.WebPage=WebPage;
		this.Facebook=Facebook;
	}
	//#region getters and setters
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
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getWebPage() {
		return WebPage;
	}
	public void setWebPage(String webPage) {
		WebPage = webPage;
	}
	public String getFacebook() {
		return Facebook;
	}
	public void setFacebook(String facebook) {
		Facebook = facebook;
	}
	//#endregion
	
}
