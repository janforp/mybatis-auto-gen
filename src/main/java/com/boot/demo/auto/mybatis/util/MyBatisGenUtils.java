package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyBatisGenUtils
 *
 * @author zhucj
 * @since 20220825
 */
@UtilityClass
class MyBatisGenUtils {

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
        return initCapAndUnderlineToCamel(tableName) + "Mapper.xml";
    }

    public static String getDaoNameByTableName(String tableName) {
        return getMobalNameByTableName(tableName) + "DAO";
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
        char found;
        while (mc.find()) {
            builder.deleteCharAt(mc.start() - i);
            found = builder.charAt(mc.start() - i);
            builder.replace(mc.start() - i, mc.start() - i + 1, Character.toString(found).toUpperCase());
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

    public static String toCamel(String param) {
        if (param == null || param.equals("")) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }

    public static String getAuthorInfo(TableInfo tableInfo) {
        return "/**\n"
                + " * " + tableInfo.getTableComment() + "\n" + "\n"
                + " * @author " + EnvInfo.AUTHOR + "\n"
                + " * @since " + EnvInfo.VERSION + "\n"
                + " */\n";
    }

    @SuppressWarnings("all")
    public static void writeText(File targetFile, String content, String charSet) {
        OutputStreamWriter out = null;
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        try {
            out = new OutputStreamWriter(new FileOutputStream(targetFile), charSet);
            out.write(content.toCharArray());
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
    }
}