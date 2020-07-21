package com.najie.exam.zuul.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface IpBaseMapper {

	/**
     * 得到域名、白名单、黑名单
     *
     * @param selectSign 查询标识
     * @return
     */
    List<String> getIpList(@Param("selectSign") String selectSign);
}
