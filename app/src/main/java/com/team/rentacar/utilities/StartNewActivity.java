package com.team.rentacar.utilities;

import android.content.Context;
import android.content.Intent;

public class StartNewActivity<T> {
   public StartNewActivity(Context from,Class<T> to){
       Intent intent=new Intent(from,to);
       from.startActivity(intent);
   }
    public StartNewActivity(Context from,Class<T> to,int extra){
        Intent intent=new Intent(from,to);
        intent.putExtra("extra",extra);
        from.startActivity(intent);
    }
}
