package com.anji.sp.model;

import lombok.Data;

import java.util.List;

@Data
public class PageQueryRep<T> {
    private Long total;
    private List<T> rows;
}
