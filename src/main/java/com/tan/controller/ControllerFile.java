package com.tan.controller;

import com.tan.entity.EntityResult;
import com.tan.entity.PageBean;
import com.tan.service.ServiceFile;
import com.tan.vo.FileListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/file")
public class ControllerFile {

    @Autowired
    private ServiceFile serviceFile;


    /**
     * 获取文件列表
     * @param currentPage
     * @param pageSize
     * @param fileType
     * @param fileName
     * @return
     */
    @GetMapping("/list")
    public EntityResult<PageBean<FileListVO>>list(
            Integer currentPage,
            Integer pageSize,
            @RequestParam(required = false) String fileType,
            @RequestParam(required = false)  String fileName){

        log.info("current:{},pageSize:{},fileType:{},fileName:{}",currentPage,pageSize,fileType,fileName);

        PageBean<FileListVO> pageBean = serviceFile.list(currentPage,pageSize,fileType,fileName);
        return EntityResult.success(pageBean);
    }

}
