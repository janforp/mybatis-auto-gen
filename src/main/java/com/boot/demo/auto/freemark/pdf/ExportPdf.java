package com.boot.demo.auto.freemark.pdf;

import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * PdfGenerator
 *
 * @author zhucj
 * @since 20230323
 */
@SuppressWarnings("all")
public class ExportPdf {

    /**
     * 通过模板导出pdf文件
     *
     * @param data 数据
     * @param templateFileName 模板文件名
     * @param basePackagePath
     * @return
     * @throws Exception SIMSUN.TTC为宋体，
     */
    public ByteArrayOutputStream createPdf(Map data, String templateFileName, String basePackagePath) throws Exception {
        OutputStream out = new ByteArrayOutputStream();

        // 创建一个FreeMarker实例, 负责管理FreeMarker模板的Configuration实例
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
        // 指定FreeMarker模板文件的位置
        configuration.setClassForTemplateLoading(this.getClass(), basePackagePath);
        ITextRenderer renderer = new ITextRenderer();
        // 设置 css中 的字体样式（暂时仅支持宋体和黑体） 必须，不然中文不显示
        //String path = ExportPdf.class.getResource("").getPath();
        //String path = ExportPdf.class.getResource("/").getPath() + "templates/export/";
        String path = "/font/";
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("templates/font/" + "simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        // 设置模板的编码格式
        configuration.setEncoding(Locale.CHINA, "UTF-8");
        // 获取模板文件
        Template template = configuration.getTemplate(templateFileName, "UTF-8");
        StringWriter writer = new StringWriter();
        // 将数据输出到html中
        template.process(data, writer);
        writer.flush();

        String html = writer.toString();
        // 把html代码传入渲染器中
        renderer.setDocumentFromString(html);

        // 设置模板中的图片路径 （这里的images在resources目录下） 模板中img标签src路径需要相对路径加图片名 如<img src="images/xh.jpg"/>
        // String url = ImportExcelUtil.class.getClassLoader().getResource("images").toURI().toString();
        // renderer.getSharedContext().setBaseURL(url);
        renderer.layout();

        renderer.createPDF(out, false);
        renderer.finishPDF();
        out.flush();
        writer.close();
        return (ByteArrayOutputStream) out;
    }
}