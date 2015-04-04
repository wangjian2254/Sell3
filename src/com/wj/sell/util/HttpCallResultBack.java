package com.wj.sell.util;

/**
 * Created by 黄海杰 on 2015/1/10.
 * 解析完成回调方法
 * 增加失败
 * by黄海杰 at:2015-2-25
 */
public interface HttpCallResultBack {

    public void doresult(HttpResult result);
    public void dofailure();
}
