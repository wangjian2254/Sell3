package com.wj.sell3;

import java.io.*;
import java.util.*;

import android.content.*;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.*;
import com.lidroid.xutils.exception.DbException;
import com.wj.sell3.ui.*;
import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.db.models.Shiming;
import com.wj.sell.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.wj.sell.db.models.UserInfo;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogOKListener;
import com.wj.sell3.ui.AlertDialogCustom.AlertDialogCancelListener;

public class XiaoShouAnalysis extends Activity {
    /**
     * Called when the activity is first created.
     */
    static Context con;
    UserInfo user = null;
    EditText tel;
    EditText name;
    EditText number;
    RadioButton gender_male;
    RadioButton gender_famale;
    EditText ethnic;
    EditText date;
    EditText qixian;
    EditText address;
    EditText danwei;


    AlertDialogCustom localAlertDialogCustom;

    private Handler tmpMainHandler4;
    public ProgressDialog myDialog = null;
    public static final int SEARCHPLUGIN = Menu.FIRST + 1;
    public static final int APPLIST = Menu.FIRST + 2;
    TitleBar titleBar;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle b = msg.getData();
                    String[] cardInfo = b.getStringArray("cardInfo");
                    int i = 0;
                    String name = cardInfo[i++].trim();
                    String sex = cardInfo[i++].trim();
                    String ethnic = cardInfo[i++].trim();
                    String date = cardInfo[i++].trim();
                    String address = cardInfo[i++].trim();
                    String number = cardInfo[i++].trim();
                    String danwei = cardInfo[i++].trim();
                    String[] qixian = cardInfo[i++].trim().replace(" ", "").split("-");
                    String qixian_start = qixian[0];
                    String qixian_end = qixian[1];
                    String qixian_str = qixian_start.substring(0, 4) + "." + qixian_start.substring(4, 6) + "." + qixian_start.substring(6, 8)
                            + "-" + qixian_end.substring(0, 4) + "." + qixian_end.substring(4, 6) + "." + qixian_end.substring(6, 8);

                    XiaoShouAnalysis.this.name.setText(name);
                    XiaoShouAnalysis.this.number.setText(number);

                    if (sex.equals("男")) {
                        gender_male.setChecked(true);
                        gender_famale.setChecked(false);
                    } else {
                        gender_male.setChecked(false);
                        gender_famale.setChecked(true);
                    }
                    XiaoShouAnalysis.this.ethnic.setText(ethnic);
                    XiaoShouAnalysis.this.date.setText(date);
                    XiaoShouAnalysis.this.qixian.setText(qixian_str);
                    XiaoShouAnalysis.this.address.setText(address);
                    XiaoShouAnalysis.this.danwei.setText(danwei);
                    break;
                case -1:
                    ToastCustom.showMessage(con, "打开设备失败");
                    break;
                case -2:
                    ToastCustom.showMessage(con, "初始化设备失败");
                    break;
                case -3:
                    ToastCustom.showMessage(con, "读取信息失败");
                    break;
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


        name = (EditText) findViewById(R.id.name);
        tel = (EditText) findViewById(R.id.tel);
        number = (EditText) findViewById(R.id.number);
        gender_male = (RadioButton) findViewById(R.id.gender_male);
        gender_famale = (RadioButton) findViewById(R.id.gender_famale);
        ethnic = (EditText) findViewById(R.id.ethnic);
        date = (EditText) findViewById(R.id.date);
        qixian = (EditText) findViewById(R.id.qixian);
        address = (EditText) findViewById(R.id.address);
        danwei = (EditText) findViewById(R.id.danwei);


//        tmpMainHandler4 = new Handler() {
//
//            @Override
//            public void handleMessage(Message msg) {
//
//                if (msg.arg1 == 1) {
//                    if (myDialog != null) {
//                        myDialog.dismiss();
//                    }
//                    String m = (String) msg.obj;
//                    myDialog = ProgressDialog.show
//                            (
//                                    con,
//                                    "提示",
//                                    m
//                            );
//                    return;
//                }
//                if (msg.arg1 < 10) {
//                    String m = (String) msg.obj;
//                    if (myDialog != null && myDialog.isShowing()) {
//                        myDialog.setMessage(m);
//                    }
//                    return;
//
//                }
//                if (msg.arg1 == 10) {
//                    String m = (String) msg.obj;
//                    if (myDialog != null && myDialog.isShowing()) {
//                        myDialog.dismiss();
//                    }
//                    failResult(m);
//                    return;
//
//                }
//
//                if (msg.arg1 == 11) {
//                    String m = (String) msg.obj;
//                    if (myDialog != null && myDialog.isShowing()) {
//                        myDialog.dismiss();
//                    }
//                    successResult(m);
//                    return;
//
//                }
//
//
//                if (msg.arg1 == 404) {
//                    String m = (String) msg.obj;
//                    OAUtil.gotoReLogin(con, m);
//                    return;
//                }
//
//            }
//
//        };
        this.titleBar = ((TitleBar) findViewById(R.id.titlebar));
        this.titleBar.setTitle(R.string.shiming_luru);
        this.titleBar.setBackListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        this.titleBar.setUp();
    }

    public void showOffice() {

    }

    /**
     * 拍照 识别函数
     *
     * @param view
     */
    public void paiZhao(View view) {

        PackageManager localPackageManager = getPackageManager();
        try {
            if (isAppInstalled("com.cmcc.icrecognizer")) {
                createPackageContext("com.cmcc.icrecognizer", CONTEXT_IGNORE_SECURITY).getSharedPreferences("id_card_preference", 1).edit().clear().commit();
                int i = localPackageManager.getPackageInfo("com.cmcc.icrecognizer", 1).versionCode;
                if (getApkVersionCode() > i) {
                    installAPK();
                } else {
                    Intent localIntent = new Intent();
                    localIntent.setComponent(new ComponentName("com.cmcc.icrecognizer", "com.cmcc.icrecognizer.CaptureActivity"));
                    startActivityForResult(localIntent, 0);
                }
            }
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            localNameNotFoundException.printStackTrace();
        }

    }

    private int getApkVersionCode() {
        int i = 0;
        if ((save2SDCard(getFileFromAssert(this, "IDScanner.apk")) != null) && (Environment.getExternalStorageState().equals("mounted"))) {
            String str = Environment.getExternalStorageDirectory().getPath() + File.separator + "IDScanner.apk";
            i = getPackageManager().getPackageArchiveInfo(str, 1).versionCode;
        }
        return i;
    }

    private boolean isAppInstalled(String paramString) {
        PackageManager localPackageManager = getPackageManager();
        try {
            localPackageManager.getPackageInfo(paramString, 1);

            return true;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return false;
        }
    }

    private File save2SDCard(InputStream inputstream) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "IDScanner.apk";
            File f = new File(path);
            try {
                FileOutputStream out = new FileOutputStream(f);
                byte[] buffer = new byte[255];
                int rl = 0;
                while ((rl = inputstream.read(buffer)) > 0) {
                    out.write(buffer, 0, rl);
                }
                out.close();
                inputstream.close();
                return f;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;


    }

    private InputStream getFileFromAssert(Context paramContext, String paramString) {
        AssetManager localAssetManager = paramContext.getAssets();
        try {
            return localAssetManager.open(paramString);

        } catch (IOException localIOException) {


        }
        return null;
    }

    private void installAPK() {
        File localFile = save2SDCard(getFileFromAssert(this, "IDScanner.apk"));
        Intent localIntent = new Intent("android.intent.action.VIEW");
        localIntent.setDataAndType(Uri.fromFile(localFile), "application/vnd.android.package-archive");
        startActivity(localIntent);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if (paramInt1 == 0) {
            getInfo();
        } else if (paramInt1 == 100) {
            String str = paramIntent.getExtras().get("addr").toString();
            System.out.println("btaddr = " + str);
            doGetIDCardInfo(9, str);
        }
    }

    private void doGetIDCardInfo(int paramInt, String paramString) {
        new Thread(new IDCardRunable(this.handler, this, paramInt, paramString)).start();
    }

    private void getInfo() {
        try {

            SharedPreferences localSharedPreferences = createPackageContext("com.cmcc.icrecognizer", CONTEXT_IGNORE_SECURITY).getSharedPreferences("id_card_preference", 1);
            Map localMap = localSharedPreferences.getAll();
            Iterator localIterator = localMap.keySet().iterator();
            while (localIterator.hasNext()) {
                String key = (String) localIterator.next();
                String value = (String) localMap.get(key);
                if (key.equals("name")) {
                    name.setText(value);
                } else if (key.equals("id_number")) {
                    number.setText(value);
                } else if (key.equals("address")) {
                    address.setText(value);
                } else if (key.equals("sex")) {
                    if (value.equals("男")) {
                        gender_male.setChecked(true);
                        gender_famale.setChecked(false);
                    } else {
                        gender_male.setChecked(false);
                        gender_famale.setChecked(true);
                    }
                } else if (key.equals("nation")) {
                    ethnic.setText(value);
                } else if (key.equals("birth")) {
                    date.setText(value);
                } else if (key.equals("issue_authority")) {
                    danwei.setText(value);
                } else if (key.equals("validity")) {
                    qixian.setText(value);
                }
            }

        } catch (Exception localException) {

        }
    }


    /**
     * 蓝牙 硬件
     *
     * @param view
     */
    public void blueTooth(View view) {
        startActivityForResult(new Intent(this, BluetoothActivity.class), 100);
    }

    public void failResult(String msg) {
        localAlertDialogCustom = new AlertDialogCustom(this);
        localAlertDialogCustom.show();
        localAlertDialogCustom.setMessage(msg);
        localAlertDialogCustom.setOnOKListener("返回主界面", new AlertDialogOKListener() {

            @Override
            public void onOKClick() {
                // TODO Auto-generated method stub
                localAlertDialogCustom.dismiss();
                finish();

            }
        });
        localAlertDialogCustom.setOnCancelListener("修改信息", new AlertDialogCancelListener() {

            @Override
            public void onCancelClick() {
                // TODO Auto-generated method stub
                localAlertDialogCustom.dismiss();
            }
        });


    }

    public void successResult(String photo) {
        Intent mainIntent = new Intent(XiaoShouAnalysis.this, XiaoShouAnalysisConfirm.class);
        Bundle extras = new Bundle();
        extras.putString("tel", tel.getText().toString().trim());
        extras.putString("name", name.getText().toString().trim());
        extras.putString("number", number.getText().toString().trim());
        extras.putString("address", address.getText().toString().trim());
        extras.putString("photo", photo);
        mainIntent.putExtras(extras);
        con.startActivity(mainIntent);
//    	
//    	localAlertDialogCustom= new AlertDialogCustom(this);
//		localAlertDialogCustom.show();
//		localAlertDialogCustom.setMessage("信息正确。是否通过？");
//		localAlertDialogCustom.setOnOKListener("通过",new AlertDialogOKListener() {
//			
//			@Override
//			public void onOKClick() {
//				// TODO Auto-generated method stub
//				localAlertDialogCustom.dismiss();
//    			Intent mainIntent = new Intent(XiaoShouAnalysis.this,XiaoShouAnalysis2.class);
//    	    	Bundle extras=new Bundle();
//    	    	extras.putString("tel", tel.getText().toString().trim());
//    	    	extras.putString("name", name.getText().toString().trim());
//    	    	extras.putString("number", number.getText().toString().trim());
//    	    	extras.putString("address", address.getText().toString().trim());
//    	    	mainIntent.putExtras(extras);
//    	    	con.startActivity(mainIntent); 
//			}
//		});
//		localAlertDialogCustom.setOnCancelListener("不通过",new AlertDialogCancelListener() {
//			
//			@Override
//			public void onCancelClick() {
//				// TODO Auto-generated method stub
//				localAlertDialogCustom.dismiss();
//			}
//		});

    }


    public void queryTongji(View view) {
        if ("".equals(tel.getText().toString())) {
//            Toast.makeText(con, "请填写手机号", Toast.LENGTH_LONG).show();
            ToastCustom.showMessage(con, "请填写手机号");
            return;
        }

        if("".equals(number.getText().toString())){
            ToastCustom.showMessage(con, "请扫描身份证");
            return;
        }

        if("".equals(name.getText().toString())){
            ToastCustom.showMessage(con, "请扫描身份证");
            return;
        }
        if("".equals(address.getText().toString())){
            ToastCustom.showMessage(con, "请扫描身份证");
            return;
        }
        if("".equals(danwei.getText().toString())){
            ToastCustom.showMessage(con, "请扫描身份证");
            return;
        }
        if("".equals(qixian.getText().toString())){
            ToastCustom.showMessage(con, "请扫描身份证");
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone_number", tel.getText().toString());
        params.addBodyParameter("cardno", number.getText().toString());
        params.addBodyParameter("name", name.getText().toString());
        params.addBodyParameter("address", address.getText().toString());
        params.addBodyParameter("qfjg", danwei.getText().toString());
        params.addBodyParameter("yxqx", qixian.getText().toString());
        final Shiming shiming = new Shiming();
        shiming.setAddress(address.getText().toString());
        shiming.setCardno(number.getText().toString());
        shiming.setName(name.getText().toString());
        shiming.setPhone_number(tel.getText().toString());
        shiming.setQfjg(danwei.getText().toString());
        shiming.setYxqx(qixian.getText().toString());
        Date date1 = new Date();
        shiming.setCreate_time(""+(date1.getYear()+1900)+"."+date1.getMonth()+"."+date1.getDate()+" "+date1.getHours()+":"+date1.getMinutes()+":"+date1.getSeconds());

        try {
            SellApplication.db.saveOrUpdate(shiming);
        } catch (DbException e) {
            e.printStackTrace();
        }

        SellApplication.showDialog("正在实名","", con);
        HttpCallResultBackShiming httpCallResultBackShiming = new HttpCallResultBackShiming(new HttpCallResultBack() {
            @Override
            public void doresult(HttpResult result) {
                if(result.getResult()!=null){
                    Shiming shiming1 = new Shiming(result.getResult());
                    if(result.isSuccess()){

                        shiming1.setSuccess(2);
                        finish();
                    }
                    else{
                        shiming1.setSuccess(0);
                        shiming1.setMessage(result.getMessage());
                        SellApplication.failureResult(result);
                    }
                    try {
                        SellApplication.db.saveOrUpdate(shiming1);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }


                }else{
                    if(!result.isSuccess()){
                        shiming.setMessage(result.getMessage());
                        try {
                            SellApplication.db.saveOrUpdate(shiming);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        SellApplication.failureResult(result);
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

    public String getDate(DatePicker dp) {
        String year = String.valueOf(dp.getYear());
        String month = String.valueOf(dp.getMonth() + 1);
        String day = String.valueOf(dp.getDayOfMonth());
        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        return year + "-" + month + "-" + day;
    }

    public void queryXiaoShouToday(View view) {
    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        populateMenu(menu);
    }

    public void populateMenu(Menu menu) {
        menu.add(Menu.NONE, SEARCHPLUGIN, Menu.NONE, "返回主界面");
        menu.add(Menu.NONE, APPLIST, Menu.NONE, "清空");
//		menu.add(Menu.NONE, APPLIST, Menu.NONE, "应用列表");
//		menu.add(Menu.NONE, SYSTEM, Menu.NONE, "系统消息");
//		menu.add(Menu.NONE, REFASH, Menu.NONE, "刷新");
//		menu.add(Menu.NONE, ALLREFASH, Menu.NONE, "反馈意见");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        populateMenu(menu);
        return (super.onCreateOptionsMenu(menu));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return (applyMenuChoice(item) || super.onOptionsItemSelected(item));
    }

    public boolean onContextItemSelected(MenuItem item) {
        return (applyMenuChoice(item) || super.onContextItemSelected(item));
    }


    public boolean applyMenuChoice(MenuItem item) {
        switch (item.getItemId()) {

            case SEARCHPLUGIN:
                finish();
                return true;
            case APPLIST:
                finish();
                return true;
        }
        return false;
    }


    public void onResume() {
        super.onResume();
        if (Convert.newtel) {
            Convert.newtel = false;
            tel.setText("");
            name.setText("");
            number.setText("");
            address.setText("");
        }
    }

    public void onPause() {
        super.onPause();
    }
}