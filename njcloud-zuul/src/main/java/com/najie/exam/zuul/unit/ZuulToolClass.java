package com.najie.exam.zuul.unit;

import java.util.ArrayList;
import java.util.List;

public class ZuulToolClass {

    /**
     * 是否为规定内的域名
     *
     * @param myOrigin 域名
     * @return
     */
    public static boolean getOriginValid(String myOrigin) {
        if (myOrigin == null || myOrigin.length() == 0) {
            return false;
        }
        List<String> list = new ArrayList<>();
        list.add("http://localhost:63342");
        list.add("http://www.lh.com");
        list.add("http://cp.lh.com:8080");
        list.add("http://cp.lh.com");
        list.add("http://cj.lh.com");
        list.add("http://wmj.lh.com");

        int i = list.indexOf(myOrigin);
        boolean isValid = i > -1 ? true : false;
        list.clear();
        return isValid;
    }
}
