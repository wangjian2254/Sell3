package com.wj.sell3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.adapter.ShimingItemAdapter;
import com.wj.sell.db.models.Shiming;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.*;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogCancelListener;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogOKListener;
import com.wj.sell3.ui.TitleBar;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XiaoShouAnalysis4 extends Activity {
    /**
     * Called when the activity is first created.
     */
    Context con;
    UserInfo user = null;

    ListView listView;
    ShimingItemAdapter shimingItemAdapter = null;


    TitleBar titleBar;
    List<Shiming> shiminglist = new ArrayList<Shiming>();

    Handler shiminghandler = new Handler(){
        public void handlerMessage(Message msg){
            if(msg.what==0){
                for(Shiming shiming: shiminglist){
                    if(shiming.getS_id()==msg.arg1){
                        shiming.setSuccess(msg.arg2);

                    }
                }

                shimingItemAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;
        Bundle bunde = this.getIntent().getExtras();
        user = SellApplication.getUserInfoIdByUid(SellApplication.getUidCurrent());
        setContentView(R.layout.real_name_registration_form);

        this.listView = (ListView)findViewById(R.id.real_name_list);
        shimingItemAdapter = new ShimingItemAdapter(this, shiminglist);
        listView.setAdapter(shimingItemAdapter);


        this.titleBar = ((TitleBar) findViewById(R.id.titlebar));
        this.titleBar.setTitle(R.string.shiming_jilu);
        this.titleBar.setBackListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        this.titleBar.setUp();
        initRealNameList();
        for(Shiming shiming: shiminglist){
            queryTongji(shiming.getS_id());
        }
    }

    public void initRealNameList() {
        shiminglist.clear();
        try {
            List<Shiming> l = SellApplication.db.findAll(Selector.from(Shiming.class).where("success","!=", "2").orderBy("id", true));
            if(l!=null){
                 shiminglist.addAll(l);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        shimingItemAdapter.notifyDataSetChanged();
    }



    public void queryTongji(final int request_id) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("request_id", String.valueOf(request_id));
        HttpCallResultBackShimingQuery httpCallResultBackShiming =new HttpCallResultBackShimingQuery(new HttpCallResultBack() {
            @Override
            public void doresult(HttpResult result) {
                if(result.getResult()!=null){
                    if(result.getResult().has("state")){

                        shiminghandler.obtainMessage(0, request_id, result.getResult().optInt("state")).sendToTarget();
                    }
                }
            }

            @Override
            public void dofailure() {

            }
        });
        httpCallResultBackShiming.setParams(params);
        SellApplication.post(httpCallResultBackShiming);

    }

    public void onPause() {
        super.onPause();
    }
}