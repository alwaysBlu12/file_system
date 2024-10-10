package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tan.dto.SaveSpaceDTO;
import com.tan.dto.UpdateSpaceDTO;
import com.tan.entity.EntityResult;
import com.tan.entity.EntitySpace;
import com.tan.mapper.MapperFile;
import com.tan.mapper.MapperSpace;
import com.tan.service.ServiceSpace;
import com.tan.utils.ElasticSearchConstant;
import com.tan.utils.UserThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class ServiceSpaceImpl implements ServiceSpace {

    @Resource
    private MapperSpace mapperSpace;

    @Resource
    private MapperFile mapperFile;

    @Resource
    private RestHighLevelClient client;

    /**
     * 空间列表
     * @return
     */
    @Override
    public EntityResult list() {
        Integer userId = UserThreadLocal.get().getUserId();

        List<EntitySpace> list = mapperSpace.list(userId);
        return EntityResult.success(list);
    }

    /**
     * 根据spaceId获取空间信息
     * @param spaceId
     * @return
     */
    @Override
    public EntityResult getById(Integer spaceId) {
       EntitySpace entitySpace =  mapperSpace.getBySpaceId(spaceId);
       return EntityResult.success(entitySpace);
    }

    /**
     * 添加空间
     *
     * @param saveSpaceDTO
     * @return
     */
    @Override
    public EntityResult save(SaveSpaceDTO saveSpaceDTO) {
        EntitySpace entitySpace = new EntitySpace();
        BeanUtil.copyProperties(saveSpaceDTO, entitySpace);
        //用户id
        Integer userId = UserThreadLocal.get().getUserId();

        entitySpace.setUserId(userId);
        //默认生成一个10Mb的空间,单位是字节
        entitySpace.setTotalSpace(10000000L);
        entitySpace.setUsedSpace(0L);
        entitySpace.setFileCount(0);
        entitySpace.setCreateTime(LocalDateTime.now());
        mapperSpace.save(entitySpace);

        //创建对应空间的索引库

        try {
            //创建request对象
            CreateIndexRequest request = new CreateIndexRequest(userId + "_" + entitySpace.getSpaceId());
            //准备参数
            request.source(ElasticSearchConstant.AUTO_COMPLETE, XContentType.JSON);
            //发送请求
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return EntityResult.success();
    }

    /**
     * 删除空间
     *
     * @param spaceId
     * @return
     */
    @Override
    public EntityResult deleteById(Integer spaceId) {
        mapperSpace.deleteById(spaceId);

        //同时需要删除文件
        mapperFile.deleteBySpaceId(spaceId);

        return EntityResult.success();
    }

    /**
     * 更新空间
     *
     * @param updateSpaceDTO
     * @return
     */
    @Override
    public EntityResult update(UpdateSpaceDTO updateSpaceDTO) {
        EntitySpace entitySpace = new EntitySpace();
        BeanUtil.copyProperties(updateSpaceDTO, entitySpace);
        //复用update
        mapperSpace.update(entitySpace);
        return EntityResult.success();
    }
}
