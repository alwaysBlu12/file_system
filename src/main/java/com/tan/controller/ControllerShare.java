package com.tan.controller;

import com.tan.dto.CreateShareDTO;
import com.tan.entity.EntityResult;
import com.tan.mapper.MapperShare;
import com.tan.service.ServiceShare;
import com.tan.utils.UserThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/share")
@Slf4j
public class ControllerShare {

    @Resource
    private ServiceShare serviceShare;


    /**
     * 创建共享文件---时间单位是天
     * createShareDTO
     * @return
     */
    @PostMapping
    public EntityResult createShareFile(@RequestBody CreateShareDTO createShareDTO){
        return serviceShare.create(createShareDTO);
    }

    /**
     * 删除共享文件
     * @param shareId
     * @return
     */
    @DeleteMapping
    public EntityResult deleteShareFile(String shareId){
        return serviceShare.deleteById(shareId);
    }

    /**
     * 根据 shareId 查看共享文件-->这里需要一个联表查询-->文件名\大小\路径\过期时间,文件id和共享id也传出去,后面如果有用
     * @param shareId
     * @return
     */
    @GetMapping("/{shareId}")
    public EntityResult getSharedFile(@PathVariable String shareId) {
        return serviceShare.getSharedFileById(shareId);
    }

    /**
     * 获取用户所有的共享文件列表
     * @return
     */
    @GetMapping("/list")
    public EntityResult getUserSharedFiles() {
        Integer userId = UserThreadLocal.get().getUserId();
        return serviceShare.getAllSharedFilesByUserId(userId);
    }


    /**
     * 检查提取密码
     * @param shareId
     * @param password
     * @return
     */
    @GetMapping("/check")
    public EntityResult checkPassword(String shareId, String password) {
        return serviceShare.checkPassword(shareId,password);
    }



}
