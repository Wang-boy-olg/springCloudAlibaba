package com.wangxu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangxu.entity.MyUser;

import java.util.List;


public interface userMapper extends BaseMapper<MyUser> {
    List<MyUser> findByUUID(String uid);
}
