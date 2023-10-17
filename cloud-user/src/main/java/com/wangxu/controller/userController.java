package com.wangxu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wangxu.clients.*;
import com.wangxu.entity.MyUser;
import com.wangxu.service.userService;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RefreshScope
public class userController {
    @Value("${server.port}")
    private String prot;
    @Resource
    private productClient client;
    @Autowired
    private userService userService;



    @GetMapping("/user/getProductListInfo")
    public Map<String,Object> getProductListInfo(){
        //log.info("进入服务"+prot);
        Map map = client.getProductList();
        return map;
    }
    @GetMapping("/user/getUser")
    public List<MyUser> getUser(){
        return userService.list();
    }
    @GetMapping("/user/getUserById/{uid}")
    @ResponseBody
    public List<MyUser> getUserById(@PathVariable String uid){
        return userService.findByUUID(uid);
    }
    @GetMapping("/user/newsList")
    public IPage<MyUser> queryNewsList(@RequestParam Integer page,
                                                  @RequestParam Integer pageSize){
        IPage<MyUser> iPage = userService.queryNewsList(page, pageSize);
        return iPage;
    }
    @GetMapping("/user/getUserByName")
    public List<MyUser> getUserByName(@RequestParam String name){
       List<MyUser> myUserList = userService.list(new LambdaQueryWrapper<MyUser>().eq(MyUser::getName,name));
        return myUserList;
    }

    @PostMapping("/user/create")
    public R<?> create(@RequestBody MyUser body){
        return R.ok(userService.create(body));
    }


}