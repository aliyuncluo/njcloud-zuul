package com.najie.exam.zuul.list;

import java.util.List;

/**
 * @desc 域名列表
 * @author admin
 *
 */
public class DomainNameList extends IpNameList{

	public DomainNameList() {
	   super(true);	
	}
	
	public DomainNameList(boolean isWhite) {
		super(isWhite);
	}

    @Override
    public int getListCount() {
        return super.getListCount();
    }

    @Override
    public boolean isAllow(String ip) {
        return super.isAllow(ip);
    }

    @Override
    public void setNameList(List<String> nameList) {
        super.setNameList(nameList);
    }

    @Override
    public List<String> getNameList() {
        return super.getNameList();
    }
    
}
