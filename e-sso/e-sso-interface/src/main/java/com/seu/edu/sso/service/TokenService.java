package com.seu.edu.sso.service;

import com.seu.edu.common.utils.EResult;

public interface TokenService {
	EResult getUserByToken(String token);
}
