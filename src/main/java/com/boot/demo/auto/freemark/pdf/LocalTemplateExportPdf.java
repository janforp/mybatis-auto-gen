package com.boot.demo.auto.freemark.pdf;

import com.boot.demo.auto.common.Constant;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.experimental.UtilityClass;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * PdfGenerator
 *
 * @author zhucj
 * @since 20230323
 */
@UtilityClass
public class LocalTemplateExportPdf {

    /**
     * 通过模板导出pdf文件
     *
     * @param dataModel 数据
     * @param templateFileName 模板文件名
     * @param basePackagePath 模版地址
     * @return 文件流
     */
    public ByteArrayOutputStream createPdf(Map<String, Object> dataModel, String templateFileName, String basePackagePath) throws DocumentException, IOException, TemplateException {
        // 获取模板文件
        Template template = getTemplate(templateFileName, basePackagePath);
        // html 内容
        String html = processDataAndReturnHtml(template, dataModel);
        // 文本渲染器
        ITextRenderer renderer = getRenderer(html);
        // 把内容输出到字节流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        renderer.createPDF(out, false);
        renderer.finishPDF();
        out.flush();
        return out;
    }

    private Template getTemplate(String templateFileName, String basePackagePath) throws IOException {
        // 创建一个FreeMarker实例, 负责管理FreeMarker模板的Configuration实例
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        configuration.setDefaultEncoding(Constant.UTF_8);
        // 指定FreeMarker模板文件的位置
        configuration.setClassForTemplateLoading(LocalTemplateExportPdf.class, basePackagePath);
        // 设置模板的编码格式
        configuration.setEncoding(Locale.CHINA, Constant.UTF_8);
        // 获取模板文件
        return configuration.getTemplate(templateFileName, Constant.UTF_8);
    }

    private String processDataAndReturnHtml(Template template, Map<String, Object> dataModel) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        // 将数据输出到html中
        template.process(dataModel, writer);
        writer.flush();
        // html 内容
        String html = writer.toString();
        writer.close();
        return html;
    }

    private ITextRenderer getRenderer(String html) throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        // 设置 css中 的字体样式（暂时仅支持宋体和黑体） 必须，不然中文不显示
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("templates/font/" + "simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        // 把html代码传入渲染器中
        renderer.setDocumentFromString(html);
        // 设置模板中的图片路径 （这里的images在resources目录下） 模板中img标签src路径需要相对路径加图片名 如<img src="images/xh.jpg"/>
        renderer.layout();
        return renderer;
    }
}