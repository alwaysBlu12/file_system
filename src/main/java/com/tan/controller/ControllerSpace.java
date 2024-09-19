package com.tan.controller;

import com.tan.dto.SaveSpaceDTO;
import com.tan.dto.UpdateSpaceDTO;
import com.tan.entity.EntityResult;
import com.tan.entity.EntitySpace;
import com.tan.service.ServiceSpace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/space")
@RestController
@Slf4j
public class ControllerSpace {

    @Autowired
    private ServiceSpace serviceSpace;

    /**
     * 获取空间列表
     * @return
     */
    @GetMapping("/list")
    public EntityResult list(){
        return serviceSpace.list();
    }

    /**
     * 根据spaceId获取空间详细信息
     * @param spaceId
     * @return
     */
    @GetMapping
    public EntityResult getById(Integer spaceId){
        return serviceSpace.getById(spaceId);
    }

    /**
     * 添加空间
     * @param saveSpaceDTO
     * @return
     */
    @PostMapping
    public EntityResult save(@RequestBody SaveSpaceDTO saveSpaceDTO){
        return serviceSpace.save(saveSpaceDTO);
    }

    /**
     * 删除空间
     * @param spaceId
     * @return
     */
    @DeleteMapping
    public EntityResult deleteById(Integer spaceId){
        return serviceSpace.deleteById(spaceId);
    }

    /**
     * 更新空间
     * @param updateSpaceDTO
     * @return
     */
    @PutMapping
    public EntityResult update(@RequestBody UpdateSpaceDTO updateSpaceDTO){
        return serviceSpace.update(updateSpaceDTO);
    }

}
