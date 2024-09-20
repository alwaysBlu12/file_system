package com.tan.controller;

import com.tan.entity.EntityResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/cycle")
public class ControllerRecycle {

    /**
     * 永久删除
     * @param fileId
     * @return
     */
    @DeleteMapping
    public EntityResult permanentDelete(Integer fileId){

    }

}
