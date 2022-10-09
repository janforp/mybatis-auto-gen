package com.boot.demo.auto.mybatis.util;

import com.boot.demo.auto.mybatis.domain.EnvInfo;
import com.boot.demo.auto.mybatis.domain.TableInfo;

import java.util.List;

/**
 * DaoBuilder
 *
 * @author zhucj
 * @since 20220825
 */
class DaoBuilder {

    static String buildDao(TableInfo tableInfo, String daoPackage, String modalPackage) {
        StringBuilder buf = new StringBuilder(1024);
        String tableName = tableInfo.getTableName();
        String daoName = MyBatisGenUtils.getDaoNameByTableName(tableName);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String modalNameWithPackage = modalPackage + "." + modalName;
        buf.append("package ").append(daoPackage).append(";").append(EnvInfo.NEW_LINE);
        buf.append(EnvInfo.NEW_LINE);
        buf.append("import ").append(modalNameWithPackage).append(";").append(EnvInfo.NEW_LINE);
        buf.append("import org.springframework.stereotype.Repository;").append(EnvInfo.NEW_LINE);
        buf.append(EnvInfo.NEW_LINE);
        buf.append("import java.util.List;").append(EnvInfo.NEW_LINE);
        buf.append(EnvInfo.NEW_LINE);
        buf.append(MyBatisGenUtils.getAuthorInfo());
        buf.append("@Repository").append(EnvInfo.NEW_LINE);
        buf.append("public interface ").append(daoName).append(" {").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);
        // 方法
        buf.append(buildMethods(tableInfo));
        buf.append("}");
        return buf.toString();
    }

    private static String buildMethods(TableInfo tableInfo) {
        List<String> primaryKeyList = tableInfo.getPrimaryKeys();
        StringBuilder buf = new StringBuilder(1024);
        String modalName = MyBatisGenUtils.getMobalNameByTableName(tableInfo.getTableName());
        String initCapModalName = MyBatisGenUtils.toCamel(modalName);

        // 添加
        {

            buf.append("    /**\n" + "     * 添加\n" + "     *\n" + "     * @param ").append(initCapModalName).append(" 记录\n").append("     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append("void insert(").append(modalName).append(" ").append(initCapModalName).append(");").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);

            buf.append("    /**\n" + "     * 选择性添加\n" + "     *\n" + "     * @param ").append(initCapModalName).append(" 记录\n").append("     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append("void insertSelective(").append(modalName).append(" ").append(initCapModalName).append(");").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);

            buf.append("    /**\n" + "     * 批量添加\n" + "     *\n" + "     * @param ").append(initCapModalName).append("List 记录\n").append("     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append("void insertBatch(List<").append(modalName).append("> ").append(" ").append(initCapModalName).append("List);").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);
        }

        // 删除
        {
            buf.append("    /**\n" + "     * 根据主键删除\n" + "     *\n" + "     * @param id 主键\n" + "     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append("void deleteByPrimaryKey(");
            int i = 0;

            for (String column : primaryKeyList) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(javaType).append(" ").append(propertyName);
                i++;
            }
            buf.append(");").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);
        }

        // 修改
        {
            buf.append("    /**\n" + "     * 选择性修改\n" + "     *\n" + "     * @param ").append(initCapModalName).append(" 记录\n").append("     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append("void updateByPrimaryKeySelective(").append(modalName).append(" ").append(initCapModalName).append(");").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);

            buf.append("    /**\n" + "     * 根据主键修改\n" + "     *\n" + "     * @param ").append(initCapModalName).append(" 记录\n").append("     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append("void updateByPrimaryKey(").append(modalName).append(" ").append(initCapModalName).append(");").append(EnvInfo.NEW_LINE).append(EnvInfo.NEW_LINE);
        }

        // 查询
        {
            buf.append("    /**\n" + "     * 根据主键查询\n" + "     *\n" + "     * @param id 主键\n" + "     * @return 记录\n" + "     */").append(EnvInfo.NEW_LINE);
            buf.append("    ").append(modalName).append(" getById(");
            int i = 0;
            for (String column : primaryKeyList) {
                String propertyName = MyBatisGenUtils.underlineToCamel(column);
                String javaType = MyBatisGenUtils.getJavaTypeByJdbcType(tableInfo.getColumnTypes().get(column));
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(javaType).append(" ").append(propertyName);
                i++;
            }
            buf.append(");").append(EnvInfo.NEW_LINE);
        }

        return buf.toString();
    }
}
