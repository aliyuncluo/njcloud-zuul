package com.najie.exam.zuul.list;

/**
 * @desc IP黑名单
 * @author admin
 *
 */
public class IpBlackNameList extends IpNameList{

	public IpBlackNameList() {
		super(true);
	}
	
	public IpBlackNameList(boolean isWhite) {
		super(isWhite);
	}

}
