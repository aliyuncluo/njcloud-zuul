package com.najie.exam.zuul.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.najie.exam.zuul.model.TokenClass;

@Component
public class CheckAccessTokenClass {

    @Autowired
    RedisOperator redisOperator;

    public CheckAccessTokenClass() {
        super();
    }


    public boolean isAccessTokenOk(TokenClass tokenClass) {
        String useId = tokenClass.getUseId();
        String accessToken = tokenClass.getAccessToken();
        String useType = tokenClass.getUseType();
        String clientType = tokenClass.getClientType();
        if ((useId != null) && (accessToken != null) && (useType != null) && (clientType != null)) {
            return checkUseToken(clientType, useId, useType, accessToken);
        }
        return false;
        //return true;
    }

    /**
     * 从Redis中验证token
     *
     * @param clientType 客户端类型 BS或CS等
     * @param useId
     * @param useType
     * @param token
     * @return
     */
    private boolean checkUseToken(String clientType, String useId, String useType, String token) {
        String keyName = String.format("%s%s:%s", clientType, useType, useId);
        String accessToken = redisOperator.getString(keyName);
        if (accessToken != null) {
            return token.equals(accessToken);
        }
            return false;
    }

}
