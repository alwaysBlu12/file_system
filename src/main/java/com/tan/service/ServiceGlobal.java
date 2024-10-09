package com.tan.service;

import com.tan.entity.EntityResult;

public interface ServiceGlobal {
    EntityResult list();

    /**
     * 自动补全
     * @param key
     * @return
     */
    EntityResult getCompleteResult(String key);
}
