package com.sravan.efactorapp.utils;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;


public class LoadingDialog {
    private Context context;
    private KProgressHUD kProgressHUD;

    public LoadingDialog(Context context2) {
        this.kProgressHUD = KProgressHUD.create(context2).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("Please wait").setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
    }

    public LoadingDialog(Context context2, String message) {
        this.kProgressHUD = KProgressHUD.create(context2).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(message).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
    }

    public LoadingDialog(Context context2, String message, String details) {
        this.kProgressHUD = KProgressHUD.create(context2).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel(message).setDetailsLabel(details).setCancellable(false).setAnimationSpeed(2).setDimAmount(0.5f);
    }

    public void show() {
        KProgressHUD kProgressHUD2 = this.kProgressHUD;
        if (kProgressHUD2 != null && !kProgressHUD2.isShowing()) {
            this.kProgressHUD.setLabel("Please wait");
        }
//        this.kProgressHUD.show();
    }

    public void show(String msg) {
        KProgressHUD kProgressHUD2 = this.kProgressHUD;
        if (kProgressHUD2 != null && !kProgressHUD2.isShowing()) {
            this.kProgressHUD.setLabel(msg);
        }
        this.kProgressHUD.show();
    }

    public void hide() {
        KProgressHUD kProgressHUD2 = this.kProgressHUD;
        if (kProgressHUD2 != null && kProgressHUD2.isShowing()) {
            this.kProgressHUD.dismiss();
        }
    }
}

