package com.sillylab.transaction.template.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestInfo {
    private Long id;
    private String title;
    private String author;
    private Date createTime;
    private Date updateTime;
}