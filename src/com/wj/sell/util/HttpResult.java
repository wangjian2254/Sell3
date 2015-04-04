package com.wj.sell.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangjian on 15/1/16.
 */
public class HttpResult {
    private int status_code=4;
    private boolean success=false;
    private String message="";
    private JSONObject result=null;
    private JSONArray resultArr=null;
    private int result_int = -1;
    private int dialog=0;
    private int jifen=0;
    private String jifen_msg="";

    /**
     * 获取积分，和 积分信息
     * by:王健 at:2015-2-12
     * @param rs
     */
    public HttpResult(String rs){
        try {
            JSONObject r = new JSONObject(rs);
            status_code = r.optInt("status_code", 4);
            success = r.optBoolean("success", false);
            message = r.optString("message", "没获取到信息");
            dialog = r.optInt("dialog", 0);
            result = r.optJSONObject("result");
            resultArr = r.optJSONArray("result");
            jifen = r.optInt("jifen");
            jifen_msg = r.optString("jifen_msg");
            if(result==null&&resultArr==null){
                result_int = r.optInt("result");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDialog() {
        return dialog;
    }



    public int getStatus_code() {
        return status_code;
    }



    public boolean isSuccess() {
        return success;
    }



    public String getMessage() {
        return message;
    }


    public JSONObject getResult() {
        return result;
    }

    public JSONArray getResultArr() {
        return resultArr;
    }

    public int getJifen() {
        return jifen;
    }

    public void setJifen(int jifen) {
        this.jifen = jifen;
    }

    public String getJifen_msg() {
        return jifen_msg;
    }

    public void setJifen_msg(String jifen_msg) {
        this.jifen_msg = jifen_msg;
    }

    public int getResult_int() {
        return result_int;
    }

    public void setResult_int(int result_int) {
        this.result_int = result_int;
    }
}
