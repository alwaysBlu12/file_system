package com.tan.service;

import com.tan.dto.SaveSpaceDTO;
import com.tan.entity.EntityResult;

public interface ServiceSpace {
    EntityResult list();

    EntityResult getById(Integer spaceId);

    /**
     * 添加空间
     * @param saveSpaceDTO
     * @return
     */
    EntityResult save(SaveSpaceDTO saveSpaceDTO);
}
