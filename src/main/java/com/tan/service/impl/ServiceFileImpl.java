package com.tan.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
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
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Resource
    private RestHighLevelClient client;


    /**
     * 存储文件信息
     *
     * @param saveFileDTO
     * @return
     */
    @Override
    public Integer save(SaveFileDTO saveFileDTO) {

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

        //10.6号,返回文件id,给消息队列发消息
        return entityFile.getFileId();
    }

    /**
     * 逻辑删除文件
     *
     * @param fileId
     * @return
     */
    @Override
    public void deleteById(Integer fileId) {

        //空间中的文件数要减少
        EntityFile file = mapperFile.getById(fileId);

        //获取空间id
        Integer spaceId = file.getSpaceId();

        //将逻辑字段变为1
        mapperFile.deleteLogic(fileId);

        //也是复用上了
        mapperSpace.subFileCountAndSpace(spaceId,FileUtils.convertToBytes(file.getFileSize()));

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
    public void update(UpdateFileDTO updateFileDTO) {

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
     * 自动补全
     *
     * @param
     * @return
     */
    @Override
    public EntityResult getCompleteResult(String fileName,Integer spaceId) {
        try {
            //获取用户信息
            EntityUser user = UserThreadLocal.get();

            //准备request对象
            SearchRequest request = new SearchRequest(user.getUserId()+"_"+spaceId);
            //自动补全
            request.source().suggest(new SuggestBuilder().addSuggestion("mySuggestion",
                    SuggestBuilders
                            .completionSuggestion("suggestion")
                            .prefix(fileName)
                            .skipDuplicates(false)
                            .size(10)
            ));
            //发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析结果
            Suggest suggest = response.getSuggest();
            CompletionSuggestion mySuggestion = suggest.getSuggestion("mySuggestion");
            List<CompletionSuggestion.Entry.Option> options = mySuggestion.getOptions();

            List<EntityFile> rs = new ArrayList<>();
            for (CompletionSuggestion.Entry.Option option : options) {
                //String result = option.getText().toString();
                String sourceAsString = option.getHit().getSourceAsString();
                //反序列化
                EntityFile entityFile = JSON.parseObject(sourceAsString, EntityFile.class);
                rs.add(entityFile);
            }
            return EntityResult.success(rs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 新增或者更新文档
     * @param fileId
     */
    @Override
    public void insertOrUpdate(Integer fileId) {
        try {

            //获取文件
            EntityFile entityFile = new EntityFile(mapperFile.getById(fileId));
            //在这条异步线程中不能调用UserThreadLocal-->通过文件获取用户信息
            Integer spaceId = entityFile.getSpaceId();
            Integer userId = entityFile.getUserId();
            //创建request对象
            IndexRequest request = new IndexRequest(userId+"_"+spaceId).id(fileId.toString());
            //准备参数
            request.source(JSON.toJSONString(entityFile), XContentType.JSON);
            //发送请求
            client.index(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文档
     * @param fileId
     */
    @Override
    public void delete(Integer fileId) {
        try {
            //获取文件
            EntityFile entityFile = new EntityFile(mapperFile.getById(fileId));
            //在这条异步线程中不能调用UserThreadLocal-->通过文件获取用户信息
            Integer spaceId = entityFile.getSpaceId();
            Integer userId = entityFile.getUserId();
            DeleteRequest request = new DeleteRequest(userId+"_"+spaceId, fileId.toString());
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
