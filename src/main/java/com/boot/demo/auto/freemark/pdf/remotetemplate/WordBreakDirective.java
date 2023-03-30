package com.boot.demo.auto.freemark.pdf.remotetemplate;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateNumberModel;
import freemarker.template.TemplateScalarModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static freemarker.template.ObjectWrapper.DEFAULT_WRAPPER;

/**
 * WordBreakDirective
 *
 * @author zhucj
 * @since 20230323
 */
public class WordBreakDirective implements TemplateDirectiveModel {

    private static final Logger LOG = LoggerFactory.getLogger(WordBreakDirective.class);

    private static final String FONT_SIZE = "fontSize";

    private static final String WIDTH = "width";

    private static final String INPUT = "input";

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {

        TemplateScalarModel input = (TemplateScalarModel) params.get(INPUT);
        TemplateNumberModel font = (TemplateNumberModel) params.get(FONT_SIZE);
        TemplateNumberModel cell = (TemplateNumberModel) params.get(WIDTH);
        int fontSize = font.getAsNumber().intValue();
        int cellWidth = cell.getAsNumber().intValue();
        int num = cellWidth * 2 / fontSize;
        //识别中文和英文数字占用字节
        env.setVariable("output", DEFAULT_WRAPPER.wrap(getBreak(input.getAsString(), num)));

        Writer out = env.getOut();
        out.write("");
        body.render(out);
    }

    /**
     * str为填充数据字符串，num为换行字节个数,强制换行的地方添加br标签
     *
     * @param str
     * @param num
     * @return 处理过的表格内容
     */
    private String getBreak(String str, int num) {
        if (str.length() * 2 < num) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        String[] strings = str.split("");
        int length = 0;
        //算法需要优化
        for (int i = 0; i < str.length(); i++) {
            int singleChar = strings[i].getBytes().length > 1 ? 2 : 1;
            if (length + singleChar < num) {
                length += singleChar;
            } else {
                length = singleChar;
                sb.append("<br>");
            }
            sb.append(strings[i]);
        }
        LOG.info("pdf表格换行结果:" + sb.toString());
        return sb.toString();
    }

    public static void main(String[] args) {
        WordBreakDirective wordBreakDirective = new WordBreakDirective();
        String str1 = "啊sdr45额34t个etgr54我43";
        System.out.println(wordBreakDirective.getBreak(str1, 8));

        Long start1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            isContainChinese("啊");
        }
        Long end1 = System.currentTimeMillis();
        System.out.println(end1 - start1);
        Long start2 = System.currentTimeMillis();
        int singleChar = 0;
        for (int i = 0; i < 1000; i++) {
            singleChar = "啊".getBytes().length > 1 ? 2 : 1;
        }
        Long end2 = System.currentTimeMillis();
        System.out.println(end2 - start2);
        System.out.println(singleChar);

    }

    //判断中文的方法相较于判断字节数方法更耗时
    private static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
