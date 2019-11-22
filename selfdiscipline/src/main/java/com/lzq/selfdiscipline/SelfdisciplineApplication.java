package com.lzq.selfdiscipline;

import com.lzq.selfdiscipline.interceptors.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;

@SpringBootApplication
@MapperScan({"com.lzq.selfdiscipline.**.mapper"})
public class SelfdisciplineApplication extends WebMvcConfigurationSupport {
    private Logger logger = LoggerFactory.getLogger(SelfdisciplineApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SelfdisciplineApplication.class, args);
    }

    /**
     * 配置 静态资源路径访问
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("addResourceHandlers start.");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        logger.info("addResourceHandlers stop.");
        super.addResourceHandlers(registry);
    }

    // 登录拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 添加 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("addInterceptors start.");
        // registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/register");
        logger.info("addInterceptors stop.");
        super.addInterceptors(registry);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 开启路径后缀匹配
        // 匹配结尾 / :会识别 url 的最后一个字符是否为 /
        // localhost:8080/test 与 localhost:8080/test/ 等价
        configurer.setUseTrailingSlashMatch(true);
        // 匹配后缀名：会识别 xx.* 后缀的内容
        // localhost:8080/test 与 localhost:8080/test.jsp 等价
        // configurer.setUseSuffixPatternMatch(true);
    }
}
