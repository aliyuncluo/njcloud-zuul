package com.najie.exam.zuul.model;
/**
 * @desc token类型
 * @author admin
 *
 */
public class TokenClass {
  
	 private String useId;
	 private String useType;
	 private String clientType;
	 private String accessToken;
	public String getUseId() {
		return useId;
	}
	public void setUseId(String useId) {
		this.useId = useId;
	}
	public String getUseType() {
		return useType;
	}
	public void setUseType(String useType) {
		this.useType = useType;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	 
	 
}
