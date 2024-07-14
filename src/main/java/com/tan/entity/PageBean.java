package com.tan.entity;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T> {
    private Long total;//总条数
    private List<T> items;//当前页数据集合
}
