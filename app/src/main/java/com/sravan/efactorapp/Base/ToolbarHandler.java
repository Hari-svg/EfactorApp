package com.sravan.efactorapp.Base;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sravan.efactorapp.R;


/**
 * Created by ubuntu on 7/12/17.
 */

public class ToolbarHandler implements View.OnClickListener {

    private BaseActivity baseActivity;
    private LinearLayout llToolbar2;
    private ImageView ivBackButton;
    private CustomTextView tvTitle;
    private static final String TAG = "ToolbarHandler";

    public ToolbarHandler(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public void findViews(){
        llToolbar2 = (LinearLayout) baseActivity.findViewById(R.id.llToolbar2);
        ivBackButton = (ImageView) baseActivity.findViewById(R.id.ivBackButton);
        tvTitle = (CustomTextView) baseActivity.findViewById(R.id.tvTitle);

        ivBackButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBackButton:

              baseActivity.onBackPressed();

                break;
            case R.id.tvTitle:

                titleButtonClick();
                break;

        }
    }

    public void titleButtonClick() {
        Log.e(TAG, "titleButtonClick: " );
    }

    public void setTitleButtonVisibilty(boolean visibility){
        if (visibility){
            tvTitle.setVisibility(View.VISIBLE);
        }
        else {
            tvTitle.setVisibility(View.GONE);
        }

    }

    public void setTitleText(String title){

        if (title!=null && !title.isEmpty()){
            tvTitle.setText(title);
        }
    }

    public  void setbackButtonVisibilty(boolean visibility){
        if (visibility){
            ivBackButton.setVisibility(View.VISIBLE);
        }
        else {
            ivBackButton.setVisibility(View.GONE);
        }
    }
    public void setToolbarVisibility(boolean visibility){
        if (visibility){
            llToolbar2.setVisibility(View.VISIBLE);
        }
        else {
            llToolbar2.setVisibility(View.GONE);
        }
    }
}
