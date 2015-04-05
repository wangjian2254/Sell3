package com.wj.sell.util;

import android.os.Handler;
import com.lidroid.xutils.exception.DbException;
import com.wj.sell.db.models.Shiming;
import com.wj.sell3.SellApplication;

/**
 * Created by huanghaijie on 15/3/8.
 */
public class HttpCallResultBackShimingQuery extends HttpCallResultBackBase {

    public HttpCallResultBackShimingQuery(HttpCallResultBack callResultBack) {
        super(callResultBack);

    }


    /**
     * 根据old 访问不同的 接口
     * @return
     */
    @Override
    protected String http_api() {
            return "app/query_real_name_state";
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
        if(result.getResult()!=null){
            Shiming shiming = new Shiming(result.getResult());
            if(result.getResult().has("state")){
                shiming.setSuccess(result.getResult().optInt("state"));
            }
            try {
                SellApplication.db.saveOrUpdate(shiming);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }

        super.doresult(result);
    }

    @Override
    public void dofailure() {


        super.dofailure();
    }

}
