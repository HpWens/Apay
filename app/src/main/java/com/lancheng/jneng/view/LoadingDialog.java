package com.lancheng.jneng.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lancheng.jneng.R;

/**
 * creeate by AFLF
 */
public class LoadingDialog extends Dialog {

    private Activity mContext;

    public LoadingDialog(Context context) {
        super(context,R.style.loading_dialog);
        this.mContext = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog_layout);
        this.setCanceledOnTouchOutside(false);
    }

}
