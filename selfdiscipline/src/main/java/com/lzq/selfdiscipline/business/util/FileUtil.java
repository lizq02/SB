package com.lzq.selfdiscipline.business.util;

import org.apache.commons.codec.binary.Base64;
import com.lzq.selfdiscipline.business.SystemProperties;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 文件 工具类
 */
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    // 可支持文件后缀
    public static final List<String> list = Arrays.asList("doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "pdf");
    // window系统前缀
    private static final String WINDOW_NAME = "window";
    // linux系统前缀
    private static final String LINUX_NAME = "linux";

    private FileUtil() {
    }

    /**
     * 上传文件
     *
     * @param file
     * @return 返回文件存储相对路径
     * @throws Exception
     */
    public static String fileUpload(MultipartFile file) throws Exception {
        // 获取 文件存储路径前缀
        String filePrefix = getFilePrefix();
        File dir = new File(filePrefix);
        if (!dir.exists()) {
            Optional.ofNullable(dir.mkdirs() == false ? null : "").orElseThrow(() -> new Exception("创建文件存储前缀目录失败"));
        }
        String filename = file.getOriginalFilename();
        int index = filename.lastIndexOf(".");
        String filetype = filename.substring(index).toLowerCase();
        // 生成一个独一无二的文件名称
        filename = getFileName();
        String src = File.separator + getFilePath(filename + filetype);
        File f = new File(filePrefix + src);
        if (!f.exists()) {
            if (!f.getParentFile().exists()) {
                Optional.ofNullable(f.getParentFile().mkdirs() == false ? null : "").orElseThrow(() -> new Exception("创建文件失败"));
            }
            Optional.ofNullable(f.createNewFile() == false ? null : "").orElseThrow(() -> new Exception("创建文件失败"));
        }
        byte[] bytes = file.getBytes();
        try (
                OutputStream out = new FileOutputStream(f);
        ) {
            out.write(bytes);
            out.flush();
        }
        return src;
    }

    /**
     * 将文件使用openoffice转换为pdf后，再转换为html文件
     *
     * @param fileUrl 文件相对路径
     * @return filePreviewUrl:文件可预览相对路径
     * @throws Exception
     */
    public static String getFilePreviewUrl(String fileUrl) throws Exception {
        // 获取 文件存储路径前缀
        String filePrefix = getFilePrefix();
        File dir = new File(filePrefix);
        if (!dir.exists()) {
            Optional.ofNullable(dir.mkdirs() == false ? null : "").orElseThrow(() -> new Exception("创建文件存储前缀目录失败"));
        }
        int index = fileUrl.lastIndexOf(".");
        String filetype = fileUrl.substring(index).toLowerCase();
        // 生成一个独一无二的文件名称
        String filename = getFileName();
        String filePreviewUrl;
        if (".pdf".equals(filetype)) {// pdf文件不用转换
            filePreviewUrl = fileUrl;
            return pdfToImage(filePrefix, filePreviewUrl);
        } else if (".xls".equals(filetype) || ".xlsx".equals(filetype)) {
            filePreviewUrl = File.separator + getFilePath(filename + ".html");
            officeToFile(filePrefix + fileUrl, filePrefix + filePreviewUrl);
            return filePreviewUrl;
        } else if (list.indexOf(filetype.substring(1)) > -1) {
            filePreviewUrl = File.separator + getFilePath(filename + ".pdf");
            officeToFile(filePrefix + fileUrl, filePrefix + filePreviewUrl);
            return pdfToImage(filePrefix, filePreviewUrl);
        } else {
            throw new Exception("只能进行" + list.toString() + "文件后缀的转换");
        }
    }

    /**
     * office文档转换
     *
     * @param sourceFile 要转换文档绝对路径
     * @param destFile   转换后文件绝对路径
     * @return
     */
    public static void officeToFile(String sourceFile, String destFile) throws Exception {
        File inputFile = new File(sourceFile);
        if (!inputFile.exists()) {
            throw new Exception("找不到源文件");
        }
        // 如果目标路径不存在, 则新建该路径
        File outputFile = new File(destFile);
        if (!outputFile.getParentFile().exists()) {
            Optional.ofNullable(outputFile.getParentFile().mkdirs() == false ? null : "").orElseThrow(() -> new Exception("创建文件失败"));
        }
        if (!outputFile.exists()) {
            Optional.ofNullable(outputFile.createNewFile() == false ? null : "").orElseThrow(() -> new Exception("创建文件失败"));
        }
        logger.info("----------------开始转换文件:" + sourceFile);
        // 转换
        OpenOfficeConSingleton.getConverter().convert(inputFile, outputFile);
        logger.info("----------------结束转换文件:" + sourceFile);
    }

    /**
     * 获取 文件存储路径前缀
     *
     * @return
     */
    public static String getFilePrefix() {
        String os = System.getProperty("os.name").toLowerCase();
        String filePrefix;
        if (os.startsWith(WINDOW_NAME)) {
            filePrefix = SystemProperties.windowsFilePath;
        } else if (os.startsWith(LINUX_NAME)) {
            filePrefix = SystemProperties.linuxFilePath;
        } else {
            filePrefix = "";
        }
        return filePrefix;
    }

    /**
     * 返回系统文件存储规则路径
     * year/month/day
     *
     * @return
     */
    private static String getFilePath(String filename) {
        StringBuffer filePath = new StringBuffer();
        LocalDateTime localDateTime = LocalDateTime.now();
        filePath.append(localDateTime.getYear()).append(File.separator)
                .append(localDateTime.getMonthValue()).append(File.separator)
                .append(localDateTime.getDayOfMonth()).append(File.separator)
                .append(filename);
        return filePath.toString();
    }

    /**
     * 生成一个独一无二的文件名称
     *
     * @return
     */
    private static String getFileName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 将pdf文件转换为html
     *
     * @param filePrefix  文件存储路径前缀
     * @param pdfFilePath
     */
    public static String pdfToImage(String filePrefix, String pdfFilePath) throws Exception {
        String filePath = pdfFilePath.substring(0, pdfFilePath.lastIndexOf("."));
        StringBuffer buffer = new StringBuffer();
        try {
            // 构造html文件
            buffer.append("<!DOCTYPE html>\r\n");
            buffer.append("<html>\r\n");
            buffer.append("<head>\r\n");
            buffer.append("<meta charset=\"UTF-8\">\r\n");
            buffer.append("<style>\r\n");
            buffer.append("img{\n background-color:#cfdeee; \n text-align:center; \n width:100%; " + "\n }\r\n");
            buffer.append("</style>\r\n");
            buffer.append("</head>\r\n");
            buffer.append("<body>\r\n");
            //pdf文件
            File pdfFile = new File(filePrefix + pdfFilePath);
            PDDocument document = PDDocument.load(pdfFile, (String) null);
            int numberOfPages = document.getNumberOfPages();
            long size = pdfFile.length() / 1024;
            long start = System.currentTimeMillis();
            logger.info("====> pdf : " + pdfFilePath + ", NumberOfPages : " + numberOfPages + ", size ：" + size + "kb.");
            PDFRenderer reader = new PDFRenderer(document);
            for (int i = 0; i < numberOfPages; i++) {
                BufferedImage image = reader.renderImage(i, 1.5f);
                // 截取调图片多余空白部分
                image = image.getSubimage(0, 110, image.getWidth(), image.getHeight() - 220);
                // 生成图片，保存位置
                File imgFile = new File(filePrefix + filePath + "_images_" + i + ".png");
                ImageIO.write(image, "png", imgFile);
                // 追加图片到网页文件中
                buffer.append("<img src=\"data:image/png;base64," + getBase64String(imgFile) + "\"/>\r\n");
                imgFile.deleteOnExit();
            }
            // 构造html文件
            buffer.append("</body>\r\n");
            buffer.append("</html>");
            long end = System.currentTimeMillis();
            // 转换时间
            long times = end - start;
            logger.info("===> Reading pdf times :" + times);
            document.close();
            // 生成html网页文件
            FileOutputStream fos = new FileOutputStream(filePrefix + filePath + ".html");
            logger.info(filePath + ".html");
            fos.write(buffer.toString().getBytes());
            fos.flush();
            fos.close();
            buffer.setLength(0);
        } catch (Exception e) {
            logger.info("====>Reader parse pdf to jpg error :" + e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return filePath + ".html";
    }

    /**
     * 将文件转换为base64编码
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static String getBase64String(File file) throws IOException {
        byte[] refereeFileOriginalBytes = FileUtils.readFileToByteArray(file);
        byte[] refereeFileBase64Bytes = Base64.encodeBase64(refereeFileOriginalBytes);
        return new String(refereeFileBase64Bytes, "UTF-8");
    }
}
