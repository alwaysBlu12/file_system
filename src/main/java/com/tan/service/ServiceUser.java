package com.tan.service;

import com.tan.dto.LoginDTO;
import com.tan.dto.RegisterDTO;
import com.tan.utils.EntityResult;

public interface ServiceUser {
    EntityResult login(LoginDTO loginDTO);

    EntityResult register(RegisterDTO registerDTO);
}
