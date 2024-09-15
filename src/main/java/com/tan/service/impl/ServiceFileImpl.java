package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tan.dto.SaveFileDTO;
import com.tan.dto.UpdateFileDTO;
import com.tan.entity.EntityFile;
import com.tan.entity.EntityResult;
import com.tan.entity.EntityUser;
import com.tan.entity.PageBean;
import com.tan.mapper.MapperFile;
import com.tan.service.ServiceFile;
import com.tan.utils.UserThreadLocal;
import com.tan.vo.FileListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.tan.utils.FileConstants.FILE_PATH;
import static com.tan.utils.FileConstants.SERVER_FILE_PATH;

@Service
public class ServiceFileImpl implements ServiceFile {

    @Autowired
    private MapperFile mapperFile;


    /**
     * 存储文件信息
     *
     * @param saveFileDTO
     * @return
     */
    @Override
    public EntityResult save(SaveFileDTO saveFileDTO) {
        EntityFile entityFile = new EntityFile();
        BeanUtil.copyProperties(saveFileDTO, entityFile);
        entityFile.setUserId(UserThreadLocal.get().getUserId());
        entityFile.setUploadTime(LocalDateTime.now());
        mapperFile.save(entityFile);
        return EntityResult.success();
    }

    /**
     * 删除文件
     *
     * @param fileId
     * @return
     */
    @Override
    public EntityResult deleteById(Integer fileId) {
        mapperFile.deleteById(fileId);
        return EntityResult.success();
    }

    /**
     * 查看文件
     *
     * @param fileId
     * @return
     */
    @Override
    public EntityResult getById(Integer fileId) {
        Integer userId = UserThreadLocal.get().getUserId();
        EntityFile entityFile = mapperFile.getById(fileId,userId);
        if (entityFile == null) {
            return EntityResult.error("找不到该文件");
        }
        return EntityResult.success(entityFile);
    }

    /**
     * 更新文件
     *
     * @param updateFileDTO
     * @return
     */
    @Override
    public EntityResult update(UpdateFileDTO updateFileDTO) {

        Integer userId = UserThreadLocal.get().getUserId();
        updateFileDTO.setUserId(userId);

        //获取该文件
        Integer fileId = updateFileDTO.getFileId();
        EntityFile entityFile = mapperFile.getById(fileId,userId);
        if (entityFile == null) {
            return EntityResult.error("文件不存在");
        }


        String newFileName = updateFileDTO.getNewFileName();

        String oldFileFullPath = FILE_PATH+"\\"+entityFile.getFileName();
        File oldfile = new File(oldFileFullPath);
        String newFileFullPath = FILE_PATH+"\\"+updateFileDTO.getNewFileName();
        File newfile = new File(newFileFullPath);

        boolean success = oldfile.renameTo(newfile);

        if (!success){
            return EntityResult.error("更新失败");
        }
        String fileType = newFileName.substring(newFileName.lastIndexOf(".")+1);
        updateFileDTO.setFileType(fileType);
        updateFileDTO.setFilePath(SERVER_FILE_PATH+newFileName);
        mapperFile.update(updateFileDTO);
        return EntityResult.success();
    }

    /**
     * 获取文件列表
     * @param currentPage
     * @param pageSize
     * @param fileType
     * @param fileName
     * @return
     */
    @Override
    public PageBean<FileListVO> list(Integer currentPage, Integer pageSize, String fileType, String fileName) {
        PageBean<FileListVO> pageBean = new PageBean<>();

        PageHelper.startPage(currentPage, pageSize);

        //获取当前用户
        EntityUser user = UserThreadLocal.get();
        Integer userId = user.getUserId();

        List<FileListVO> data = mapperFile.list(userId,fileType,fileName);

        Page<FileListVO> page = (Page<FileListVO>) data;

        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());

        return pageBean;
    }


}
