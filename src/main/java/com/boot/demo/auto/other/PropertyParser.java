package com.boot.demo.auto.other;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

import java.util.Properties;

/**
 * 类说明：
 *
 * @author zhucj
 * @since 2020/3/11 - 下午9:53
 */
public class PropertyParser {

    private PropertyParser() {
        // Prevent Instantiation
    }

    public static String parse(String string, Properties variables) {
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    //就是一个map，用相应的value替换key
    private static class VariableTokenHandler implements TokenHandler {

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

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("a", "b");

        System.out.println(parse("b", properties));
    }
}
