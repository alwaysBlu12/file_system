package com.tan.service;


import com.tan.entity.EntityResult;

public interface ServiceRecycle {
    /**
     * 永久删除
     * @param fileId
     * @return
     */
    EntityResult deleteById(Integer fileId);

    /**
     * 获取回收站的文件类型
     * @return
     */
    EntityResult getRecycleFileTypes();

    /**
     * 恢复文件
     * @param fileId
     * @return
     */
    void recycleFile(Integer fileId);
}
