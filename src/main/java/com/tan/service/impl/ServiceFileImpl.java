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

import java.time.LocalDateTime;
import java.util.List;

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
    public EntityResult deleteById(Integer fileId,Integer spaceId) {

        log.info("-----------------打印参数:{}",fileId);

        //空间中的文件数要减少
        EntityFile file = mapperFile.getById(fileId);
        mapperFile.deleteById(fileId);
        //也是复用上了
        mapperSpace.subFileCountAndSpace(spaceId,FileUtils.convertToBytes(file.getFileSize()));
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
        EntityFile entityFile = mapperFile.getById(fileId);
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
        EntityFile entityFile = mapperFile.getById(fileId);
        //当前空间
        Integer currentSpaceId = entityFile.getSpaceId();
        BeanUtil.copyProperties(updateFileDTO, entityFile);
        entityFile.setUpdateTime(LocalDateTime.now());

        Integer updateSpaceId = updateFileDTO.getSpaceId();
        if (!currentSpaceId.equals(updateSpaceId)) {
            //移动空间

            //获取当前文件的大小
            String fileSize = entityFile.getFileSize();
            long fileByte = FileUtils.convertToBytes(fileSize);
            //当前空间-1
            mapperSpace.subFileCountAndSpace(currentSpaceId,fileByte);
            //更新空间+1
            mapperSpace.addFileCountAndSpace(updateSpaceId,fileByte);
            //更新
        }

        mapperFile.update(entityFile);

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
