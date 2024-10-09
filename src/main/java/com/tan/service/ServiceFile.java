package com.tan.service;

import com.tan.dto.PageFileDTO;
import com.tan.dto.SaveFileDTO;
import com.tan.dto.UpdateFileDTO;
import com.tan.entity.EntityResult;
import com.tan.entity.PageBean;
import com.tan.vo.FileListVO;

public interface ServiceFile {
    PageBean<FileListVO> list(PageFileDTO pageFileDTO);

    /**
     * 存储文件信息
     * @param saveFileDTO
     * @return
     */
    Integer save(SaveFileDTO saveFileDTO);

    /**
     * 逻辑 删除文件
     * @param fileId
     * @return
     */
    void deleteById(Integer fileId);

    /**
     * 查看文件
     * @param fileId
     * @return
     */
    EntityResult getById(Integer fileId);

    /**
     * 更新文件
     * @param updateFileDTO
     * @return
     */
    void update(UpdateFileDTO updateFileDTO);

    /**
     * 获取文件分类
     * @param spaceId
     * @return
     */
    EntityResult getFileTypes(Integer spaceId);

    /**
     * 自动补全
     * @param key
     * @return
     */
    EntityResult getCompleteResult(String key);

    void insertOrUpdate(Integer fileId);

    void delete(Integer fileId);
}
