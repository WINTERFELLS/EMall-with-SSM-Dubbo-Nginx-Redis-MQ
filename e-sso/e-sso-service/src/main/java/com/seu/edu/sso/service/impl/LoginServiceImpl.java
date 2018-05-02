package com.seu.edu.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.seu.edu.common.jedis.JedisClient;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.common.utils.JsonUtils;
import com.seu.edu.mapper.TbUserMapper;
import com.seu.edu.pojo.TbUser;
import com.seu.edu.pojo.TbUserExample;
import com.seu.edu.pojo.TbUserExample.Criteria;
import com.seu.edu.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public EResult userLogin(String username, String password) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list == null || list.size() == 0) {
			return EResult.build(400, "用户名或密码错误");
		}
		
		TbUser user = list.get(0);
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return EResult.build(400, "用户名或密码错误");
		}
		
		String token = UUID.randomUUID().toString();
		
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		jedisClient.expire("SESSION:"+token, 1800);
		
		return EResult.ok(token);
	}

}
