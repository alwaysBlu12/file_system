package com.tan.controller;

import com.tan.entity.EntityResult;
import com.tan.service.ServiceFile;
import com.tan.service.ServiceGlobal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/global")
public class GlobalController {

    @Resource
    private ServiceGlobal serviceGlobal;

    /**
     * 获取全部文件-->存入索引库
     * @return
     */
    @GetMapping
    public EntityResult list() {
        return serviceGlobal.list();
    }

    /**
     * 自动补全
     * @param key
     * @return
     */
    @GetMapping("/autocomplete")
    public EntityResult autoComplete(@RequestParam("queryData") String key){
        return serviceGlobal.getCompleteResult(key);
    }


}
