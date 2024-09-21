package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tan.dto.PageFileDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * 逻辑删除文件
     *
     * @param fileId
     * @return
     */
    @Override
    public EntityResult deleteById(Integer fileId) {

        //空间中的文件数要减少
        EntityFile file = mapperFile.getById(fileId);

        //获取空间id
        Integer spaceId = file.getSpaceId();

        //将逻辑字段变为1
        mapperFile.deleteLogic(fileId);

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

        //获取该文件
        Integer fileId = updateFileDTO.getFileId();
        EntityFile entityFile = mapperFile.getById(fileId);
        //当前空间
        Integer currentSpaceId = entityFile.getSpaceId();

        //如果需要移动空间,entityFile里面就是新的空间
        BeanUtil.copyProperties(updateFileDTO, entityFile);
        entityFile.setUpdateTime(LocalDateTime.now());

        Integer updateSpaceId = updateFileDTO.getSpaceId();
        if (!currentSpaceId.equals(updateSpaceId)) {

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
        Set<String> uniqueTypes = new HashSet<>(list); // 使用HashSet来自动去除重复项
        return EntityResult.success(uniqueTypes);
    }

    /**
     * 获取文件列表
     * @param pageFileDTO
     * @return
     */
    @Override
    public PageBean<FileListVO> list(PageFileDTO pageFileDTO) {
        PageBean<FileListVO> pageBean = new PageBean<>();

        Integer currentPage = pageFileDTO.getCurrentPage();
        Integer pageSize = pageFileDTO.getPageSize();

        PageHelper.startPage(currentPage, pageSize);

        Integer userId = UserThreadLocal.get().getUserId();
        pageFileDTO.setUserId(userId);
        List<FileListVO> data = mapperFile.list(pageFileDTO);
        Page<FileListVO> page = (Page<FileListVO>) data;

        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());

        return pageBean;
    }


}
