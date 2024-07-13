package com.tan.service;

import com.tan.dto.LoginDTO;
import com.tan.dto.RegisterDTO;
import com.tan.dto.SaveUserDTO;
import com.tan.dto.UpdateUserDTO;
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
}
