package com.tan.controller;

import com.tan.entity.EntityResult;
import com.tan.entity.ResponseFile;
import com.tan.utils.AliOssUtil;
import com.tan.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;


@Slf4j
@RestController
public class ControllerFileUpload {

    @PostMapping("/upload")
    public EntityResult upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return EntityResult.error("文件不存在");
        }

        String originalName = file.getOriginalFilename();
        // 保证上传文件名唯一-->UUID拼接
        String filename = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        System.out.println(filename);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        log.info("fileType:{}", fileType);// 获取文件类型
        // 获取文件大小
        long fileSizeBytes = file.getSize();
        log.info("fileSizeBytes:{}", fileSizeBytes);

        String url = AliOssUtil.uploadFile(filename, file.getInputStream());

        return EntityResult.success(new ResponseFile(filename,url,fileType,fileSizeBytes));
    }

}
