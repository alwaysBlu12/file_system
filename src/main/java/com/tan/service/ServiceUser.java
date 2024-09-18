package com.tan.service;

import com.tan.dto.*;
import com.tan.entity.EntityResult;

public interface ServiceUser {
    EntityResult login(LoginDTO loginDTO);

    EntityResult register(RegisterDTO registerDTO);

    EntityResult getUserInfo();

    EntityResult list();

    EntityResult save(SaveUserDTO saveUserDTO);

    EntityResult update(UpdateUserDTO updateUserDTO);

    EntityResult deleteById(Integer id);

    EntityResult logout();

    EntityResult updatePwd(UpdatePwdDTO updatePwdDTO);
}
