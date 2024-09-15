package com.tan.controller;

import com.tan.entity.EntityResult;
import com.tan.entity.ResponseFile;
import com.tan.utils.AliOssUtil;
import com.tan.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static com.tan.utils.FileConstants.FILE_PATH;
import static com.tan.utils.FileConstants.SERVER_FILE_PATH;

@Slf4j
@RestController
public class ControllerFileUpload {

    @PostMapping("/upload")
    public EntityResult upload(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return EntityResult.error("文件不存在");
        }

        String originalName = file.getOriginalFilename();
        //保证上传文件名唯一-->UUID拼接
        String filename = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        System.out.println(filename);


        // 指定本地存储路径
        String localPath = FILE_PATH; // 替换为你的本地文件夹路径

        File localFile  = new File(localPath, filename);


        // 将文件存入本地
        try (
            FileOutputStream fos = new FileOutputStream(localFile)) {
            fos.write(file.getBytes());
        }

        // 构建返回的URL（如果需要）
        String filePath = SERVER_FILE_PATH + filename;

        //获取文件类型
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        log.info("fileType:{}",fileType);
        //获取文件大小
        long fileSizeBytes = file.getSize();
        String fileSize = FileUtils.convertFileSize(fileSizeBytes);

        //将文件存入阿里云
        //String url = AliOssUtil.uploadFile(filename, file.getInputStream());
        //将url存入数据库
        return EntityResult.success(new ResponseFile(filename,filePath,fileType,fileSize));
    }

}
