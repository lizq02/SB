package com.lzq.selfdiscipline;

import com.lzq.selfdiscipline.business.SystemProperties;
import com.lzq.selfdiscipline.business.util.FileUtil;
import com.lzq.selfdiscipline.ta.interceptors.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;

import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
@EnableTransactionManagement
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
        /* 配置虚拟路径映射：映射到对应文件位置
         * 注意：window系统，配置文件路径需要加 file: 前缀，如file:F:\xxx\xxx\
         */
        String os = System.getProperty("os.name").toLowerCase();
        String filePrefix;
        if (os.startsWith("window")) {
            filePrefix = "file:" + SystemProperties.windowsFilePath;
        } else if (os.startsWith("linux")) {
            filePrefix = SystemProperties.linuxFilePath;
        }else {
            filePrefix = null;
        }
        registry.addResourceHandler("/" + SystemProperties.virtualPathmap + "/**").addResourceLocations(filePrefix);
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
        // 登录拦截器：不拦截请求路径
        List<String> excludePathPatterns = new LinkedList<>();
        excludePathPatterns.add("/loginController.do");
        excludePathPatterns.add("/loginController!login.do");
        excludePathPatterns.add("/register");
        excludePathPatterns.add("/static/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludePathPatterns);

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

    @Override
    protected void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/index").setViewName("/loginController.do");
    }
}
