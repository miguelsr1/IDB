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
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.web.jaxrs;

import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.iadb.ws.timeandlabor.web.xtecuannet.framework.configuration.ConfigurationFacade;

/**
 *
 * @author xtecuan
 */
public abstract class XApplicationBaseRestService {

    private static final Logger logger = Logger.getLogger(XApplicationBaseRestService.class);

    protected abstract ConfigurationFacade getConfig();

    protected boolean validateToken(String currentToken) {
        return getConfig().getApiToken().equals(currentToken);
    }
    
    protected Response forbiddenResponseForPassCode(String passCode) {
        String message = "";
        if (passCode == null || passCode.equals("")) {
            message = "Forbidden 403: You need to provide a valid passCode";
        } else {
            message = "Forbidden 403: The passCode provided is invalid : " + passCode;
        }
        return Response.status(Response.Status.FORBIDDEN).entity(message).type("text/plain").build();
    }
    
    private Response badRequestParams(String name, String value) {
        String message = "";
        if (value == null || value.equals("")) {
            message = "Bad Request 400: You need to provide a valid " + name;
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build();
    }
    
    public abstract Response root();

    public static Logger getLogger() {
        return logger;
    }
    
    
    
}
