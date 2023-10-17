package com.wangxu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangxu.entity.MyUser;
import org.springframework.stereotype.Service;

import java.util.List;

public interface userService extends IService<MyUser> {
    List<MyUser> findByUUID(String uid);

    IPage<MyUser> queryNewsList(Integer page, Integer pageSize);

    Boolean create(MyUser body);
}
