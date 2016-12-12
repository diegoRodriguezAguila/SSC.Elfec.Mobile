package com.elfec.ssc.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;
import com.elfec.ssc.model.Contact;
import com.elfec.ssc.presenter.ContactPresenter;
import com.elfec.ssc.presenter.views.IContactsView;

public class ContactsActivity extends BaseActivity implements IContactsView {

    private ContactPresenter mPresenter;

    private boolean isFirst = true;
    private int mSelectedPhoneNumberIndex;
    private String facebookUrl;
    private String facebookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        mPresenter = new ContactPresenter(this);
        mPresenter.loadContact();
        isFirst = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirst)
            overridePendingTransition(R.anim.slide_right_in,
                    R.anim.slide_right_out);
        else
            isFirst = false;
    }

    @Override
    public void releasePresenter() {
        mPresenter.close();
        mPresenter = null;
    }

    @Override
    public void setContact(Contact contact) {
        ((TextView) findViewById(R.id.contact_phone)).setText(contact.getPhone());
        ((TextView) findViewById(R.id.contact_address))
                .setText(contact.getAddress());
        ((TextView) findViewById(R.id.contact_email)).setText(contact.getEmail());
        ((TextView) findViewById(R.id.contact_web)).setText(contact.getWebPage());
        ContactsActivity.this.facebookUrl = "https://" + contact.getFacebook();
        ContactsActivity.this.facebookId = contact.getFacebookId();
    }

    public void contactPhoneClick(View view) {
        String phoneNumbersTxt = ((TextView) view).getText().toString();
        final String[] phoneNumbers = phoneNumbersTxt.split("/");
        if (phoneNumbers.length > 1)
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_phone_pick)
                    .setNegativeButton(R.string.btn_cancel, null)
                    .setSingleChoiceItems(phoneNumbers, -1,
                            (dialog, which) -> {
                                mSelectedPhoneNumberIndex = which;
                            })
                    .setPositiveButton(R.string.btn_dial,
                            (dialog, which) -> goToPhoneNumber(phoneNumbers[mSelectedPhoneNumberIndex]
                                    .trim())).show();
        else
            goToPhoneNumber(phoneNumbersTxt);
    }

    public void contactEmailClick(View view) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(("mailto:" + ((TextView) view).getText())));
        startActivity(webIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void contactWebClick(View view) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(("http://" + ((TextView) view).getText())));
        startActivity(webIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    public void contactFacebookClick(View view) {
        Intent facebookIntent = getFacebookIntent();
        startActivity(facebookIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

    /**
     * Obtiene el intent necesario para abrir la página de facebook, en caso de
     * que no se tenga la aplicación la abre por defecto en un browser
     *
     * @return Intent para el facebook
     */
    public Intent getFacebookIntent() {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(getFacebookPageURL(this)));
    }

    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

            boolean activated = packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if (activated) {
                if ((versionCode >= 3002850)) {
                    return "fb://facewebmodal/f?href="+ facebookUrl;
                } else {
                    return "fb://page/" + facebookId;
                }
            } else {
                return facebookUrl;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return facebookUrl;
        }
    }

    private void goToPhoneNumber(String phone) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phone));
        startActivity(dialIntent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
    }

}
