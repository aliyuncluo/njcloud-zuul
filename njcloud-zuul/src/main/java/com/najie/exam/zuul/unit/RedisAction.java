package com.najie.exam.zuul.unit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisAction {

	private StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
	  
	  private TimeUnit timeUnit = TimeUnit.MINUTES;
	  
	  private long expirationTime = 10L;
	  
	  public void saveKeyAndValue(String key, String value) {
	    saveValue(key, value);
	  }
	  
	  public void saveKeyAndValue(String key, String value, long expirationTime, TimeUnit timeUnit) {
	    saveValueAndTime(key, value, expirationTime, timeUnit);
	  }
	  
	  public void saveValueAndTime(String key, String value) {
	    saveValueAndTime(key, value, this.expirationTime, this.timeUnit);
	  }
	  
	  private void saveValue(String key, String value) {
	    this.stringRedisTemplate.opsForValue().set(key, value);
	  }
	  
	  private void saveValueAndTime(String key, String value, long expirationTime, TimeUnit timeUnit) {
	    this.stringRedisTemplate.opsForValue().set(key, value, expirationTime, timeUnit);
	  }
	  
	  public String getValue(Object key) {
	    return (String)this.stringRedisTemplate.opsForValue().get(key);
	  }
	  
	  public void delete(String key) {
	    this.stringRedisTemplate.delete(key);
	  }
	  
	  public void deletel(List<String> keys) {
	    if (keys != null && keys.size() > 0)
	      for (String row : keys)
	        delete(row);  
	  }
	  
	  public TimeUnit getTimeUnit() {
	    return this.timeUnit;
	  }
	  
	  public void setTimeUnit(TimeUnit timeUnit) {
	    this.timeUnit = timeUnit;
	  }
	  
	  public long getExpirationTime() {
	    return this.expirationTime;
	  }
	  
	  public void setExpirationTime(Integer expirationTime) {
	    this.expirationTime = expirationTime.intValue();
	  }
	  
	  public StringRedisTemplate getStringRedisTemplate() {
	    return this.stringRedisTemplate;
	  }
	  
	  public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
	    this.stringRedisTemplate = stringRedisTemplate;
	  }
	  
	  public void saveToList(String listKey, long index, String value) {
	    this.stringRedisTemplate.opsForList().set(listKey, index, value);
	  }
	  
	  public long saveRightPush(String listKey, String value) {
	    return this.stringRedisTemplate.opsForList().rightPush(listKey, value).longValue();
	  }
	  
	  public List<String> getListAll(String listKey) {
	    return getList(listKey, 0L, -1L);
	  }
	  
	  public List<String> getList(String listKey, long begin, long end) {
	    return this.stringRedisTemplate.opsForList().range(listKey, begin, end);
	  }
	  
	  public long updateListByValue(String listKey, Object oldValue, Object newValue) {
	    if (oldValue == null || newValue == null)
	      return -1L; 
	    long index = 0L;
	    boolean finder = false;
	    List<String> listAll = getListAll(listKey);
	    if (listAll != null && !listAll.isEmpty())
	      while (!finder) {
	        if (oldValue.equals(listAll.get((int)index))) {
	          finder = true;
	          saveToList(listKey, index, newValue.toString());
	          break;
	        } 
	        index++;
	      }  
	    return finder ? index : -1L;
	  }
	  
	  public long deleteListByValue(String listKey, Object value) {
	    return this.stringRedisTemplate.opsForList().remove(listKey, 0L, value).longValue();
	  }
	  
	  public long clearList(String listKey) {
	    long count = 0L;
	    List<String> listAll = getListAll(listKey);
	    if (listAll != null)
	      for (String key : listAll) {
	        Long remove = this.stringRedisTemplate.opsForList().remove(listKey, 0L, key);
	        count = (count > remove.longValue()) ? count : remove.longValue();
	      }  
	    return count;
	  }
}
