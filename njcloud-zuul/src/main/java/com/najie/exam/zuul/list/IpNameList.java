package com.najie.exam.zuul.list;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc IP名单抽象类
 * @author admin
 *
 */
public abstract class IpNameList {
    //IP名单集合
	private List<String> nameList;
	//是否白名单
    private boolean isWhite;
    
    public IpNameList(boolean isWhite) {
        super();
        this.iniClass(isWhite);
    }

    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        nameList.clear();
        nameList = null;
    }

    public boolean isWhite() {
        return isWhite;
    }
    
    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public List<String> getNameList() {
        return nameList;
    }
    
    private void iniClass(boolean isWhite) {
        this.isWhite = isWhite;
        nameList = new ArrayList();
//        nameList = this.getNameList();
    }
    public int getListCount(){
        if (nameList == null) {
            return 0;
        }
        return this.nameList.size();
    }

    /**
     * @param ip 客户端IP
     * @return 客户端IP是否存在于名单中
     */
    public boolean isAllow(String ip) {
        if (this.nameList.isEmpty())
            return false;
        else {
            return this.nameList.indexOf(ip) > -1 ? true : false;
        }
    }
    
}
