package com.boot.demo.auto.freemark.pdf.remotetemplate;

import freemarker.cache.TemplateLoader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * AutoEscapeTemplateLoader
 *
 * @author zhucj
 * @since 20230323
 */
public class AutoEscapeTemplateLoader implements TemplateLoader {

    private static final String HTML_ESCAPE_PREFIX = "<#escape x as x?html>";

    private static final String HTML_ESCAPE_SUFFIX = "</#escape>";

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final TemplateLoader templateLoader;

    public AutoEscapeTemplateLoader(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return templateLoader.findTemplateSource(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return templateLoader.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Reader reader = templateLoader.getReader(templateSource, encoding);
        String templateText = IOUtils.toString(reader);
        String autoEscapedTemplateText = StringUtils.join(HTML_ESCAPE_PREFIX, LINE_SEPARATOR, templateText, LINE_SEPARATOR, HTML_ESCAPE_SUFFIX);
        return new StringReader(autoEscapedTemplateText);
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        templateLoader.closeTemplateSource(templateSource);
    }
}