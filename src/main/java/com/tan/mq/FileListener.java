package com.tan.mq;


import com.tan.service.ServiceFile;
import com.tan.utils.MqConstants;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FileListener {

    @Resource
    private ServiceFile serviceFile;


    @RabbitListener(queues = MqConstants.FILE_INSERT_QUEUE)
    public void listenInsertOrUpdate(Integer fileId){
        serviceFile.insertOrUpdate(fileId);
    }

    @RabbitListener(queues = MqConstants.FILE_DELETE_QUEUE)
    public void listenDelete(Integer fileId){
        serviceFile.delete(fileId);
    }


}
