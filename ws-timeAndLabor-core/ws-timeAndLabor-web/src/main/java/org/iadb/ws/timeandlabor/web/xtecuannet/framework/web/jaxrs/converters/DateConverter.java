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
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.web.jaxrs.converters;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.ext.ParamConverter;
import org.apache.log4j.Logger;

/**
 *
 * @author xtecuan
 */
public class DateConverter implements ParamConverter<Date> {

    private static final Logger LOGGER = Logger.getLogger(DateConverter.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    @Override
    public Date fromString(String parameter) {
        Date result = null;
        try {
            result = sdf.parse(parameter);
        } catch (Exception e) {
            LOGGER.error("Error converting " + parameter + " to java.util.Date: ", e);
        }
        return result;
    }

    @Override
    public String toString(Date date) {
        String result = null;
        if (date != null) {
            result = sdf.format(date);
        }else{
            result = "";
        }
        return result;
    }

}
