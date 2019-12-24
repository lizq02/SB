package com.lzq.selfdiscipline.business;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 系统 properties
 */
@Component
public class SystemProperties {
    // windows系统文件路径前缀
    public static String windowsFilePath;
    // linux系统文件路径前缀
    public static String linuxFilePath;
    // openoffice 连接host地址
    public static String openofficeHost;
    // openoffice 端口号
    public static Integer openofficePort;
    // 虚拟路径映射
    public static String virtualPathmap;

    // windows系统文件路径前缀
    @Value("${filepath.windows_file_path}")
    private String windows_file_path;
    // linux系统文件路径前缀
    @Value("${filepath.linux_file_path}")
    private String linux_file_path;

    // openoffice 连接host地址
    @Value("${openoffice.host}")
    private String openoffice_host;
    // openoffice 端口号
    @Value("${openoffice.port}")
    private Integer openoffice_port;

    // 虚拟路径映射
    @Value("${virtualPathmap}")
    private String virtual_pathmap;

    @PostConstruct
    private void init() {
        Objects.requireNonNull(this.windows_file_path, "获取文件存储路径前缀失败!!!");
        Objects.requireNonNull(this.linux_file_path, "获取文件存储路径前缀失败!!!");
        Objects.requireNonNull(this.openoffice_host, "获取openoffice连接地址失败!!!");
        Objects.requireNonNull(this.openoffice_port, "获取openoffice端口号失败!!!");
        Objects.requireNonNull(this.virtual_pathmap, "获取虚拟路径映射失败!!!");
        windowsFilePath = this.windows_file_path;
        linuxFilePath = this.linux_file_path;
        openofficeHost = this.openoffice_host;
        openofficePort = this.openoffice_port;
        virtualPathmap = this.virtual_pathmap;
    }
}
