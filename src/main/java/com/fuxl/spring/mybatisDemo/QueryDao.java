package com.fuxl.spring.mybatisDemo;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface QueryDao {
    @Select("select * from tm_dealer_basicinfo")
    public List query();
}
