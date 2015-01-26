package com.elfec.ssc.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Guarda la información del contacto de la aplicacion
 * @author Diego
 */
@Table(name = "Contacts")
public class Contact extends Model{
	
	private static final String PHONE="176";
	private static final String ADDRESS="Av. Heroinas entre c. Falsuri y c. Costanera #686";
	private static final String EMAIL="elfec@elfec.com";
	private static final String WEB_PAGE="www.elfec.bo";
	private static final String FACEBOOK="touch.facebook.com/ende.elfec";
	/**
	 * Sirve para invocar la página de facebook con la app respectiva,
	 * se lo saca entrando a graph.facebook.com/<usuario>
	 */
	private static final String FACEBOOK_ID="1451525075080740";
	
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
	@Column(name = "FacebookId")
	private String FacebookId;
	@Column(name = "InsertDate", notNull = true)
	private DateTime InsertDate;
	
	@Column(name = "UpdateDate")
	private DateTime UpdateDate;

	
	public Contact()
	{
		super();
	}
	public Contact(String phone,String address,String email,String webPage,String facebook, String facebookId)
	{
		super();
		this.Phone=phone;
		this.Address=address;
		this.Email=email;
		this.WebPage=webPage;
		this.Facebook=facebook;
		this.FacebookId = facebookId;
		this.InsertDate=DateTime.now();
	}
	
	/**
	 * Crea y guarda el contacto por defecto con los valores estáticos predefinidos
	 */
	public static Contact createDefaultContact()
	{
		Contact defaultContact=new Contact(PHONE, ADDRESS, EMAIL, WEB_PAGE, FACEBOOK, FACEBOOK_ID);
		defaultContact.save();
		return defaultContact;
	}
	
	public static Contact getDefaultContact()
	{
		return new Select().from(Contact.class).executeSingle();
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
	public String getFacebookId() {
		return FacebookId;
	}
	public void setFacebookId(String facebookId) {
		FacebookId = facebookId;
	}
	
	//#endregion
	
}
