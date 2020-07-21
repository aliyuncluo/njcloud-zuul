package com.najie.exam.zuul.service;

import java.util.List;

public interface IpService {

	List<String> getDomainList();
	List<String> getWhiteList();
	List<String> getBlackList();

}
