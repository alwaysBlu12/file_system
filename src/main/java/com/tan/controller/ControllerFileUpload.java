package com.tan.controller;

import com.tan.entity.EntityResult;
import com.tan.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
public class ControllerFileUpload {

    @PostMapping("/upload")
    public EntityResult upload(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        //保证上传文件名唯一-->UUID拼接
        String filename = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        System.out.println(filename);
        //将文件存入阿里云
        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        return EntityResult.success(url);
    }

}
