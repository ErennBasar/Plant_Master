// LoadingDialog.java - Yükleme animasyonu için özel dialog sınıfı
package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.PlantMaster.plantmaster.R;

public class LoadingDialog {
    private Dialog dialog;
    private LottieAnimationView animationView;

    public LoadingDialog(Context context) {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);


        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        animationView = dialog.findViewById(R.id.animation_view);
        animationView.setAnimation("loading.json");
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
            animationView.playAnimation();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            animationView.cancelAnimation();
            dialog.dismiss();
        }
    }
}