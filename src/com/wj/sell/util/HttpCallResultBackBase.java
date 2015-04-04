package com.wj.sell.util;

import android.util.Log;
import com.lidroid.xutils.http.RequestParams;
import com.wj.sell.db.models.UserInfo;

/**
 * Created by wangjian on 15/3/7.
 */
public abstract class HttpCallResultBackBase implements HttpCallResultBack {

    private HttpCallResultBack httpCallResultBack;

    private UserInfo currentUser;

    private RequestParams params;

    private boolean loading_auto=true;

    public HttpCallResultBackBase(){

    }

    /**
     * 构造函数，放入 回调函数
     * by:王健 at:2015-3-7
     * @param httpCallResultBack
     */
    public HttpCallResultBackBase(HttpCallResultBack httpCallResultBack){
        this.httpCallResultBack = httpCallResultBack;
        this.setCurrentUser(Convert.currentUser);
    }

    protected abstract String http_api();
    protected abstract int queryTimeline();

    /**
     * 在获取url前，把timeline 插入param中
     * @return
     */
    public String getUrl() {
        queryTimeline();
        return http_api();
    }

    public RequestParams getParams() {
        return params;
    }

    /**
     * 处理返回结果
     * by:王健 at:2015-3-7
     * 添加监控日志
     * by:王健 at:2015-3-8
     * 输出参数 字符串
     * by:王健 at:2015-3-9
     * @param result
     */
    @Override
    public void doresult(HttpResult result) {
        //todo:执行 保存数据的操作
        if(result.getResultArr()!=null){
            String url = http_api();
            try{
                Log.e(url, "params:"+params.getQueryStringParams().toString());
            }catch (Exception e){

            }
            try{
                Log.e(url, "num:"+result.getResultArr().length()+"");
            }catch (Exception e){

            }


        }
        //todo:运行 接口
        if(httpCallResultBack!=null){
            httpCallResultBack.doresult(result);
        }
    }

    /**
     * 处理错误情况
     * by:王健 at:2015-3-7
     * 输出参数 字符串
     * by:王健 at:2015-3-9
     * 异常错误
     * by王健 at:2015-3-9
     */
    @Override
    public void dofailure() {
        //todo:执行 保存数据的操作
        String url = http_api();
        try{
            Log.e(url, "params:"+params.getQueryStringParams().toString());
        }catch (Exception e){

        }

        //todo:运行 接口
        if(httpCallResultBack!=null){
            httpCallResultBack.dofailure();
        }
    }

    public HttpCallResultBack getHttpCallResultBack() {
        return httpCallResultBack;
    }

    public void setHttpCallResultBack(HttpCallResultBack httpCallResultBack) {
        this.httpCallResultBack = httpCallResultBack;
    }


    public UserInfo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserInfo currentUser) {
        this.currentUser = currentUser;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    public boolean isLoading_auto() {
        return loading_auto;
    }

    public void setLoading_auto(boolean loading_auto) {
        this.loading_auto = loading_auto;
    }
}
