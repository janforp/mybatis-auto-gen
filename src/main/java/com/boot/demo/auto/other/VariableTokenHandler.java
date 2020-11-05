package com.boot.demo.auto.other;

import org.apache.ibatis.parsing.TokenHandler;

import java.util.Properties;

/**
 * 类说明：
 *
 * @author zhucj
 * @since 2020/3/12 - 下午9:25
 */
public class VariableTokenHandler implements TokenHandler {

    private Properties variables;

    public VariableTokenHandler(Properties variables) {
        this.variables = variables;
    }

    @Override
    public String handleToken(String content) {
        if (variables != null && variables.containsKey(content)) {
            return variables.getProperty(content);
        }
        return "${" + content + "}";
    }
}
