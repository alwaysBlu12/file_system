package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tan.dto.SaveSpaceDTO;
import com.tan.entity.EntityResult;
import com.tan.entity.EntitySpace;
import com.tan.mapper.MapperSpace;
import com.tan.service.ServiceSpace;
import com.tan.utils.UserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceSpaceImpl implements ServiceSpace {

    @Resource
    private MapperSpace mapperSpace;

    @Override
    public EntityResult list() {
        Integer userId = UserThreadLocal.get().getUserId();
        List<EntitySpace> list = mapperSpace.list(userId);
        return EntityResult.success(list);
    }

    /**
     * 根据spaceId获取空间信息
     * @param spaceId
     * @return
     */
    @Override
    public EntityResult getById(Integer spaceId) {
       EntitySpace entitySpace =  mapperSpace.getBySpaceId(spaceId);
       return EntityResult.success(entitySpace);
    }

    /**
     * 添加空间
     *
     * @param saveSpaceDTO
     * @return
     */
    @Override
    public EntityResult save(SaveSpaceDTO saveSpaceDTO) {
        EntitySpace entitySpace = new EntitySpace();
        BeanUtil.copyProperties(saveSpaceDTO, entitySpace);
        //用户id
        Integer userId = UserThreadLocal.get().getUserId();
        entitySpace.setUserId(userId);
        //默认生成一个10Mb的空间,单位是字节
        entitySpace.setTotalSpace(10000000L);
        entitySpace.setUsedSpace(0L);
        entitySpace.setFileCount(0);
        entitySpace.setCreateTime(LocalDateTime.now());
        mapperSpace.save(entitySpace);
        return EntityResult.success();
    }
}
