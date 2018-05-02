package com.seu.edu.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.mapper.TbUserMapper;
import com.seu.edu.pojo.TbUser;
import com.seu.edu.pojo.TbUserExample;
import com.seu.edu.pojo.TbUserExample.Criteria;
import com.seu.edu.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	TbUserMapper userMapper;
	
	@Override
	public EResult checkDate(String param, int type) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if(type == 1) {
			criteria.andUsernameEqualTo(param);
		}else if(type == 2) {
			criteria.andPhoneEqualTo(param);
		}else if(type == 3) {
			criteria.andEmailEqualTo(param);
		}else {
			return EResult.build(400, "数据类型错误");
		}
		
		List<TbUser> list = userMapper.selectByExample(example);
		if(list != null && list.size() > 0 ) {
			return EResult.ok(false);
		}
		return EResult.ok(true);
	}

	@Override
	public EResult register(TbUser user) {
		
		if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPhone())) {
			return EResult.build(400, "用户数据不完整");
		}
		
		EResult result = checkDate(user.getUsername(), 1);
		if(!(boolean) result.getData()) {
			return EResult.build(400, "此用户名已占用");
		}
		result = checkDate(user.getPhone(), 2);
		if(!(boolean) result.getData()) {
			return EResult.build(400, "此手机号已占用");
		}
		
		user.setCreated(new Date());
		user.setUpdated(new Date());
		
		String md5PassWord = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5PassWord);
		
		userMapper.insert(user);
		
		return EResult.ok();
	}

}
