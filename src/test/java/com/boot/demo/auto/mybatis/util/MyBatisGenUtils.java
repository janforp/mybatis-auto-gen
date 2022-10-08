package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.MybatisCodeGenerateTest;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyBatisGenUtils
 *
 * @author zhucj
 * @since 20220825
 */
@UtilityClass
public class MyBatisGenUtils {

    public static String getJavaTypeByJdbcType(String jdbcType) {
        if (jdbcType.equalsIgnoreCase("int") || jdbcType.equalsIgnoreCase("integer") || jdbcType.equalsIgnoreCase("tinyint")) {
            return "Integer";
        } else if (jdbcType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (jdbcType.equalsIgnoreCase("varchar") || jdbcType.equalsIgnoreCase("text") || jdbcType.equalsIgnoreCase("MEDIUMTEXT")) {
            return "String";
        } else if (jdbcType.equalsIgnoreCase("double")) {
            return "Double";
        } else if (jdbcType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (jdbcType.equalsIgnoreCase("decimal")) {
            return "BigDecimal";
        } else if (jdbcType.equalsIgnoreCase("datetime") || jdbcType.equalsIgnoreCase("timestamp") || jdbcType.equalsIgnoreCase("date")) {
            return "Date";
        } else if (jdbcType.equalsIgnoreCase("char")) {
            return "Integer";
        }
        // throw new RuntimeException("not supported JDBC Type : \"" + jdbcType + "\"!");
        return "String";
    }

    public static String getMobalNameByTableName(String tableName) {
        return initCapAndUnderlineToCamel(tableName);
    }

    public static String getSqlmapFileNameByTableName(String tableName) {
        return tableName + "-mapper.xml";
    }

    public static String getCustomSqlmapFileNameByTableName(String tableName) {
        return tableName + "-custom-mapper.xml";
    }

    public static String getDaoNameByTableName(String tableName) {
        return getMobalNameByTableName(tableName) + "DAO";
    }

    public static String getDaoImplNameByTableName(String tableName) {
        return getMobalNameByTableName(tableName) + "DAOImpl";
    }

    /**
     * 驼峰处理ly_share_user -> LyShareUser
     */
    private static String initCapAndUnderlineToCamel(String param) {
        String rs = underlineToCamel(param);
        return initCap(rs);
    }

    /**
     * 驼峰处理shop_name -> shopName
     */
    public static String underlineToCamel(String param) {
        if (param == null || param.equals("")) {
            return "";
        }
        Pattern p = Pattern.compile("_");
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        Character found = null;
        while (mc.find()) {
            builder.deleteCharAt(mc.start() - i);
            found = builder.charAt(mc.start() - i);
            builder.replace(mc.start() - i, mc.start() - i + 1, found
                    .toString().toUpperCase());
            i++;
        }
        return builder.toString();
    }

    public static String initCap(String src) {
        if (src == null) {
            return null;
        }
        if (src.length() > 1) {
            return src.substring(0, 1).toUpperCase() + src.substring(1);
        } else {
            return src.toUpperCase();
        }
    }

    public static String getAuthorInfo() {
        String newLine = "\n";
        return "/**" + newLine + " * Created by " + MybatisCodeGenerateTest.class.getName() + " on " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + newLine + " */" + newLine;
    }

    private static void moveFile(File fromFile, File toFile) {// 复制文件
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new FileInputStream(fromFile);// 创建文件输入流
            fos = new FileOutputStream(toFile);// 文件输出流
            byte[] buffer = new byte[1024];// 字节数组
            int len = -1;
            while ((len = is.read(buffer)) != -1) {// 将文件内容写到文件中
                fos.write(buffer, 0, len);
            }
            fromFile.delete();
        } catch (Exception e) {// 捕获异常
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();// 输入流关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();// 输出流关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param targetFile
     * @param content
     * @return
     */
    public static boolean writeText(File targetFile, String content,
            String charSet) {
        boolean flag = false;
        OutputStreamWriter out = null;
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            out = new OutputStreamWriter(new FileOutputStream(targetFile),
                    charSet);
            out.write(content.toCharArray());
            flag = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }
}
