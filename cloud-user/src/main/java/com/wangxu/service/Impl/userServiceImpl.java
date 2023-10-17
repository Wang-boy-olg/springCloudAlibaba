package com.wangxu.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangxu.entity.MyUser;
import com.wangxu.mapper.userMapper;
import com.wangxu.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class userServiceImpl extends ServiceImpl<userMapper, MyUser> implements userService{
    @Resource
    private userMapper userMapper;
    @Override
    public List<MyUser> findByUUID(String uid) {
        return userMapper.findByUUID(uid);
    }

    @Override
    public IPage<MyUser> queryNewsList(Integer page, Integer pageSize) {
        Page<MyUser> page1 = new Page<>(page,pageSize);
        //这里进行了时间排序
        QueryWrapper<MyUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("updated_time");
        //MP自带的selectPage方法，有兴趣的同学，可以看一下源码
        IPage<MyUser> iPage = userMapper.selectPage(new Page<>(page, pageSize),
                queryWrapper);
        return iPage;

    }

    @Override
    public Boolean create(MyUser body) {
        Date date=new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化时间
        body.setUpdatedTime(formatter.format(date));
        save(body);
        return true;
    }
}
