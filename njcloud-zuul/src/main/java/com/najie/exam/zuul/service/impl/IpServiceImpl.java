package com.najie.exam.zuul.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.najie.exam.zuul.dao.IpBaseMapper;
import com.najie.exam.zuul.service.IpService;

@Service
public class IpServiceImpl implements IpService{

	    @Autowired
	    IpBaseMapper ipBaseMapper;

	    /**
	     * @return 得到域名列表，解决跨域问题
	     */
	    @Override
	    public List<String> getDomainList() {
	        return ipBaseMapper.getIpList("DO");
	    }

	    /**
	     * @return 得到白名单，对指定方法需要验证白名单
	     */
	    @Override
	    public List<String> getWhiteList() {
	        return ipBaseMapper.getIpList("WH");
	    }

	    /**
	     * @return 得到黑名单
	     */
	    @Override
	    public List<String> getBlackList() {
	        return ipBaseMapper.getIpList("BL");
	    }

}
