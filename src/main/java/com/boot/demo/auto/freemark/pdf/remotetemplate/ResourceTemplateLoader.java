package com.boot.demo.auto.freemark.pdf.remotetemplate;

import freemarker.cache.URLTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * ResourceTemplateLoader
 *
 * @author zhucj
 * @since 20230323
 */
public class ResourceTemplateLoader extends URLTemplateLoader {

    private static final transient Logger LOG = LoggerFactory.getLogger(ResourceTemplateLoader.class);

    private String url;

    public ResourceTemplateLoader(String url) {
        this.url = url;
    }

    /**
     * 这个path参数，可以只是文件名，或者文件绝对路径
     */
    @Override
    protected URL getURL(String path) {
        URL urlObj;
        try {
            urlObj = new URL(url + path);
            return urlObj;
        } catch (MalformedURLException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }
}