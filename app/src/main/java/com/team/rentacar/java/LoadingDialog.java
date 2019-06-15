package com.team.rentacar.java;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.team.rentacar.R;
import pl.droidsonroids.gif.GifImageView;

import java.util.Random;

public class LoadingDialog {
    String loading_msg;
    Context ctx;
    Dialog dialog;
    String query;

    public LoadingDialog(String loading_msg, Context ctx){
        this.loading_msg=loading_msg;
        this.ctx=ctx;
    }
    public LoadingDialog(Context ctx) {
//        this.loading_msg = loading_msg;
        this.ctx = ctx;
    }
    public LoadingDialog(String loading_msg, Context ctx, String query){
        this.loading_msg=loading_msg;
        this.ctx=ctx;
        this.query = query;
    }

    public void showdialog(){

        dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loadingdialog);
        dialog.setCancelable(true);
        GifImageView myimg =(GifImageView) dialog.findViewById(R.id.myimg);

        Random r = new Random();
        int myr = r.nextInt(6) + 1;

        switch (myr){
            case 1:
                myimg.setImageResource(R.drawable.fidget1);
                break;
            case 2:
                myimg.setImageResource(R.drawable.fidget2);
                break;
            case 3:
                myimg.setImageResource(R.drawable.fidget3);
                break;
            case 4:
                myimg.setImageResource(R.drawable.fidget4);
                break;
            case 5:
                myimg.setImageResource(R.drawable.fidget5);
                break;
            case 6:
                myimg.setImageResource(R.drawable.fidget6);
                break;
        }


        TextView mytext = (TextView)dialog.findViewById(R.id.mytxt);
        mytext.setText(loading_msg);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }
    public boolean ishowingg(){
        if(dialog!=null){
            if(dialog.isShowing())
                return  true;
            else
                return false;

        }
        else
            return false;
    }
    public void dismisss(){
        try {
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}