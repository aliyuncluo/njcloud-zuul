package com.najie.exam.zuul.list;

/**
 * @desc IP白名单
 * @author admin
 *
 */
public class IpWhiteNameList extends IpNameList{

	public IpWhiteNameList() {
		super(true);
	}
	
	public IpWhiteNameList(boolean isWhite) {
		super(isWhite);
	}	

}
