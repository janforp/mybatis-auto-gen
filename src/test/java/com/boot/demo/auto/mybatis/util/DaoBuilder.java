package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.TableInfo;

/**
 * DaoBuilder
 *
 * @author zhucj
 * @since 20220825
 */
public class DaoBuilder {

    public static String buildDao(TableInfo tableInfo, String daoPackage, String modalPackage) {
        StringBuilder buf = new StringBuilder(1024);
        String tableName = tableInfo.getTableName();
        String daoName = MyBatisGenUtils.getDaoNameByTableName(tableName);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String modalNameWithPackage = modalPackage + "." + modalName;
        String newLine = "\n";
        buf.append("package ").append(daoPackage).append(";").append(newLine);
        buf.append(newLine);
        buf.append("import ").append(modalNameWithPackage).append(";").append(newLine);
        buf.append(newLine);
        buf.append("import java.util.List;").append(newLine);
        buf.append(newLine);
        buf.append(MyBatisGenUtils.getAuthorInfo());
        buf.append("public interface ").append(daoName).append(" {").append(newLine);

        {
            buf.append("    ").append("int deleteByPrimaryKey(");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(javaType).append(" ").append(propertyName);
                i++;
            }
            buf.append(");").append(newLine).append(newLine);
        }

        {

            buf.append("    ").append("void insert(").append(modalName).append(" record);").append(newLine).append(newLine);
            buf.append("    ").append("void insertSelective(").append(modalName).append(" record);").append(newLine).append(newLine);
            buf.append("    ").append("void insertBatch(List<").append(modalName).append("> records);").append(newLine).append(newLine);
        }

        {
            buf.append("    ").append(modalName).append(" selectByPrimaryKey(");
            int i = 0;
            for (String column : tableInfo.getPrimaryKeys()) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(javaType).append(" ").append(propertyName);
                i++;
            }
            buf.append(");")
                    .append(newLine).append(newLine);
        }

        {
            buf.append("    ").append("int updateByPrimaryKeySelective(").append(modalName).append(" record);").append(newLine).append(newLine);
            buf.append("    ").append("int updateByPrimaryKey(").append(modalName).append(" record);").append(newLine);
        }

        buf.append("}");
        return buf.toString();
    }
}
