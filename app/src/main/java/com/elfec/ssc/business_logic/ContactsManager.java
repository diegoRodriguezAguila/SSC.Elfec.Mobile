package com.elfec.ssc.business_logic;

import com.elfec.ssc.model.Contact;

/**
 * Se encarga de las operaciones relacionadas a los contactos
 * @author drodriguez
 *
 */
public class ContactsManager {

	/**
	 * Actualiza la información de contacto<br/>
	 * <b>NOTA.-</b> Reemplaza toda la información de contactos actual, por tanto
	 * todos los campos tienen que ser enviados nuevamente y deben ser válidos
	 * @param phone
	 * @param address
	 * @param email
	 * @param webPage
	 * @param facebook
	 * @param facebookId
	 */
	public static void updateContactData(String phone,String address,String email,
			String webPage,String facebook, String facebookId)
	{
		Contact contact = Contact.getDefaultContact();
		contact.setPhone(phone).setAddress(address).setEmail(email)
		.setWebPage(webPage).setFacebook(facebook).setFacebookId(facebookId)
		.save();
	}
}
