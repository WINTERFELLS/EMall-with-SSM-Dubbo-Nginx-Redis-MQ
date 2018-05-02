package com.seu.edu.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seu.edu.common.jedis.JedisClient;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.common.utils.JsonUtils;
import com.seu.edu.pojo.TbUser;
import com.seu.edu.sso.service.TokenService;
@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public EResult getUserByToken(String token) {
		
		String json = jedisClient.get("SESSION:"+token);
		
		if(StringUtils.isBlank(json)) {
			return EResult.build(201, "用户登录已过期");
		}
		
		jedisClient.expire("SESSION"+token, 1800);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		
		return EResult.ok(user);
	}

}
