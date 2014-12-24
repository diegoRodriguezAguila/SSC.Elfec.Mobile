package com.elfec.ssc.view;

import com.elfec.ssc.R;
import com.elfec.ssc.presenter.ViewAccountsPresenter;
import com.elfec.ssc.presenter.views.IViewAccounts;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

public class ViewAccounts extends ActionBarActivity implements IViewAccounts {

	private ViewAccountsPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);
        presenter = new ViewAccountsPresenter(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_accounts, menu);
        return true;
    }
    
}
