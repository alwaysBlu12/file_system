package com.tan.controller;

import com.tan.entity.EntityResult;
import com.tan.service.ServiceRecycle;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/cycle")
public class ControllerRecycle {

    @Resource
    private ServiceRecycle serviceRecycle;

    /**
     * 永久删除
     * @param fileId
     * @return
     */
    @DeleteMapping
    public EntityResult permanentDelete(Integer fileId){
        return serviceRecycle.deleteById(fileId);
    }

    /**
     * 获取回收站的文件类型
     * @return
     */
    @GetMapping("/type")
    public EntityResult getRecycleFileTypes(){
        return serviceRecycle.getRecycleFileTypes();
    }


    /**
     * 恢复文件
     * @param fileId
     * @return
     */
    @GetMapping
    public EntityResult recycleFile(@RequestParam("fileId") Integer fileId){
        return serviceRecycle.recycleFile(fileId);
    }

}
