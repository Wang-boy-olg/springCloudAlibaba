package com.wangxu.filter;

import com.wangxu.properties.VerifyWhite;

import org.example.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class RequestFilter implements GlobalFilter, Ordered {

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private VerifyWhite verifyWhite;
    private static final Logger log = LoggerFactory.getLogger(AuthorizeGatewayFilterFactory.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath(); // 当前调用方法的url
        log.info("当前路径的url:"+path);
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst("token");
        ServerHttpResponse response = exchange.getResponse();
        //白名单不做校验
    /*    List<String> StrList = verifyWhite.getWhite();
        if (CollectionUtils.isNotEmpty(StrList)){
            String pathWhite = String.join(",", StrList);
            if (pathWhite.contains(path)){
                return chain.filter(exchange);
            }
        }*/
        if (token==null){
            log.error("没有登陆！");
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //处理完成，直接拦截，不再进行下去
            return ServletUtils.webFluxResponseWriter(exchange.getResponse(),"没有登陆",HttpStatus.METHOD_NOT_ALLOWED.value());
        }
        return filters(exchange,chain);
    }

    public Mono<Void> filters(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println(123456);
        return chain.filter(exchange);
    }
    @Override
    public int getOrder() {
        return -100;
    }
}
