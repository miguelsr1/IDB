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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author xtecuan
 */
public class DateNullSerializer extends JsonSerializer<Date> {

    private static final String DEFAULT_DATE = "01/01/1900 00:00:00";
    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    public void serialize(Date t, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {

        if (t != null) {

            if (sdf.format(t).equals(DEFAULT_DATE)) {
                jgen.writeString("");
            } else {

                jgen.writeString(sdf.format(t));
            }
        } else {
            jgen.writeString("");
        }

    }

}
