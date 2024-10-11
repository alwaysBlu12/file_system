package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tan.dto.CreateShareDTO;
import com.tan.entity.EntityFile;
import com.tan.entity.EntityResult;
import com.tan.entity.EntityShare;
import com.tan.entity.EntityUser;
import com.tan.mapper.MapperFile;
import com.tan.mapper.MapperShare;
import com.tan.mapper.MapperUser;
import com.tan.service.ServiceShare;
import com.tan.utils.UserThreadLocal;
import com.tan.vo.ShareFileVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Slf4j
@Service
public class ServiceShareImpl implements ServiceShare {

    @Resource
    private MapperFile mapperFile;

    @Resource
    private MapperShare mapperShare;

    @Resource
    private MapperUser mapperUser;
    /**
     * 创建共享文件
     * @param createShareDTO
     * @return
     */
    @Override
    public EntityResult create( CreateShareDTO createShareDTO) {

        Integer expiryTime = createShareDTO.getExpiryTime();
        Integer fileId = createShareDTO.getFileId();
        String filePassword = createShareDTO.getFilePassword();

        //查询文件
        EntityFile file = mapperFile.getById(fileId);

        if (Objects.isNull(file)) {
            return EntityResult.error("文件不存在");
        }

        //生成4位唯一共享id
        String shareId = UUID.randomUUID().toString().substring(0, 4);

        EntityShare entityShare = new EntityShare();

        entityShare.setShareId(shareId);
        entityShare.setCreateTime(LocalDateTime.now());
        entityShare.setFilePassword(filePassword);
        entityShare.setFileId(fileId);
        entityShare.setUserId(UserThreadLocal.get().getUserId());
        entityShare.setExpiryTime(LocalDateTime.now().plusDays(expiryTime));

        //生成链接
        String url = "http://localhost:5173/share/" + shareId;
        entityShare.setShareLink(url);

        //存入数据库
        mapperShare.insert(entityShare);
        //返回唯一id
        return EntityResult.success(url);
    }

    /**
     * 删除共享文件
     *
     * @param shareId
     * @return
     */
    @Override
    public EntityResult deleteById(String shareId) {
        mapperShare.deleteById(shareId);
        return EntityResult.success();
    }

    @Override
    public EntityResult getSharedFileById(String shareId) {
        ShareFileVO shareFileVO = mapperShare.getByShareInfo(shareId);
        if (Objects.isNull(shareFileVO)) {
            return EntityResult.error("文件不存在");
        }
        //查询分享用户
        EntityUser user = mapperUser.getUserById(shareFileVO.getUserId());
        shareFileVO.setShareUser(user.getUsername());
        return EntityResult.success(shareFileVO);
    }

    @Override
    public EntityResult getAllSharedFilesByUserId(Integer userId) {
        List<EntityShare> list = mapperShare.getAllSharedFilesByUserId(userId);
        return EntityResult.success(list);
    }

    @Override
    public EntityResult checkPassword(String shareId, String password) {
        //获取共享文件信息
        EntityShare share = mapperShare.getByShareId(shareId);
        if (Objects.isNull(share)) {
            return EntityResult.error("文件不存在");
        }
        //判断密码是否正确
        if (!share.getFilePassword().equals(password)) {
            return EntityResult.error("密码错误");
        }
        return EntityResult.success();
    }
}
