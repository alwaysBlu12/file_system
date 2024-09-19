package com.tan.service;

import com.tan.dto.SaveSpaceDTO;
import com.tan.dto.UpdateSpaceDTO;
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

    /**
     * 删除空间
     * @param spaceId
     * @return
     */
    EntityResult deleteById(Integer spaceId);

    /**
     * 更新空间
     * @param updateSpaceDTO
     * @return
     */
    EntityResult update(UpdateSpaceDTO updateSpaceDTO);
}
