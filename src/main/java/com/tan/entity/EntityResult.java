package com.tan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *
 *
 * 响应类实体
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityResult {
    /**
     * 响应码  成功:200   失败:400
     */
    private Integer code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 返回的数据
     */
    private Object data;

    //增删改 成功响应
    public static EntityResult success(){
        return new EntityResult(200,"success",null);
    }
    //查询 成功响应
    public static EntityResult success(Object data){
        return new EntityResult(200,"success",data);
    }
    //失败响应
    public static EntityResult error(String msg){
        return new EntityResult(400,msg,null);
    }
}