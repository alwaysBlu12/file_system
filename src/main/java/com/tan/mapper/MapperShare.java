package com.tan.mapper;

import com.tan.entity.EntityResult;
import com.tan.entity.EntityShare;
import com.tan.vo.ShareFileVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MapperShare {
    @Insert("insert into share (share_id, file_id, user_id, expiry_time, create_time,file_password,share_link) VALUE  " +
            "(#{shareId},#{fileId},#{userId},#{expiryTime},#{createTime},#{filePassword},#{shareLink})")
    void insert(EntityShare entityShare);

    @Delete("delete from share where share_id = #{shareId}")
    EntityResult deleteById(String shareId);

    ShareFileVO getByShareInfo(String shareId);

    @Select("select * from share where user_id = #{userId}")
    List<EntityShare> getAllSharedFilesByUserId(Integer userId);

    @Select("select * from share where share_id = #{shareId}")
    EntityShare getByShareId(String shareId);
}

