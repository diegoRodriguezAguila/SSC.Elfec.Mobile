package com.elfec.ssc.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Guarda la información del contacto de la aplicacion
 *
 * @author Diego
 */
public class Contact {

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

    @SerializedName("Phone")
    private String Phone;
    @SerializedName("Address")
    private String Address;
    @SerializedName("Email")
    private String Email;
    @SerializedName("WebPage")
    private String WebPage;
    @SerializedName("Facebook")
    private String Facebook;
    @SerializedName("FacebookId")
    private String FacebookId;
    @SerializedName("created_at")
    private DateTime InsertDate;
    @SerializedName("updated_at")
    private DateTime UpdateDate;

    public Contact() {
    }

    public Contact(String phone, String address, String email, String webPage,
                   String facebook, String facebookId) {
        this.Phone = phone;
        this.Address = address;
        this.Email = email;
        this.WebPage = webPage;
        this.Facebook = facebook;
        this.FacebookId = facebookId;
        this.InsertDate = DateTime.now();
    }

    /**
     * Crea el contacto con la información por defecto con los valores estáticos
     * predefinidos
     */
    public static Contact defaultContact() {
        return new Contact(PHONE, ADDRESS, EMAIL, WEB_PAGE,
                FACEBOOK, FACEBOOK_ID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return Phone.equals(contact.Phone) && Address.equals(contact.Address) &&
                Email.equals(contact.Email) && WebPage.equals(contact.WebPage) &&
                Facebook.equals(contact.Facebook) && FacebookId.equals(contact.FacebookId);

    }

    @Override
    public int hashCode() {
        int result = Phone.hashCode();
        result = 31 * result + Address.hashCode();
        result = 31 * result + Email.hashCode();
        result = 31 * result + WebPage.hashCode();
        result = 31 * result + Facebook.hashCode();
        result = 31 * result + FacebookId.hashCode();
        return result;
    }

    //region getters and setters
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

    //endregion

}
