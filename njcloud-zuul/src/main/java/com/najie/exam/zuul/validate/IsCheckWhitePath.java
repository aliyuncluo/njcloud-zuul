package com.najie.exam.zuul.validate;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc 判断是否进行白名单IP验证
 * @author admin
 *
 */
public class IsCheckWhitePath {

    public IsCheckWhitePath() {
        super();
        iniClass();
    }

    private List<String> list;

    private void iniClass() {
        list = new ArrayList<>();
        list.add("-wip-");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        list.clear();
        list = null;
    }

    /**
     *
     * */
    public boolean isCheckWhite(String _Path) {
        if (_Path == null) {
            return false;
        } else {
            boolean isCheck = false;
            for (String row : list) {
                isCheck = isCheck || (_Path.indexOf(row) > -1 ? true : false);
            }
            return isCheck;
        }
    }
}
