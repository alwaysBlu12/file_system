package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tan.dto.SaveFileDTO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
