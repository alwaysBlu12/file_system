package com.tan.service.impl;

import com.tan.entity.EntityFile;
import com.tan.entity.EntityResult;
import com.tan.mapper.MapperFile;
import com.tan.mapper.MapperSpace;
import com.tan.service.ServiceRecycle;
import com.tan.utils.FileUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ServiceRecycleImpl implements ServiceRecycle {

    @Resource
    private MapperFile mapperFile;

    @Resource
    private MapperSpace mapperSpace;

    /**
     * 永久删除
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
     * 获取回收站的文件类型
     *
     * @return
     */
    @Override
    public EntityResult getRecycleFileTypes() {
        List<String> list = mapperFile.getRecycleFileTypes();
        Set<String> data = new HashSet<>(list);
        return EntityResult.success(data);
    }

    /**
     * 恢复文件
     *
     * @param fileId
     * @return
     */
    @Override
    public EntityResult recycleFile(Integer fileId) {
        //空间中的文件数要增加
        EntityFile file = mapperFile.getById(fileId);

        //获取空间id
        Integer spaceId = file.getSpaceId();

        //将逻辑字段变为0
        mapperFile.recycleFile(fileId);

        //也是复用上了
        mapperSpace.addFileCountAndSpace(spaceId, FileUtils.convertToBytes(file.getFileSize()));
        return EntityResult.success();
    }
}
