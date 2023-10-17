package org.example.utils;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class test {
    @Resource
    private static ThreadPoolUtils threadPoolUtils;

    public static boolean test1() throws Exception {
        List<String> strList = new ArrayList<>();
        strList.add("test");
        strList.add("test1");
        strList.add("test2");
        strList.add("test3");
        strList.add("test4");
        strList.add("test5");
        /**
         * supplyAsync异步有返回值
         */
        /*CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < strList.size(); i++) {
                strList.set(i, strList.get(i) + "1");
            }
            return strList;
        },threadPoolUtils.getThreadPool());*/
        /**
         * runAsync无返回值
         */
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int i = 0; i < 10; i++) {

                    System.out.println("任务执行" + i);
                }
            }, threadPoolUtils.getThreadPool());

            //System.out.println(future.get());
            return true;
    }

    public static void main(String[] args) {
        try {
            System.out.println(test1());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
