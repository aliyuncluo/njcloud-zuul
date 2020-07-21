package com.najie.exam.zuul.filter;

import com.google.gson.Gson;
import com.najie.exam.zuul.enums.EnumClass;
import com.najie.exam.zuul.enums.OperateTypeClass;
import com.najie.exam.zuul.list.IpBlackNameList;
import com.najie.exam.zuul.list.IpWhiteNameList;
import com.najie.exam.zuul.model.ReturnModel;
import com.najie.exam.zuul.model.TokenClass;
import com.najie.exam.zuul.service.IpService;
import com.najie.exam.zuul.unit.CheckAccessTokenClass;
import com.najie.exam.zuul.unit.RedisOperator;
import com.najie.exam.zuul.unit.ZuulToolClass;
import com.najie.exam.zuul.validate.IsCheckWhitePath;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORWARD_TO_KEY;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @function 身份认证过滤器
 * @desc 过滤IP黑名单和白名单，判断是BS还是CS，判断是否需要身份验证
 * @author admin
 *
 */
public class AccessTokenFilter extends ZuulFilter{
	 Logger logger = LoggerFactory.getLogger(AccessTokenFilter.class); 
	
	 private final String TokenName = "accessToken";
	 private final String UseId = "useId";
	 private final String UseType = "useType";
	 private final String ClientType = "clientType";
	 
	 private IpWhiteNameList ipWhiteNameList;
	 private IpBlackNameList ipBlackNameList;
	 
	 @Autowired
	 Gson gson;
	 @Autowired
	 IpService ipService;
	 @Autowired
	 CheckAccessTokenClass checkAccessTokenClass;
	 @Autowired
	 RedisOperator redisOperator;
	    
	 public AccessTokenFilter() {
	        super();
	        iniFilter();
	 }
	 
	 public void iniFilter() {
	        ipBlackNameList = new IpBlackNameList();
	        ipWhiteNameList = new IpWhiteNameList();
	 }

	 
	    
	@Override
	public Object run() throws ZuulException {
	        ReturnModel returnModel = new ReturnModel();
	        returnModel.message = String.format("%s:无合法通行证，不允许访问！", returnModel.message);
	        int nStatusCode = 401;
	        RequestContext ctx = RequestContext.getCurrentContext();
	        HttpServletRequest request = ctx.getRequest();
	        HttpServletResponse response = ctx.getResponse();

	        String useIp = request.getRemoteAddr();

	        if (ipBlackNameList.isAllow(useIp)) {
	            returnModel.isok = true;
	            ipWhiteNameList = new IpWhiteNameList();
	            String getRequestURI = request.getRequestURI();
	            IsCheckWhitePath isCheckWhitePath = new IsCheckWhitePath();

	            if (isCheckWhitePath.isCheckWhite(getRequestURI)) {
	                returnModel.isok = ipWhiteNameList.isAllow(useIp);
	                if (!returnModel.isok) {
	                    returnModel.message = String.format("%s:您的IP(%s)未在白名单中，不允许访问！", returnModel.message, useIp);
	                }
	            }

	            if (returnModel.isok) {
	                returnModel = new ReturnModel();
	                OperateTypeClass operateTypeClass = new OperateTypeClass();
	                EnumClass.CheckIdentityEnum checkIdentityEnum = operateTypeClass.GetOperateType(getRequestURI);
	                switch (checkIdentityEnum) {
	                    case IS_CS:
	                    case IS_ANDROID:
	                    case IS_IOS: {
	                        Object accessToken = request.getParameter(TokenName);
	                        Object useId = request.getParameter(UseId);
	                        Object useType = request.getParameter(UseType);
	                        Object clientType = request.getParameter(ClientType);
	                        if ((accessToken != null) && (useId != null) && (useType != null)) {
	                            TokenClass tokenClass = new TokenClass();
	                            tokenClass.setUseId(useId.toString());
	                            tokenClass.setAccessToken(accessToken.toString());
	                            tokenClass.setUseType(useType.toString());
	                            tokenClass.setClientType(clientType.toString());
	                            returnModel.isok = checkAccessTokenClass.isAccessTokenOk(tokenClass);
	                            if (returnModel.isok) {
	                                returnModel.setSuccess();
	                            }
	                        }
	                    }
	                    break;
	                    case IS_BS: {
	                        Cookie[] cookies = request.getCookies();
	                        returnModel.isok = cookies == null ? false : true;
	                        if (returnModel.isok) {
	                            String useId = null;
	                            String accessToken = null;
	                            String useType = null;
	                            String clientType = null;
	                            for (Cookie row : cookies
	                                    ) {
	                                if (row.getName().equals(TokenName)) {
	                                    accessToken = row.getValue();
	                                }
	                                if (row.getName().equals(UseId)) {
	                                    useId = row.getValue();
	                                }
	                                if (row.getName().equals(UseType)) {
	                                    useType = row.getValue();
	                                }
	                                if (row.getName().equals(ClientType)) {
	                                    clientType = row.getValue();
	                                }
	                            }
	                            if (returnModel.isok && accessToken != null && useId != null && useType != null && clientType != null) {
	                                TokenClass tokenClass = new TokenClass();
	                                tokenClass.setUseId(useId);
	                                tokenClass.setAccessToken(accessToken);
	                                tokenClass.setUseType(useType);
	                                tokenClass.setClientType(clientType);
	                                returnModel.isok = checkAccessTokenClass.isAccessTokenOk(tokenClass);
	                                if (returnModel.isok) {
	                                    returnModel.setSuccess();
	                                    request.getParameterMap();
	                                    Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();
	                                    if (requestQueryParams != null) {
	                                        if (!requestQueryParams.containsKey(UseId)) {
	                                            List<String> list = new ArrayList<>();
	                                            list.add(useId);
	                                            requestQueryParams.put(UseId, list);
	                                        }
	                                        if (!requestQueryParams.containsKey(UseType)) {
	                                            List<String> list = new ArrayList<>();
	                                            list.add(useType);
	                                            requestQueryParams.put(UseType, list);
	                                        }
	                                        if (!requestQueryParams.containsKey(ClientType)) {
	                                            List<String> list = new ArrayList<>();
	                                            list.add(clientType);
	                                            requestQueryParams.put(ClientType, list);
	                                        }
	                                    } else {
	                                        List<String> listUseId = new ArrayList<>();
	                                        listUseId.add(useId);
	                                        List<String> listUseType = new ArrayList<>();
	                                        listUseType.add(useType);
	                                        List<String> listClientType = new ArrayList<>();
	                                        listClientType.add(clientType);
	                                        requestQueryParams.put(UseId, listUseId);
	                                        requestQueryParams.put(UseType, listUseType);
	                                        requestQueryParams.put(ClientType, listClientType);
	                                    }
	                                    ctx.setRequestQueryParams(requestQueryParams);
	                                }
	                            }
	                        }
	                        if (!returnModel.isok) {
	                            break;
	                        }
	                    }
	                    case IS_NO: {
	                        String myOrigin = request.getHeader("origin");
	                        logger.info("NO:" + myOrigin);
	                        returnModel.isok = ZuulToolClass.getOriginValid(myOrigin);
	                        if (returnModel.isok) {
	                            response.setHeader("Access-Control-Allow-Origin", myOrigin);
	                            response.setHeader("Access-Control-Allow-Method", "OPTIONS, TRACE, GET, HEAD, POST, PUT");
	                            response.setHeader("Access-Control-Allow-Credentials", "true");
	                            response.setContentType("application/json;text/html;charset=UTF-8");
	                            response.setCharacterEncoding("UTF-8");
	                        }
	                    }
	                    break;
	                    case IS_WEIXIN_PUBLIC:
	                        break;
	                    case IS_WEIXIN_SMALLPROGRAME:
	                        break;
	                    case IS_LOCALREMOTE:
	                        break;
	                    default:
	                        break;
	                }
	            }
	        } else {
	            returnModel.message = String.format("%s:您的IP(%s)已进入黑名单，不允许访问！", returnModel.message, useIp);
	        }

	        if (returnModel.isok) {
	            if (!useIp.equals("192.168.1.123")) {
	 //               RibbonFilterContextHolder.getCurrentContext().add("version", "1");
	            } else {
	 //               RibbonFilterContextHolder.getCurrentContext().add("version", "2");
	            }
	        } else {
	            String resultBody = "<div class=\"top\">";
	            resultBody += "<form name=\"userLoginActionForm\" id=\"userLoginActionForm\" method=\"POST\" action=\"\" target=\"_parent\">";
	            resultBody += "<input type=\"text\" autofocus=\"true\" id=\"username\" name=\"username\" maxlength=\"20\" placeholder=\"帐号\"";
	            resultBody += "onkeydown=\"UserEnter(event)\" onfocus=\"hideVcode()\"/>";
	            resultBody += "<input type=\"password\" id=\"userpwd\" name=\"userpwd\" maxlength=\"20\" placeholder=\" 密码\" ";
	            resultBody += "onkeydown=\"PassEnter(event)\"/>";
	            resultBody += "<input type=\"text\" id=\"validatecode\"  placeholder=\" 验证码\"";
	            resultBody += "onkeydown=\"ValidateCodeEnter(event)\">";
	            resultBody += "<img id=\"vcodesrc\" onclick=\"updateValidateImage()\" src=\"\"  style=\"display: none\">";
	            resultBody += "<input type=\"button\" value=\"\" id=\"login_bt\" name=\"login_bt\"/>";
	            resultBody += "<a href=\"\" class=\"forget\">忘记密码</a>";
	            resultBody += "</form>";
	            resultBody += "</div>";
	            ctx.setSendZuulResponse(false);
	            ctx.setResponseStatusCode(nStatusCode);
	            ctx.getResponse().setContentType("text/html;charset=UTF-8");
	            ctx.setResponseBody(resultBody);
	        }
	        return null;
	}

	/**
	 * @desc 返回一个boolean类型来判断该过滤器是否要执行  为true，说明需要过滤
	 */
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
        return !ctx.containsKey(FORWARD_TO_KEY)
                && !ctx.containsKey(SERVICE_ID_KEY);
	}

	/**
	 * @desc 通过int值来定义过滤器的执行顺序  优先级为0，数字越大，优先级越低
	 */
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * @desc 前置过滤器   pre：可以在请求被路由之前调用
	 */
	@Override
	public String filterType() {
		return "pre";
	}

}
