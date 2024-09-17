package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tan.dto.SaveFileDTO;
import com.tan.dto.UpdateFileDTO;
import com.tan.entity.*;
import com.tan.mapper.MapperFile;
import com.tan.mapper.MapperSpace;
import com.tan.service.ServiceFile;
import com.tan.utils.FileUtils;
import com.tan.utils.UserThreadLocal;
import com.tan.vo.FileListVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.tan.utils.FileConstants.FILE_PATH;
import static com.tan.utils.FileConstants.SERVER_FILE_PATH;
@Slf4j
@Service
public class ServiceFileImpl implements ServiceFile {

    @Autowired
    private MapperFile mapperFile;

    @Resource
    private MapperSpace mapperSpace;


    /**
     * 存储文件信息
     *
     * @param saveFileDTO
     * @return
     */
    @Override
    public EntityResult save(SaveFileDTO saveFileDTO) {

        Integer userId = UserThreadLocal.get().getUserId();

        EntityFile entityFile = new EntityFile();
        BeanUtil.copyProperties(saveFileDTO, entityFile);
        entityFile.setUserId(userId);

        /**
         * 注意:这里的saveFileDTO中的fileSize是Long,因为后面需要存入空间进行计算
         * 但存入文件,可以用容易读的字符串展示
         */

        entityFile.setFileSize(FileUtils.convertFileSize(saveFileDTO.getFileSize()));

        entityFile.setUpdateTime(LocalDateTime.now());
        entityFile.setSpaceId(saveFileDTO.getSpaceId());
        mapperFile.save(entityFile);

        //用户空间也需要更新---文件数和文件大小
        //获取用户空间
        //这里应该根据spaceId
        EntitySpace space = mapperSpace.getBySpacecId(saveFileDTO.getSpaceId());
        //更新用户空间的usedSpace和fileCount
        space.setFileCount(space.getFileCount() + 1);
        space.setUsedSpace(space.getUsedSpace() + saveFileDTO.getFileSize());
        mapperSpace.update(space);
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
        //获取该文件
        Integer fileId = updateFileDTO.getFileId();
        EntityFile entityFile = mapperFile.getById(fileId,userId);

        BeanUtil.copyProperties(updateFileDTO, entityFile);
        entityFile.setUpdateTime(LocalDateTime.now());

        mapperFile.update(entityFile);

        //更新文件之后,如果是移动,空间的文件数需要修改

        return EntityResult.success();
    }

    /**
     * 获取文件分类
     *
     * @param spaceId
     * @return
     */
    @Override
    public EntityResult getFileTypes(Integer spaceId) {
        List<String> list = mapperFile.getFileTypes(spaceId);
        return EntityResult.success(list);
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
    public PageBean<FileListVO> list(Integer currentPage, Integer pageSize, String fileType, String fileName,Integer spaceId) {
        PageBean<FileListVO> pageBean = new PageBean<>();

        PageHelper.startPage(currentPage, pageSize);

        //获取当前用户
        EntityUser user = UserThreadLocal.get();
        Integer userId = user.getUserId();

        List<FileListVO> data = mapperFile.list(userId,fileType,fileName,spaceId);
        Page<FileListVO> page = (Page<FileListVO>) data;

        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());

        return pageBean;
    }


}
