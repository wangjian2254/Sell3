package com.wj.sell3;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell.util.Convert;
import com.wj.sell.util.HttpCallResultBackBase;
import com.wj.sell.util.HttpResult;
import com.wj.sell3.ui.AlertDialogCustom;
import com.wj.sell3.ui.ToastCustom;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangjian2254 on 15/4/4.
 */
public class SellApplication extends Application {
    private static List<Activity> activityList = new LinkedList<Activity>();

    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String USER_PROJECT = "USER_PROJECT";

    private static ProgressDialog mypDialog;

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 添加Activity到容器中
    public void removeActivity(Activity activity) {
        for(int i=0;i<activityList.size();i++){
            if(activityList.get(i)==activity){
                activityList.remove(i);
                return;
            }
        }
    }


    private static SellApplication instance;

    public static Context applicationContext;

    public static SellApplication getInstance() {
        return instance;
    }

    public static HttpUtils httpUtils;
    public static HttpUtils tempHttpUtils;
    public static DbUtils db;
    private static PreferencesCookieStore preferencesCookieStore;

//    private static boolean loading = true;

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    public static boolean is_sd = false;

    public static Handler handler ;
    public static DisplayImageOptions personOptions;

    /**
     * cookie、数据库 初始化
     * by:王健 at:2015-1-16
     * 添加环信初始化操作
     * by:王健 at:2015-1-22
     * 配置 ImageLoad
     * by王健 at:2015-2-2
     * 如果没有sd卡，要提示，同时加载当前登录用户和使用项目
     * by王健 at:2015-2-13
     * 改为主动撤销loading框
     * by:王健 at:2015-3-13
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        applicationContext = this;

        preferencesCookieStore = new PreferencesCookieStore(instance);
        BasicClientCookie cookie = new BasicClientCookie("test", "hello");
        cookie.setPath("/");
        preferencesCookieStore.addCookie(cookie);
        httpUtils = new HttpUtils();
        tempHttpUtils = new HttpUtils();
        httpUtils.configCookieStore(preferencesCookieStore);
//        db = DbUtils.create(this);

        //设置为 自动登录状态
//       by:王健 at:2015-3-11
//        EMChat.getInstance().setAutoLogin(false);
        loadInitUserInfo();



        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                // // 接收子线程的消息
                HttpResult result = (HttpResult)msg.obj;
                if (msg.what == 0) {
                    if(result.getDialog()==0){
//                        Toast.makeText(instance, result.getMessage(), Toast.LENGTH_SHORT).show();
                        ToastCustom.showMessage(instance, result.getMessage());
                    }
                    if(result.getDialog()==1){
                        AlertDialogCustom dialogCustom = new AlertDialogCustom(instance);
                        dialogCustom.show();
                        dialogCustom.setMessage(result.getMessage());
                        dialogCustom.setOnOKListener("好", new AlertDialogCustom.AlertDialogOKListener() {
                            @Override
                            public void onOKClick() {

                            }
                        });
                    }
                }
                if(msg.what == 1){
                    try {
                        switch (result.getStatus_code()){
                            case 0:             //正常
                                break;
                            case 404:             //未登录

                                Intent intent = new Intent(activityList.get(activityList.size()-1), Login.class);
                                activityList.get(activityList.size()-1).startActivity(intent);
                                activityList.get(activityList.size()-1).finish();
                                break;
                            case 2:             //不是一个项目
                                showMessage(result);
                                break;
                            case 3:             //需要username/password
                                showMessage(result);
                                break;
                            case 4:             //需要username/password
                                showMessage(result);
                                break;
                            case 401:             //error
                                showMessage(result);
                                break;
                            case 5:             //用户被禁用
                                showMessage(result);
                                Intent i1 = new Intent(activityList.get(activityList.size()-1), Login.class);
                                activityList.get(activityList.size()-1).startActivity(i1);
                                activityList.get(activityList.size()-1).finish();
                                break;
                            case 6:             //用户不在项目成员里
                                showMessage(result);
                                break;
                            case 7:             //项目余额不足
                                showMessage(result);
                                break;
                            case 8:             //权限不足
                                showMessage(result);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(msg.what==2){
                    if(mypDialog!=null && mypDialog.isShowing()){
                        mypDialog.dismiss();
                    }
                }
            }
        };

    }


    /**
     * 加载当前的用户和项目
     * by:王健 at:2015-2-13
     */
    public static void loadInitUserInfo(){
        if(Convert.currentUser == null && getUidCurrent()>0){
            try {
                Convert.currentUser = db.findById(UserInfo.class, getUidCurrent());
            } catch (DbException e) {
                e.printStackTrace();
            }


        }
    }

    /**
     * 加载 图片加载工具
     * by王健 at:2015-2-2
     * 设置新的 配置参数
     * by王健 at:2015-2-13
     * 添加 空图片的默认图片
     * by王健 at:2015-3-6
     * 改为 图片imageview 自己设置 默认背景图片，用新加载的图片 覆盖背景
     * by王健 at:2015-3-11
     * 修改图片缓存
     * by王健 at:2015-3-12
     * 增加 默认用户头像配置
     * by王健 at:2015-3-13
     * @param context
     */
    public static void initImageLoader(Context context) {


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(0)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
//        File cacheDir = StorageUtils.getCacheDirectory(context);
        File picPath=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"yourneed"+File.separator+"files");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiscCache(picPath)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(options) // default
                .writeDebugLogs()
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }


    /**
     *  遍历所有Activity并finish
     * by:王健 at:2015-1-21
     */
    public void exit() {

        httpUtils.send(HttpRequest.HttpMethod.GET, Convert.hosturl + "ns/logout", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                for (Activity activity : activityList) {
                    activity.finish();
                }
                System.exit(0);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                for (Activity activity : activityList) {
                    activity.finish();
                }
                System.exit(0);
            }
        });

    }

    /**
     * 访问远程服务， 可自定义 请求方法
     * by:王健 at:2015-1-16
     * 获得积分时，Toast提醒
     * by:王健 at:2015-2-12
     * 增加dofailure方法
     * by黄海杰at:2015-2-25
     * 改为主动撤销loading框
     * by:王健 at:2015-3-13
     * @param method
     * @param url
     * @param params
     * @param resultBack
     */
    public static void httpservice(HttpRequest.HttpMethod method,String url,RequestParams params,final HttpCallResultBackBase resultBack){
//        if(mypDialog==null||!mypDialog.isShowing()){
//            new Handler().postDelayed(new Runnable(){
//                public void run() {
//                    if(loading){
//                        showDialog();
//                    }
//
//                }
//            }, 2000);
//
//        }
//        loading = true;
        Log.e("http_api", url);
        SellApplication.httpUtils.send(method, Convert.hosturl + url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                HttpResult result = new HttpResult(responseInfo.result);
                resultBack.doresult(result);
                if(resultBack.isLoading_auto()){
                    hideDialog();
                }


                if(result.getJifen()>0){
                    Toast t =Toast.makeText(getInstance(), result.getJifen_msg(), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP, 0, 10);
                    t.show();
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(instance, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
                resultBack.dofailure();
                if(resultBack.isLoading_auto()){
                    hideDialog();
                }
            }
        });
    }

    /**
     * 访问远程 http请求 post
     * by:王健 at:2015-1-16
     * @param url
     * @param params
     * @param resultBack
     */
    public static void post(String url,RequestParams params,final HttpCallResultBackBase resultBack){
        httpservice(HttpRequest.HttpMethod.POST, url, params, resultBack);

    }

    /**
     * 访问远程 使用 新设计（HttpCallResultBackBase）
     * by:王健 at:2015-3-7
     * @param resultBack
     */
    public static void post(HttpCallResultBackBase resultBack){
        httpservice(HttpRequest.HttpMethod.POST, resultBack.getUrl(), resultBack.getParams(), resultBack);

    }

    public static String post_sync(HttpCallResultBackBase resultBack){
        Log.e("http_api", resultBack.getUrl());
        try {
            ResponseStream responseStream =  SellApplication.httpUtils.sendSync(HttpRequest.HttpMethod.POST, Convert.hosturl + resultBack.getUrl(), resultBack.getParams());
            try {
                return responseStream.readString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (HttpException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 访问远程 http请求 get
     * by:王健 at:2015-1-16
     * @param url
     * @param params
     * @param resultBack
     */
    public static void get(String url,RequestParams params,final HttpCallResultBackBase resultBack){
        httpservice(HttpRequest.HttpMethod.GET, url, params, resultBack);

    }

    /**
     * 下载数据
     * by:王健 at:2015-1-31
     * @param url
     * @param path
     * @param resultBack
     */
    public static void download(String url,String path,final RequestCallBack<File> resultBack){
        SellApplication.httpUtils.download(url,path,true,false,resultBack);

    }

    /**
     * 根据dialog 显示提示信息
     * by:王健 at:2015-1-16
     * 优化多线程造成的 toast问题
     * by:王健 at:2015-3-12
     * 解决toast 提示，没显示bug
     * by:王健 at:2015-3-13
     * @param result
     */
    public static void showMessage(HttpResult result){
        handler.obtainMessage(0, result).sendToTarget();
    }

    /**
     * 失败后 根据status_code 进行相应处理
     * by:王健 at:2015-1-16
     * 优化多线程造成的 toast问题
     * by:王健 at:2015-3-12
     * 解决toast 提示，没显示bug
     * by:王健 at:2015-3-13
     * @param result
     */
    public static void failureResult(HttpResult result){
        handler.obtainMessage(1, result).sendToTarget();

    }


    /**
     * 保存用户登录的id
     * by王健 at 2015-1-16
     * 根据uid 设置数据库名称
     * by王健 at 2015-2-13
     * @param uid
     */
    public static void saveLoginUser(int uid){
        if(db==null){
            db = DbUtils.create(instance, "need"+uid+".db");
        }else{
            if(!db.getDaoConfig().getDbName().equals("need"+uid+".db")){
                db = DbUtils.create(instance, "need"+uid+".db");
            }
        }
        SharedPreferences sp = instance.getSharedPreferences(SellApplication.USER_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("uid", uid);
        editor.commit();
    }


    /**
     * 通过id获取用户信息
     * by王健 at：2015-1-13
     * @param uid
     * @return
     */
    public static UserInfo getUserInfoIdByUid(int uid){
        UserInfo user = null;
        try {
            user = SellApplication.db.findById(UserInfo.class,uid);
        } catch (DbException e) {
            e.printStackTrace();
        }

        return user;
    }
    /**
     * 获取当前用户id
     * by王健 at：2015-1-16
     * 根据 uid的不同 加载数据库名称
     * by王健 at：2015-2-13
     * @return
     */
    public static int getUidCurrent(){
        SharedPreferences sp = instance.getSharedPreferences(SellApplication.USER_LOGIN, MODE_PRIVATE);
        int uid = sp.getInt("uid",0);
        if(db==null){
            db = DbUtils.create(instance, "need"+uid+".db");
        }else{
            if(!db.getDaoConfig().getDbName().equals("need"+uid+".db")){
                db = DbUtils.create(instance, "need"+uid+".db");
            }
        }
        return uid;
    }

    /**
     * 显示一个 加载loading
     * by王健 at：2015-1-16
     */
    public static void showDialog(Context context){
        showDialog(instance.getResources().getString(R.string.app_name),"正在获取数据……", context);
    }

    /**
     * 自定义 标题 和 message 的loading
     * by王健 at：2015-1-16
     * @param title
     * @param message
     */
    public static void showDialog(String title, String message, Context context){
        if(mypDialog!=null && mypDialog.isShowing()){
            mypDialog.dismiss();
        }
        mypDialog=new ProgressDialog(context);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mypDialog.setTitle(title);
        mypDialog.setMessage(message);
        mypDialog.setIcon(R.drawable.ic_launcher);
        mypDialog.setIndeterminate(false);
        mypDialog.setCancelable(true);
        try{
            mypDialog.show();
        }catch (Exception e){
            mypDialog.dismiss();
        }

    }

    /**
     * 隐藏dialog
     * by王健 at：2015-1-16
     * 改为主动撤销loading框
     * by:王健 at:2015-3-13
     */
    public static void hideDialog(){
        handler.obtainMessage(2).sendToTarget();
    }
    /**
     * 同步一下cookie
     * by:王健 at:2015-3-31
     * 优化同步cookie
     * by王健 at:2015-3-31
     */
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        List<Cookie> cookies = httpUtils.getCookieStore().getCookies();
        StringBuffer sb = new StringBuffer();
        String domain = null;
        for(Cookie ck : cookies){
            if(!"sessionid".equals(ck.getName().toLowerCase())){
                continue;
            }
            sb.append(ck.getName());
            sb.append("=");
            sb.append(ck.getValue());
//            sb.append("&");
            if(domain!=ck.getDomain()){
                domain = ck.getDomain();
            }

        }
        sb.append(";domain=");
        sb.append(domain);
        sb.append(";path=/");
        cookieManager.setCookie(url, sb.toString());//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }
}