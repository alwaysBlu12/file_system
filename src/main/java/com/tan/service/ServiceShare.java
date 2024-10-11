package com.tan.service;

import com.tan.dto.CreateShareDTO;
import com.tan.entity.EntityResult;

import java.util.List;

public interface ServiceShare {
    /**
     * 创建共享文件
     * @param createShareDTO
     * @return
     */
    EntityResult create( CreateShareDTO createShareDTO);

    /**
     * 删除共享文件
     * @param shareId
     * @return
     */
    EntityResult deleteById(String shareId);

    EntityResult getSharedFileById(String shareId);

    EntityResult getAllSharedFilesByUserId(Integer userId);

    EntityResult checkPassword(String shareId, String password);
}
