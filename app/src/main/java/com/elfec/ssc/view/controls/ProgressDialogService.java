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

    public ProgressDialogService setMessage(CharSequence message){
        mTxtMessage.setText(message);
        return this;
    }

    public ProgressDialogService setMessage(@StringRes int messageId){
        mTxtMessage.setText(messageId);
        return this;
    }

    public ProgressDialogService setTitle(CharSequence title){
        mDialogBuilder.setTitle(title);
        return this;
    }

    public ProgressDialogService setTitle(@StringRes int titleId){
        mDialogBuilder.setTitle(titleId);
        return this;
    }

    public ProgressDialogService setCancelable(boolean cancelable){
        mDialogBuilder.setCancelable(cancelable);
        return this;
    }

    public ProgressDialogService setNegativeButton(@StringRes int buttonLabel, DialogInterface.OnClickListener
            listener){
        mDialogBuilder.setNegativeButton(buttonLabel, listener);
        return this;
    }

    public ProgressDialogService setCanceledOnTouchOutside(boolean cancel){
        if(mDialog==null)
            mDialog = mDialogBuilder.create();
        mDialog.setCanceledOnTouchOutside(cancel);
        return this;
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
