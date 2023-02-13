package com.boot.demo.auto.other;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZIP压缩文件操作工具类
 * 支持密码
 * 依赖zip4j开源项目(http://www.lingala.net/zip4j/)
 *
 * @author kangzs
 */
@UtilityClass
@Slf4j
public class Zip4jUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(Zip4jUtil.class);

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zip 指定的ZIP压缩文件
     * @param dest 解压目录
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public File[] unzip(String zip, String dest, String password) throws ZipException {
        File zipFile = new File(zip);
        return unzip(zipFile, dest, password);
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到当前目录
     *
     * @param zip 指定的ZIP压缩文件
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public File[] unzip(String zip, String password) throws ZipException {
        File zipFile = new File(zip);
        File parentDir = zipFile.getParentFile();
        return unzip(zipFile, parentDir.getAbsolutePath(), password);
    }

    public static void main(String[] args) throws ZipException {
        String path = "/Users/zhuchenjian/Desktop/zip/dfsfsf.zip";
        File[] unzip = unzip(path, null);
        List<File> files = new ArrayList<>();
        for (File file : unzip) {
            if (file.toString().contains("__MACOSX")) {
                file.delete();
                continue;
            }
            files.add(file);
        }

        for (File file : files) {
            String parentName = file.getParentFile().getName();
            String name = file.getName();
            System.out.println("文件夹名称 = " + parentName + " , 文件名称 = " + name);
        }
    }

    /**
     * 使用给定密码解压指定的ZIP压缩文件到指定目录
     * <p>
     * 如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
     *
     * @param zipFile 指定的ZIP压缩文件
     * @param dest 解压目录
     * @param password ZIP文件的密码
     * @return 解压后文件数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public File[] unzip(File zipFile, String dest, String password) throws ZipException {
        ZipFile zFile = new ZipFile(zipFile);

        zFile.setFileNameCharset("UTF-8");
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法,可能被损坏.");
        }
        File destDir = new File(dest);
        if (destDir.isDirectory() && !destDir.exists()) {
            if (!destDir.mkdir()) {
                System.out.println("创建目录失败：" + dest);
            }
        }
        if (zFile.isEncrypted()) {
            zFile.setPassword(password.toCharArray());
        }
        zFile.extractAll(dest);

        List<FileHeader> headerList = zFile.getFileHeaders();
        List<File> extractedFileList = new ArrayList<>();
        for (FileHeader fileHeader : headerList) {
            if (!fileHeader.isDirectory()) {
                extractedFileList.add(new File(destDir, fileHeader.getFileName()));
            }
        }
        File[] extractedFiles = new File[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    /**
     * 压缩指定文件到当前文件夹
     *
     * @param src 要压缩的指定文件
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public String zip(String src) {
        return zip(src, null);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到当前目录
     *
     * @param src 要压缩的文件
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public String zip(String src, String password) {
        return zip(src, null, password);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到当前目录
     *
     * @param src 要压缩的文件
     * @param dest 压缩文件存放路径
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public String zip(String src, String dest, String password) {
        return zip(src, dest, true, password);
    }

    /**
     * 使用给定密码压缩指定文件或文件夹到指定位置.
     * <p>
     * dest可传最终压缩文件存放的绝对路径,也可以传存放目录,也可以传null或者"".<br />
     * 如果传null或者""则将压缩文件存放在当前目录,即跟源文件同目录,压缩文件名取源文件名,以.zip为后缀;<br />
     * 如果以路径分隔符(File.separator)结尾,则视为目录,压缩文件名取源文件名,以.zip为后缀,否则视为文件名.
     *
     * @param src 要压缩的文件或文件夹路径
     * @param dest 压缩文件存放路径
     * @param isCreateDir 是否在压缩文件里创建目录,仅在压缩文件为目录时有效.<br />
     * 如果为false,将直接压缩目录下文件到压缩文件.
     * @param password 压缩使用的密码
     * @return 最终的压缩文件存放的绝对路径, 如果为null则说明压缩失败.
     */
    public static String zip(String src, String dest, boolean isCreateDir, String password) {
        File srcFile = new File(src);
        dest = buildDestinationZipFilePath(srcFile, dest);
        ZipParameters parameters = new ZipParameters();

        // 压缩方式
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // 压缩级别
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        if (!StringUtils.isEmpty(password)) {
            parameters.setEncryptFiles(true);
            // 加密方式
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
            parameters.setPassword(password.toCharArray());
        }
        try {
            ZipFile zipFile = new ZipFile(dest);
            if (srcFile.isDirectory()) {
                // 如果不创建目录的话,将直接把给定目录下的文件压缩到压缩文件,即没有目录结构
                if (!isCreateDir) {
                    File[] subFiles = srcFile.listFiles();
                    ArrayList<File> temp = new ArrayList<>();
                    if (null != subFiles) {
                        Collections.addAll(temp, subFiles);
                    }
                    zipFile.addFiles(temp, parameters);
                    return dest;
                }
                zipFile.addFolder(srcFile, parameters);
            } else {
                zipFile.addFile(srcFile, parameters);
            }
            return dest;
        } catch (ZipException e) {
            System.out.println("使用给定密码压缩指定文件或文件夹到指定位置时出现ZipException异常");
        }
        return null;
    }

    /**
     * 构建压缩文件存放路径,如果不存在将会创建 传入的可能是文件名或者目录,也可能不传,此方法用以转换最终压缩文件的存放路径
     *
     * @param srcFile 源文件
     * @param destParam 压缩目标路径
     * @return 正确的压缩文件存放路径
     */
    private static String buildDestinationZipFilePath(File srcFile, String destParam) {
        if (StringUtils.isEmpty(destParam)) {
            if (srcFile.isDirectory()) {
                destParam = srcFile.getParent() + File.separator + srcFile.getName() + ".zip";
            } else {
                String fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destParam = srcFile.getParent() + File.separator + fileName + ".zip";
            }
        } else {
            // 在指定路径不存在的情况下将其创建出来
            createDestDirectoryIfNecessary(destParam);
            if (destParam.endsWith(File.separator)) {
                String fileName = "";
                if (srcFile.isDirectory()) {
                    fileName = srcFile.getName();
                } else {
                    fileName = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                }
                destParam += fileName + ".zip";
            }
        }
        return destParam;
    }

    /**
     * 在必要的情况下创建压缩文件存放目录,比如指定的存放路径并没有被创建
     *
     * @param destParam 指定的存放路径,有可能该路径并没有被创建
     */
    private static void createDestDirectoryIfNecessary(String destParam) {
        File destDir = null;
        if (destParam.endsWith(File.separator)) {
            destDir = new File(destParam);
        } else {
            destDir = new File(destParam.substring(0, destParam.lastIndexOf(File.separator)));
        }
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                System.out.println("创建目录失败");
            }
        }
    }

    public static InputStream getFileInputStreamFromUrl(String path) {
        try {
            URL url = new URL(path);
            return url.openStream();
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * oss文件压缩成zip文件
     *
     * @param ossFileList oss url地址列表
     * @param zipName zip文件名
     * @return zip文件路径
     */
    public static String ossFileToZip(List<Pair<String, String>> ossFileList, String zipName) {
        if (CollectionUtils.isEmpty(ossFileList)) {
            return null;
        }

        String root = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
        String excelFolder = root + generateRandomString() + File.separator;
        String zipPath = excelFolder + zipName + ".zip";
        buildDestinationZipFilePath(new File(zipPath), excelFolder);
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipPath));
            for (Pair<String, String> ossFile : ossFileList) {
                String fileUrl = ossFile.getRight();
                //url 可能是加签加密的 需要舍弃 ？ 后面的部分
                String fileName = ossFile.getLeft() + StringUtils.split(fileUrl.substring(fileUrl.lastIndexOf(".")), "?")[0];
                zos.putNextEntry(new ZipEntry(fileName));
                InputStream in = getFileInputStreamFromUrl(fileUrl);
                try {
                    StreamUtils.copy(in, zos);
                } finally {
                    in.close();
                }
            }
        } catch (IOException e) {
            System.out.println("创建目录失败");
            zipPath = null;
        } finally {
            if (null != zos) {
                try {
                    zos.closeEntry();
                } catch (Exception e) {
                    System.out.println("创建目录失败");
                    zipPath = null;
                }

                try {
                    zos.close();
                } catch (Exception e) {
                    System.out.println("创建目录失败");
                    zipPath = null;
                }
            }
        }
        return zipPath;
    }

    /**
     * 生成随机时间戳字符串
     *
     * @return
     */
    private static String generateRandomString() {
        int randomNum = RandomUtils.nextInt(100000);
        return System.currentTimeMillis() + "_" + randomNum;
    }

}