package com.najie.exam.zuul.model;
/**
 * @desc 返回报文
 * @author admin
 *
 */
public class ReturnModel {
    public ReturnModel() {
        super();
        isok = false;
        message = "请求失败";
        count = 0;
        data = null;
    }
    public boolean isok;
    public String message;
    public int count;
    public String data;
    public void setSuccess(){
        this.message = "存在合法身份";
    }
}
