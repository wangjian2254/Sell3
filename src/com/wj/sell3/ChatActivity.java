package com.wj.sell3;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.wj.sell.adapter.ChatMsgViewAdapter;
import com.wj.sell.db.models.ChatMsgEntity;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.HttpCallResultBack;
import com.wj.sell.util.HttpCallResultBackSendChat;
import com.wj.sell.util.HttpResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author geniuseoe2012
 *         更多精彩，请关注我的CSDN博客http://blog.csdn.net/geniuseoe2012
 *         android开发交流群：200102476
 */
public class ChatActivity extends Activity implements OnClickListener, OnItemClickListener {
    /**
     * Called when the activity is first created.
     */

    private Button mBtnSend;
    private Button mBtnBack;
    private EditText mEditTextContent;
    private TextView tousername;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    private UserInfo user;
    private Handler tempchathandler;
    private Context con;

    private String request_id="";

    public static Handler chathandler= null;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;
        setContentView(R.layout.im_chat_xiaohei);
//        String username = this.getIntent().getExtras().getString("username");


        //启动activity时不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        UserUtil.chatUserInfo(user, item.username, true);
        tousername = (TextView) findViewById(R.id.tousername);
        initView();
        initData();
        tempchathandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                // // 接收子线程的消息
                if (msg.what == 1) {
                    if (msg.obj == null) {
                        return;
                    }
                    Object m = msg.obj;
                    ChatMsgEntity chat = null;
                    if (m instanceof ChatMsgEntity) {
                        chat = (ChatMsgEntity) m;

                        mDataArrays.add(chat);
                        mAdapter.notifyDataSetChanged();
                    }

                }

                if(msg.what==404){
                    if(Convert.debug){
                        if(msg.obj!=null){
                            Toast.makeText(ChatActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }

        };

//        ChatUtil.readChat( item.username,user);
    }


    public void initView() {
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setOnItemClickListener(this);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(this);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
    }


    private void refreshData() {
        //todo:刷新获取的消息
        try {
            List<ChatMsgEntity> list = SellApplication.db.findAll(Selector.from(ChatMsgEntity.class).orderBy("id").limit(30));
            if(list!=null){
                mDataArrays.clear();
                for(ChatMsgEntity chat:list){
                    mDataArrays.add(chat);
                }
            }


        } catch (DbException e) {
            e.printStackTrace();
        }
        //mDataArrays;
//
//        ChatUtil.getChatList(mDataArrays, user, item);
//        if (mDataArrays.size() > 0) {
//            ChatUtil.updateChat(item.username,user);
//        }
    }

    public void initData() {

        refreshData();
        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                send();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        ChatMsgEntity chat = mAdapter.getItem(arg2);
//        if (!chat.fx && (chat.status == 0 || chat.status == 3)) {
//            ChatTask ct = new ChatTask(con);
//            ct.setChat(chat);
//            ct.setmMainHandler(refreshHandler);
//            ct.start();
//            chat.status=1;
//            mAdapter.notifyDataSetChanged();
//        }
        closeInput();

    }

    /**
     * 关闭键盘事件
     *
     * @author shimiso
     * @update 2012-7-4 下午2:34:34
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public static String getDate() {
        return Convert.format1.format(new Date());
    }


    private void send() {
        String contString = mEditTextContent.getText().toString();
        RequestParams params = new RequestParams();
        params.addBodyParameter("text", contString);
        params.addBodyParameter("request_id", request_id);
        if (contString.length() > 0) {
            String time = getDate();
            final ChatMsgEntity chat = new ChatMsgEntity();
            chat.fx = false;
            chat.message = contString;
            chat.time = getDate();
            chat.status = 1;
            mDataArrays.add(chat);

            HttpCallResultBackSendChat httpCallResultBackSendChat = new HttpCallResultBackSendChat(new HttpCallResultBack() {
                @Override
                public void doresult(final HttpResult result) {
                    if(result.isSuccess()){
                        chat.status = 2;
                        try {
                            SellApplication.db.save(chat);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                mListView.setSelection(mListView.getCount() - 1);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(con, result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }


                }

                @Override
                public void dofailure() {

                }
            });
//            httpCallResultBackSendChat.
            httpCallResultBackSendChat.setParams(params);
            SellApplication.post(httpCallResultBackSendChat);


            mEditTextContent.setText("");
        }
    }



//    private String getDate() {
//        Calendar c = Calendar.getInstance();
//
//        String year = String.valueOf(c.get(Calendar.YEAR));
//        String month = String.valueOf(c.get(Calendar.MONTH));
//        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
//        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
//        String mins = String.valueOf(c.get(Calendar.MINUTE));
//        
//        
//        StringBuffer sbBuffer = new StringBuffer();
//        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
//        						
//        						
//        return sbBuffer.toString();
//    }


    public void onResume() {
        super.onResume();
        refreshData();
        mListView.setSelection(mListView.getCount() - 1);
        mAdapter.notifyDataSetChanged();

        MobclickAgent.onResume(this);

        chathandler = tempchathandler;

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        chathandler = null;
    }



}