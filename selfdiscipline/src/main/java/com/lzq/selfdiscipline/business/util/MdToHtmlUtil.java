package com.lzq.selfdiscipline.business.util;

import org.apache.commons.io.FileUtils;
import org.pegdown.PegDownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * markdown 文件转 html
 */
public class MdToHtmlUtil {
    private static Logger logger = LoggerFactory.getLogger(MdToHtmlUtil.class);

    // md 文件后缀
    private static final String FILE_TYPE = "md";

    private MdToHtmlUtil() {}

    /**
     * 将md文件格式转换为html格式
     * 注意：md中的图片不能正常显示
     * @param mdfile
     * @return
     */
    public static File getHtmlByMdfile(File mdfile) {
        try {
            System.out.println(mdfile.getName());
            String filename = mdfile.getName();
            int index = filename.lastIndexOf(".");
            // 判断文件是否是md格式文件
            if (!FILE_TYPE.equals(filename.substring(index + 1).toLowerCase())){
                logger.error("This file is not an md file and conversion is not supported!!!");
                return null;
            }
            PegDownProcessor pegDownProcessor = new PegDownProcessor();
            String htmlStr = pegDownProcessor.markdownToHtml(FileUtils.readFileToString(mdfile, "UTF-8"));
            // System.out.println(htmlStr);
            File htmlFile = new File(filename.substring(0, index) + ".html");
            FileUtils.write(htmlFile, htmlStr);
            return htmlFile;
        } catch (IOException e) {
            logger.info("Md file to HTM error!!!" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
