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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
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
        // 保证上传文件名唯一-->UUID拼接
        String filename = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
        System.out.println(filename);

        // 获取文件类型
        String fileType = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        log.info("fileType:{}", fileType);
        // 获取文件大小
        long fileSizeBytes = file.getSize();
        log.info("fileSizeBytes:{}", fileSizeBytes);

        // 定义需要进行UTF-8编码处理的文本文件类型
        List<String> textFileTypes = Arrays.asList("txt", "json", "md", "csv");

        // 如果是纯文本文件，确保其以 UTF-8 编码上传
        if (textFileTypes.contains(fileType)) {
            InputStream inputStream = file.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            // 将内容转为 UTF-8 编码字节流上传
            String url = AliOssUtil.uploadFile(filename, new ByteArrayInputStream(content.toString().getBytes(StandardCharsets.UTF_8)));
            return EntityResult.success(new ResponseFile(filename, url, fileType, fileSizeBytes));
        }

        // 如果是二进制文件，如 PDF，直接上传二进制文件
        List<String> binaryFileTypes = Arrays.asList("pdf", "xlsx", "docx");

        if (binaryFileTypes.contains(fileType)) {
            // 对于二进制文件，直接上传，绝不进行编码转换
            String url = AliOssUtil.uploadFile(filename, file.getInputStream());
            return EntityResult.success(new ResponseFile(filename, url, fileType, fileSizeBytes));
        }

        return EntityResult.error("不支持的文件类型");
    }

}
