package com.tan.controller;

import com.tan.dto.PageFileDTO;
import com.tan.dto.SaveFileDTO;
import com.tan.dto.UpdateFileDTO;
import com.tan.entity.EntityResult;
import com.tan.entity.PageBean;
import com.tan.service.ServiceFile;

import com.tan.utils.MqConstants;
import com.tan.vo.FileListVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/file")
public class ControllerFile {

    @Autowired
    private ServiceFile serviceFile;


    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 上传文件后进行保存
     * @return
     */
    @PostMapping
    public EntityResult saveFile(@RequestBody SaveFileDTO saveFileDTO) {
        Integer fileId = serviceFile.save(saveFileDTO);
        rabbitTemplate.convertAndSend(MqConstants.FILE_EXCHANGE, MqConstants.FILE_INSERT_KEY, fileId);
        return EntityResult.success();
    }


    /**
     * 获取文件列表
     * @param pageFileDTO
     * @return
     */
    @PostMapping("/list")
    public EntityResult<PageBean<FileListVO>> list(@RequestBody PageFileDTO pageFileDTO) {

        log.info("pageFileDTO:{}", pageFileDTO);
        PageBean<FileListVO> pageBean = serviceFile.list(pageFileDTO);
        return EntityResult.success(pageBean);
    }

    /**
     * 逻辑删除文件
     * @param fileId
     * @return
     */
    @DeleteMapping
    public EntityResult deleteById(Integer fileId) {
        serviceFile.deleteById(fileId);
        rabbitTemplate.convertAndSend(MqConstants.FILE_EXCHANGE, MqConstants.FILE_DELETE_KEY, fileId);
        return EntityResult.success();
    }

    /**
     * 查看文件详细信息-->可以做共享文件
     * @param fileId
     * @return
     */
    @GetMapping
    public EntityResult getById(Integer fileId) {
        return serviceFile.getById(fileId);
    }

    /**
     * 更新文件
     * @return
     */
    @PutMapping
    public EntityResult update(@RequestBody UpdateFileDTO updateFileDTO) {
        serviceFile.update(updateFileDTO);
        rabbitTemplate.convertAndSend(MqConstants.FILE_EXCHANGE, MqConstants.FILE_INSERT_KEY, updateFileDTO.getFileId());
        return EntityResult.success();
    }


    /**
     * 获取文件分类
     * @param spaceId
     * @return
     */
    @GetMapping("/type")
    public EntityResult getFileTypes(Integer spaceId) {
        return serviceFile.getFileTypes(spaceId);
    }


    /**
     * 自动补全
     * @param
     * @return
     */
    @GetMapping("/autocomplete")
    public EntityResult autoComplete(
            @RequestParam String fileName,
            @RequestParam Integer spaceId
    ) {
        return serviceFile.getCompleteResult(fileName,spaceId);
    }




}
