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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import javax.ws.rs.ext.ContextResolver;

/**
 *
 * @author xtecuan
 */
//@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public JacksonConfig() throws Exception {

        objectMapper = new ObjectMapper()
                //                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                //                .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .setSerializationInclusion(JsonInclude.Include.ALWAYS)
                .registerModule(new JaxbAnnotationModule())
                .registerModule(new Jdk8Module());
        objectMapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
        //objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        //objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }

}
