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
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.web.viewcontroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author xtecuan
 */
public final class ViewUtils {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ViewUtils.class);

    public static List<SelectItem> fromListToSelectItems(List list, Class clazz, String valueField, String labelField) {

        ClassMemberExtractor ext = new ClassMemberExtractor(clazz);
        List<SelectItem> items = new ArrayList<SelectItem>(0);
        for (Object object : list) {
            items.add(new SelectItem(ext.getValue(object, valueField), (String) ext.getValue(object, labelField)));
        }
        return items;
    }

    private static final class ClassMemberExtractor {

        private final Class myClass;

        public ClassMemberExtractor(Class myClass) {
            this.myClass = myClass;
        }

        public final Object getValue(Object instance, String fieldName) {
            Object value = null;
            Field field = null;
            try {
                field = myClass.getField(fieldName);

                value = field.get(instance);

            } catch (NoSuchFieldException ex) {
                logger.error("No Such Field Exception: ", ex);

            } catch (SecurityException ex) {
                logger.error("Security Exception: ", ex);
            } finally {
                return value;
            }
        }

        public Class getMyClass() {
            return myClass;
        }

    }
}
