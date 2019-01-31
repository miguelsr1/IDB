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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author xtecuan
 */
public class DateNullDeSerializer extends JsonDeserializer<Date> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        Date result = null;
        JsonNode node = jp.readValueAsTree();
        try {
            if (node.asText().isEmpty()) {
                result = null;
            } else {
                result = sdf.parse(node.asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
