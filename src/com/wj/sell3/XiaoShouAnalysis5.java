package com.wj.sell3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.adapter.ShimingItemAdapter;
import com.wj.sell.db.models.Shiming;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.HttpCallResultBack;
import com.wj.sell.util.HttpCallResultBackShimingQuery;
import com.wj.sell.util.HttpCallResultBackShimingResultQuery;
import com.wj.sell.util.HttpResult;
import com.wj.sell3.ui.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class XiaoShouAnalysis5 extends Activity {
    /**
     * Called when the activity is first created.
     */
    Context con;
    UserInfo user = null;
    DatePicker datePicker_start ;
    DatePicker datePicker_end ;
    Calendar c;
    Calendar c_end;
    TextView txt_result ;



    TitleBar titleBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;
        Bundle bunde = this.getIntent().getExtras();
        user = SellApplication.getUserInfoIdByUid(SellApplication.getUidCurrent());
        setContentView(R.layout.real_name_query);

        txt_result = (TextView)findViewById(R.id.result);



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
        datePicker_start = (DatePicker)findViewById(R.id.datePicker_start);
        datePicker_end = (DatePicker)findViewById(R.id.datePicker_end);

        c = Calendar.getInstance();
        c_end = Calendar.getInstance();
        int year=c.get(Calendar.YEAR);
        int monthOfYear=c.get(Calendar.MONTH);
        int dayOfMonth=c.get(Calendar.DAY_OF_MONTH);
        datePicker_start.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
            }

        });

        datePicker_end.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                c_end.set(year, monthOfYear, dayOfMonth);
            }

        });


        queryTongji(null);
    }




    public void queryTongji(View view) {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");

        RequestParams params = new RequestParams();
        params.addBodyParameter("start_date", df.format(c.getTime()));
        params.addBodyParameter("end_date", df.format(c_end.getTime()));
        HttpCallResultBackShimingResultQuery httpCallResultBackShiming =new HttpCallResultBackShimingResultQuery(new HttpCallResultBack() {
            @Override
            public void doresult(HttpResult result) {
                if(result.getResult()!=null){
                    if(result.getResult().has("count")){

                        txt_result.setText("实名成功数量："+result.getResult().optInt("count"));
                    }
                }else{
                    SellApplication.failureResult(result);
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