package com.seu.edu.sso.service;

import com.seu.edu.common.utils.EResult;
import com.seu.edu.pojo.TbUser;

public interface RegisterService {
	EResult checkDate(String param, int type);
	EResult register(TbUser user);
}
