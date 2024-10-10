package com.tan.mq;


import com.tan.service.ServiceFile;
import com.tan.utils.MqConstants;
import com.tan.utils.UserThreadLocal;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileListener {

    @Resource
    private ServiceFile serviceFile;

    @RabbitListener(queues = MqConstants.FILE_INSERT_QUEUE)
    public void listenInsertOrUpdate(Integer fileId){
        log.info("监听添加文件线程--user:{}", UserThreadLocal.get());
        serviceFile.insertOrUpdate(fileId);
    }
    @RabbitListener(queues = MqConstants.FILE_DELETE_QUEUE)
    public void listenDelete(Integer fileId){
        serviceFile.delete(fileId);
    }
}
