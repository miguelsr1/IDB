/*
 * Copyright 2018 xtecuan.
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
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.queries;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.ejb.Stateless;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author xtecuan
 */
@Stateless
public class QueryLoader {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(QueryLoader.class);
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_PACKAGE_FROM = "./queries/";
    private static final String DEFAULT_PACKAGE_HTML = "./html/";

    public String getQueryUsingFilename(String filename) {
        String result = null;
        try {
            result = IOUtils.resourceToString(
                    DEFAULT_PACKAGE_FROM + filename,
                    Charset.forName(DEFAULT_CHARSET),
                    Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            logger.error("Error loading query from file: " + DEFAULT_PACKAGE_FROM + filename, e);
        }
        return result;
    }

    public List<String> getSQLSentencesUsingFilename(String filename) {
        List<String> sql = new ArrayList<>();
        String sqlFull = getQueryUsingFilename(filename);
        int i = 0;
        Scanner s = new Scanner(sqlFull);
        s.useDelimiter(";\n");
        while (s.hasNext()) {
            String c = s.next();
            sql.add(c);
        }
        return sql;
    }

    public String getQueryUsingFilename(String packageName, String filename) {
        String result = null;
        try {
            result = IOUtils.resourceToString(
                    packageName + filename,
                    Charset.forName(DEFAULT_CHARSET),
                    Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            logger.error("Error loading query from file: " + packageName + filename, e);
        }
        return result;
    }

    public String getHtmlTemplate(String filename) {
        return getQueryUsingFilename(DEFAULT_PACKAGE_HTML, filename);
    }

    public String getSQLTemplate(String filename) {
        return getQueryUsingFilename(filename);
    }

    public String getFilledHtmlTemplate(Map<String, String> data, String filename) {
        String template = getHtmlTemplate(filename);

        for (String key : data.keySet()) {
            template = template.replaceAll("\\$" + key, data.get(key));
        }

        return template;
    }

    public String getFilledSQLTemplate(Map<String, String> data, String filename) {
        String template = getSQLTemplate(filename);
        for (String key : data.keySet()) {
            template = template.replaceAll("\\$" + key, data.get(key));
        }
        return template;
    }

}
