package com.tan.service.impl;

import com.alibaba.fastjson.JSON;
import com.tan.entity.EntityFile;
import com.tan.entity.EntityResult;
import com.tan.mapper.MapperGlobal;
import com.tan.service.ServiceGlobal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class ServiceGlobalImpl implements ServiceGlobal {

    @Resource
    private MapperGlobal mapperGlobal;

    @Resource
    private RestHighLevelClient client;
    /**
     * 获取全部文件
     * @return
     */
    @Override
    public EntityResult list() {
        List<EntityFile>list =  mapperGlobal.list();
        return EntityResult.success(list);
    }

    /**
     * 自动补全
     *
     * @param key
     * @return
     */
    @Override
    public EntityResult getCompleteResult(String key) {
        try {
            //准备request对象
            SearchRequest request = new SearchRequest("file");
            //自动补全
            request.source().suggest(new SuggestBuilder().addSuggestion("mySuggestion",
                    SuggestBuilders
                            .completionSuggestion("suggestion")
                            .prefix(key)
                            .skipDuplicates(false)
                            .size(10)
            ));
            //发送请求
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            //解析结果
            Suggest suggest = response.getSuggest();
            CompletionSuggestion mySuggestion = suggest.getSuggestion("mySuggestion");
            List<CompletionSuggestion.Entry.Option> options = mySuggestion.getOptions();
            log.info("options:{}",options);
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
}
