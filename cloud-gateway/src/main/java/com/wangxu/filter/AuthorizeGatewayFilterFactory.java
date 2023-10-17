package com.wangxu.filter;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.wangxu.cors.ModifiedRequestDecorator;
import com.wangxu.cors.RewriteConfig;
import com.wangxu.properties.VerifyWhite;
import io.netty.buffer.ByteBufAllocator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.example.utils.EncryptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 名称必须是xxxGatewayFilterFactory形式
 * todo：模拟授权的验证，具体逻辑根据业务完善
 */
@Component
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> implements Ordered{
    private static final Logger log = LoggerFactory.getLogger(AuthorizeGatewayFilterFactory.class);
    private static final String AUTHORIZE_TOKEN = "token";

    // 排除过滤的 uri 地址，nacos自行添加
    @Autowired
    private VerifyWhite verifyWhite;
 
    //构造函数，加载Config
    public AuthorizeGatewayFilterFactory() {
        //固定写法
        super(Config.class);
        log.info("加载自定义拦截器");
        System.out.println(123);
    }
 
    //读取配置文件中的参数 赋值到 配置类中
    @Override
    public List<String> shortcutFieldOrder() {
        //Config.enabled
        return Arrays.asList("enabled");
    }
 
    @Override
    public GatewayFilter apply(AuthorizeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            //判断是否开启授权验证
      /*      if (!config.enabled) {
                return chain.filter(exchange);
            }*/
            ServerHttpRequest request = exchange.getRequest();
            //白名单不做校验
            List<String> StrList = verifyWhite.getWhite();
            if (CollectionUtils.isNotEmpty(StrList)){
                String pathWhite = String.join(",", StrList);
                if (pathWhite.contains(request.getURI().getPath())){
                    return chain.filter(exchange);
                }
            }
            //拦截指定请求中的参数，修改参数(GET请求)
            if (request.getMethodValue().equalsIgnoreCase("GET") && request.getPath().toString().equalsIgnoreCase("/user/newsList")){
                // 获取原始的URI和查询参数
                URI originalUri = request.getURI();
                MultiValueMap<String, String> originalParams = request.getQueryParams();

                // 创建新的查询参数Map，并复制原始参数
                MultiValueMap<String, String> modifiedParams = new LinkedMultiValueMap<>(originalParams);

                // 修改查询参数
                modifiedParams.set("pageSize", "1");
                modifiedParams.set("page","2");
                // 构建新的URI
                URI modifiedUri = UriComponentsBuilder.fromUri(originalUri)
                        .replaceQueryParams(modifiedParams)
                        .build()
                        .toUri();

                // 创建新的ServerHttpRequest对象
                ServerHttpRequest modifiedRequest = request.mutate()
                        .uri(modifiedUri)
                        .build();
                exchange = exchange.mutate().request(modifiedRequest).build();

            }
            if (request.getPath().toString().equalsIgnoreCase("/user/create") && "POST".equalsIgnoreCase(request.getMethodValue())){
//                String body = resolveBodyFromRequest(request);
//                String newBody = SmUtil.sm2("00b39b3c7b6ff9860ac212912d808f00b186d06332043ba2efb5248a967efc2f8e", null).decryptStr(body, KeyType.PrivateKey);
          /*      MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
                AtomicReference<String> bodyStr = new AtomicReference<>("");
                Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
                    //因为约定了终端传参的格式，所以只考虑json的情况，如果是表单传参，请自行增加
                    if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType) || MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType)) {
                        String newBody = "";
                        try {
                            // 解密body
                            newBody = EncryptUtils.decrypt2Data(body, finalThirdPrivateKey);
                            newBody = UnicodeUtil.toString(newBody);
                            bodyStr.set(newBody);
                        } catch (Exception e) {
                            log.error("", e);
                            log.error("[网关参数解密异常]请求路径:{},请求参数:{},错误信息:{}", exchange.getRequest().getPath(), body, e);
                            return Mono.error(new EncryptException("非法参数！"));
                        }
                        return Mono.just(newBody);
                    }
                    return Mono.empty();
                });
*/
        }
            return chain.filter(exchange);
        };
    }
    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体
     */
    private String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();

        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        //获取request body
        return bodyRef.get();
    }
    private DataBuffer stringBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }

    @Override
    public int getOrder() {
        return -100000000;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        // 控制是否开启认证
        private boolean enabled = true;

    }
}