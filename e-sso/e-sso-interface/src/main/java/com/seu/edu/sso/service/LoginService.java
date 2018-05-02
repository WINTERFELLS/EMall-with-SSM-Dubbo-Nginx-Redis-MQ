package com.seu.edu.sso.service;

import com.seu.edu.common.utils.EResult;

public interface LoginService {
	EResult userLogin(String username, String password);
}
