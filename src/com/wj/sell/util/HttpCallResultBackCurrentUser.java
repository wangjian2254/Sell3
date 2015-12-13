package com.wj.sell.util;

import android.os.Handler;
import com.lidroid.xutils.exception.DbException;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell3.SellApplication;

/**
 * Created by huanghaijie on 15/3/8.
 */
public class HttpCallResultBackCurrentUser extends HttpCallResultBackBase {

    public HttpCallResultBackCurrentUser(HttpCallResultBack callResultBack) {
        super(callResultBack);

    }


    /**
     * 根据old 访问不同的 接口
     * @return
     */
    @Override
    protected String http_api() {
            return "app/current_user";
    }

    /**
     * 数据库查询timeline返回0
     * by黄海杰 at:2015-3-8
     * @return
     */
    @Override
    protected int queryTimeline(){
        return 0;
    }


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Handler handler;
    /**
     * 处理返回结果
     * by:黄海杰 at:2015-3-8
     * @param result
     */
    @Override
    public void doresult(HttpResult result) {
        if(result.isSuccess()){
            if(result.getResult()!=null){
                UserInfo user = new UserInfo(result.getResult());

                try {

                    SellApplication.db.replace(user);
                    if(Convert.currentUser==null){
                        Convert.currentUser = user;
                    }else{
                        Convert.currentUser.initUserInfo();
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        super.doresult(result);
    }

    @Override
    public void dofailure() {


        super.dofailure();
    }

}
