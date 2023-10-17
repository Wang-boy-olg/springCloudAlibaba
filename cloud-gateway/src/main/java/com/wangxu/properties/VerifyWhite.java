package com.wangxu.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "verify.whites")
public class VerifyWhite {
    List<String> white = new ArrayList<>();

    public List<String> getWhite()
    {
        return white;
    }

    public void setWhite(List<String> white)
    {
        this.white = white;
    }
}
