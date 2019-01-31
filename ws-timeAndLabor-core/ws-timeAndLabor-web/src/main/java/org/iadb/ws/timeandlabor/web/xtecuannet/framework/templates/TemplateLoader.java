/*
 * Copyright 2016 xtecuan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.templates;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author xtecuan
 */
@Stateless
public class TemplateLoader {

    private static final Logger logger = Logger.getLogger(TemplateLoader.class);
    private static final String DEFAULT_PACKAGE_TEMPLATES = "/templates";
    private static final Configuration cfg = getConfiguration();

    public static Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(TemplateLoader.class, DEFAULT_PACKAGE_TEMPLATES);
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setAPIBuiltinEnabled(true);
//        DefaultObjectWrapper wrapper =new DefaultObjectWrapper(Configuration.VERSION_2_3_27);
//        wrapper.setUseAdaptersForContainers(true);
//        cfg.setObjectWrapper(wrapper);
        return cfg;
    }

    public String getFilledHtmlTemplate(Map<String, Object> data, String filename) {
        String result = "";
        try {
            Template template = cfg.getTemplate(filename);
            Writer stringWriter = new StringWriter();
            template.process(data, stringWriter);
            result = stringWriter.toString();
        } catch (Exception ex) {
            logger.error("Error processing template: " + filename, ex);
        }

        return result;
    }
    
    public String getFilledTemplate(Map<String, Object> data, String filename) {
        String result = "";
        try {
            Template template = cfg.getTemplate(filename);
            Writer stringWriter = new StringWriter();
            template.process(data, stringWriter);
            result = stringWriter.toString();
        } catch (Exception ex) {
            logger.error("Error processing template: " + filename, ex);
        }

        return result;
    }

    public String getFilledStringTemplate(Map<String, Object> data, String textTemplate) {
        String result = "";
        try {
            Template template = new Template("templateName0", new StringReader(textTemplate), cfg);
            Writer stringWriter = new StringWriter();
            template.process(data, stringWriter);
            result = stringWriter.toString();
        } catch (Exception e) {
            logger.error("Error processing textTemplate: " + textTemplate, e);
        }
        return result;
    }
}
