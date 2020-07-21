package com.najie.exam.zuul.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @desc 得到域名、白名单、黑名单
 * @author admin
 *
 */
@Component
public class RedisOperator {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private int insertIntoRedisSet(String setName,List<String> nameList){
        if (nameList == null) {
            return 0;
        }
        SetOperations<String, String> set = stringRedisTemplate.opsForSet();
        for (String row: nameList
             ) {
            set.add(setName,row);
        }
        return nameList.size();
    }

    private List<String> getFormRedisSet(String setName){
        Set<String> members = stringRedisTemplate.opsForSet().members(setName);
        return new ArrayList(members);
    }

    public List<String> getDomainList(){
        return getFormRedisSet("domainSet");
    }

    public int insertDomain(List<String> domainList){
        return insertIntoRedisSet("domainSet",domainList);
    }

    public List<String> getBlackList(){
        return getFormRedisSet("blackSet");
    }

    public int insertBlack(List<String> blackList){
        return insertIntoRedisSet("blackSet",blackList);
    }

    public List<String> getWhiteList(){
        return getFormRedisSet("whiteSet");
    }

    public int insertWhite(List<String> whiteList){
        return insertIntoRedisSet("whiteSet",whiteList);
    }

    /**
     * 增加或修改指定键值
     *
     * @param key   键
     * @param value 值
     */
    public void saveString(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String getString(String key) {
        if (key != null)
            return stringRedisTemplate.opsForValue().get(key);
        else
            return null;
    }

    /**
    *@Description 删除指定的键值
    *@Author  luolei
    *@Date 2019/9/3 16:35
    *@Param
    *@Return
    *@Exception
    *
    **/
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
    *@Description 增加键值，过期时间
    *@Author  luolei
    *@Date 2019/9/3 16:40
    *@Param  
    *@Return  
    *@Exception 
    *
    **/
    public void saveStringExpire(String useId,String useType,String accessToken,String clientType){
        String key = String.format("%s%s:%s", useType, clientType, useId);
        RedisAction redisAction = new RedisAction();
        redisAction.setStringRedisTemplate(this.stringRedisTemplate);
        redisAction.saveKeyAndValue(key, accessToken, 1L, TimeUnit.DAYS);
    }

    /**
    *@Description  根据提供的键来查询相应的值
    *@Author  luolei
    *@Date 2019/9/3 16:49
    *@Param
    *@Return
    *@Exception
    *
    **/
    public String getValueByKey(String key){
      if(key==null || key.equals("")){
          return null;
      }
      RedisAction redisAction = new RedisAction();
      redisAction.setStringRedisTemplate(this.stringRedisTemplate);
      String value = redisAction.getValue(key);
      return value;
    }

    /**
    *@Description 保存到集合
    *@Author  luolei
    *@Date 2019/9/3 17:41
    *@Param  nameList  键
    *@param  list  集合
    *@Return  
    *@Exception 
    *
    **/
    public void insertListData(String nameList,List<String> list){
        if(list!=null && list.size()>0 && !StringUtils.isEmpty(nameList)){
            for(int i=0;i<list.size();i++){
                RedisAction redisAction = new RedisAction();
                redisAction.setStringRedisTemplate(this.stringRedisTemplate);
                redisAction.saveToList(nameList,i,list.get(i)); 
            }
            
        }
        
    }
	
}
