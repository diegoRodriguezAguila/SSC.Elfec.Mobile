package com.elfec.ssc.view.controls;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.elfec.ssc.R;

import static com.elfec.ssc.R.layout.progress_dialog_material;

/**
 * Dialogo de progreso indefinido
 */
public class ProgressDialogService implements DialogInterface {

    private AlertDialog mDialog;
    private AlertDialog.Builder mDialogBuilder;
    private View mRootView;
    private TextView mTxtMessage;

    public ProgressDialogService(Context context){
        mRootView = LayoutInflater.from(context).inflate(
                progress_dialog_material, null, false);
        mDialogBuilder = new AlertDialog.Builder(context).setView(mRootView);
        mTxtMessage = (TextView) mRootView.findViewById(R.id.txt_progress_message);
    }

    public void setMessage(CharSequence message){
        mTxtMessage.setText(message);
    }

    public void setMessage(@StringRes int messageId){
        mTxtMessage.setText(messageId);
    }

    public void setTitle(CharSequence title){
        mDialogBuilder.setTitle(title);
    }

    public void setTitle(@StringRes int titleId){
        mDialogBuilder.setTitle(titleId);
    }

    public void setCancelable(boolean cancelable){
        mDialogBuilder.setCancelable(cancelable);
    }

    public void setNegativeButton(@StringRes int buttonLabel, DialogInterface.OnClickListener
            listener){
        mDialogBuilder.setNegativeButton(buttonLabel, listener);
    }

    public void setCanceledOnTouchOutside(boolean cancel){
        if(mDialog==null)
            mDialog = mDialogBuilder.create();
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void show(){
        mDialog.show();
    }

    @Override
    public void cancel() {
        if(mDialog==null)
            mDialog = mDialogBuilder.create();
        mDialog.cancel();
    }

    @Override
    public void dismiss() {
        if(mDialog==null)
            mDialog = mDialogBuilder.create();
        mDialog.dismiss();
    }
}
