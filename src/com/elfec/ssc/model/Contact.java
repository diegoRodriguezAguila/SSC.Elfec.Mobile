package com.elfec.ssc.model;

import org.joda.time.DateTime;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/**
 * Guarda la información del contacto de la aplicacion
 * 
 * @author Diego
 */
@Table(name = "Contacts")
public class Contact extends Model {

	private static final String PHONE = "176 / 4259400";
	private static final String ADDRESS = "Av. Heroinas entre c. Falsuri y c. Costanera #686";
	private static final String EMAIL = "elfec@elfec.bo";
	private static final String WEB_PAGE = "www.elfec.bo";
	private static final String FACEBOOK = "touch.facebook.com/ende.elfec";
	/**
	 * Sirve para invocar la página de facebook con la app respectiva, se lo
	 * saca entrando a graph.facebook.com/<usuario>
	 */
	private static final String FACEBOOK_ID = "1451525075080740";

	@Column(name = "Phone", notNull = true)
	private String Phone;
	@Column(name = "Address", notNull = true)
	private String Address;
	@Column(name = "Email", notNull = true)
	private String Email;
	@Column(name = "WebPage", notNull = true)
	private String WebPage;
	@Column(name = "Facebook", notNull = true)
	private String Facebook;
	@Column(name = "FacebookId", notNull = true)
	private String FacebookId;
	@Column(name = "InsertDate", notNull = true)
	private DateTime InsertDate;

	@Column(name = "UpdateDate")
	private DateTime UpdateDate;

	public Contact() {
		super();
	}

	public Contact(String phone, String address, String email, String webPage,
			String facebook, String facebookId) {
		super();
		this.Phone = phone;
		this.Address = address;
		this.Email = email;
		this.WebPage = webPage;
		this.Facebook = facebook;
		this.FacebookId = facebookId;
		this.InsertDate = DateTime.now();
	}

	/**
	 * Crea y guarda el contacto por defecto con los valores estáticos
	 * predefinidos
	 */
	public static Contact createDefaultContact() {
		Contact defaultContact = new Contact(PHONE, ADDRESS, EMAIL, WEB_PAGE,
				FACEBOOK, FACEBOOK_ID);
		defaultContact.save();
		return defaultContact;
	}

	/**
	 * Obtiene el contacto por defecto
	 * 
	 * @return el contacto por defecto
	 */
	public static Contact getDefaultContact() {
		return new Select().from(Contact.class).executeSingle();
	}

	@Override
	public boolean equals(Object other) {
		if ((other != null) && (other instanceof Contact)) {
			Contact otherContact = (Contact) other;
			return (this.Phone == otherContact.getPhone())
					&& (this.Address == otherContact.getAddress())
					&& (this.Email == otherContact.getEmail())
					&& (this.WebPage == otherContact.getWebPage())
					&& (this.Facebook == otherContact.getFacebook())
					&& (this.FacebookId == otherContact.getFacebookId());
		}
		return false;

	}

	// #region getters and setters
	public DateTime getInsertDate() {
		return InsertDate;
	}

	public Contact setInsertDate(DateTime insertDate) {
		InsertDate = insertDate;
		return this;
	}

	public DateTime getUpdateDate() {
		return UpdateDate;
	}

	public Contact setUpdateDate(DateTime updateDate) {
		UpdateDate = updateDate;
		return this;
	}

	public String getPhone() {
		return Phone;
	}

	public Contact setPhone(String phone) {
		Phone = phone;
		return this;
	}

	public String getAddress() {
		return Address;
	}

	public Contact setAddress(String address) {
		Address = address;
		return this;
	}

	public String getEmail() {
		return Email;
	}

	public Contact setEmail(String email) {
		Email = email;
		return this;
	}

	public String getWebPage() {
		return WebPage;
	}

	public Contact setWebPage(String webPage) {
		WebPage = webPage;
		return this;
	}

	public String getFacebook() {
		return Facebook;
	}

	public Contact setFacebook(String facebook) {
		Facebook = facebook;
		return this;
	}

	public String getFacebookId() {
		return FacebookId;
	}

	public Contact setFacebookId(String facebookId) {
		FacebookId = facebookId;
		return this;
	}

	// #endregion

}
