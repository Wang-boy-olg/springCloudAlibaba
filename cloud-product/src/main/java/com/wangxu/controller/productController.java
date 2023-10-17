package com.wangxu.controller;



import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class productController {
    @Value("${path-image:${user.dir}/images}")
    private String prot;
    @GetMapping("/product/productList")
    public Map<String,Object> getProductList(){
        //log.info("进入商品服务"+prot);
        Map<String,Object> map = new HashMap<>();
        map.put("state",200);
        map.put("msg","这里是商品列表端口号"+prot);
        return map;
    }
    @GetMapping("/product/getList")
    public List getList(){
        return new ArrayList();
    }
    @PostMapping(value = "/product/upload")
    public boolean upload(@RequestParam("image") MultipartFile image) throws IOException{
        if (image != null){
            String imageName = image.getOriginalFilename();
            String pathName = "/Users/wangxu/Desktop/test";
            File targetFile = new File(pathName);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            File saveFile = new File(targetFile, imageName);
            image.transferTo(saveFile);
            return true;
        }
        return false;
    }
    public static void main(String[] args) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);

    }
}
