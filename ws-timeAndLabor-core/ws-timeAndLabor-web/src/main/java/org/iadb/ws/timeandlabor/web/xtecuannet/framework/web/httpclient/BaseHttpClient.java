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
package org.iadb.ws.timeandlabor.web.xtecuannet.framework.web.httpclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.iadb.ws.timeandlabor.web.xtecuannet.framework.configuration.ConfigurationFacade;


/**
 *
 * @author xtecuan
 */
public abstract class BaseHttpClient {

    public static final String RESULT = "CLIENT_RESULT";

    public static final String STATUS = "CLIENT_STATUS";

    private static final Logger LOGGER = Logger.getLogger(BaseHttpClient.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public abstract ConfigurationFacade getConfig();

    public abstract String getAuthorizationHeaderKey();

    public abstract String getBaseURL();

    private HttpHost getHttpProxy() {
        return new HttpHost(getConfig().getValue("http.proxy.host"), getConfig().getInteger("http.proxy.port"));
    }

    private Boolean isProxyEnabled() {
        return Boolean.valueOf(getConfig().getValue("http.proxy.enabled"));
    }

    private String getHttpProxyUsername() {
        return getConfig().getValue("http.proxy.user");
    }

    private String getHttpProxyPassword() {
        return getConfig().getValue("http.proxy.pass");
    }

    private Boolean isUserAndPasswordSet() {
        Boolean result = Boolean.FALSE;
        String user = getHttpProxyUsername();
        String pass = getHttpProxyPassword();
        if ((user != null && !user.equals("")) && (pass != null && !pass.equals(""))) {
            result = Boolean.TRUE;
        }

        return result;
    }

    private Header getXDefaultHeader() {
        return new BasicHeader("X-Default-Header", "Default Header: " + getClass().getCanonicalName());
    }

    private Header getUserAgent() {
        return new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
    }

    private Header getAcceptHeader(String accept) {
        return new BasicHeader("accept", accept);
    }

    public String getJsonApplication() {
        return "application/json";
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public Header getAuthorizationHeader() {
        return new BasicHeader("Authorization", "bearer " + getConfig().getValue(getAuthorizationHeaderKey()));
    }

    public String writeToJson(Object obody) {
        String result = null;
        try {
            result = MAPPER.writeValueAsString(obody);
        } catch (JsonProcessingException ex) {
            getLogger().error("Error writing to Json the Object of class: " + obody.getClass(), ex);
        }
        return result;
    }

    public <T extends Object> T parseResult(Map<String, Object> result, Class<T> valueType) {
        if (result.containsKey(RESULT)) {
            try {
                return MAPPER.readValue((String) result.get(RESULT), valueType);
            } catch (IOException ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public <T extends Object> T doPostAndParseResult(String requestURL, String body, Class<T> valueType) {
        return parseResult(doPost(requestURL, body), valueType);
    }

    public Map<String, Object> doPost(String requestURL) {
        Map<String, Object> resultResponse = new HashMap<>();
        List<Header> defaultHeaders = Arrays.asList(getXDefaultHeader(), getUserAgent(), getAcceptHeader(getJsonApplication()));
        if (getAuthorizationHeaderKey() != null && getAuthorizationHeaderKey().length() > 0) {
            defaultHeaders.add(getAuthorizationHeader());
        }
        CloseableHttpClient httpclient = null;
        if (isProxyEnabled() && isUserAndPasswordSet()) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();

            credsProvider.setCredentials(new AuthScope(getHttpProxy(), AuthScope.ANY_REALM, "ntlm"),
                    new NTCredentials(getHttpProxyUsername(), getHttpProxyPassword(), getConfig().getValue("http.proxy.host"), getConfig().getValue("http.proxy.domain")));
            httpclient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        } else {
            httpclient = HttpClients.custom()
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        }

        try {

            HttpPost post = new HttpPost(requestURL);
            if (isProxyEnabled()) {
                RequestConfig config = RequestConfig.custom()
                        .setProxy(getHttpProxy())
                        .build();
                post.setConfig(config);
            }

            System.out.println("Executing request " + post.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(post);
            try {
                String myResult = EntityUtils.toString(response.getEntity());
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(myResult);
                resultResponse.put(STATUS, response.getStatusLine());
                resultResponse.put(RESULT, myResult);
            } finally {
                response.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return resultResponse;
    }

    public Map<String, Object> doPost(String requestURL, String body) {
        Map<String, Object> resultResponse = new HashMap<>();
        List<Header> defaultHeaders = Arrays.asList(getXDefaultHeader(), getUserAgent(), getAcceptHeader(getJsonApplication()));
        if (getAuthorizationHeaderKey() != null && getAuthorizationHeaderKey().length() > 0) {
            defaultHeaders.add(getAuthorizationHeader());
        }
        CloseableHttpClient httpclient = null;
        if (isProxyEnabled() && isUserAndPasswordSet()) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();

            credsProvider.setCredentials(new AuthScope(getHttpProxy(), AuthScope.ANY_REALM, "ntlm"),
                    new NTCredentials(getHttpProxyUsername(), getHttpProxyPassword(), getConfig().getValue("http.proxy.host"), getConfig().getValue("http.proxy.domain")));
            httpclient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        } else {
            httpclient = HttpClients.custom()
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        }

        try {

            HttpEntity reqEntity = EntityBuilder.create()
                    .setText(body)
                    .setContentType(ContentType.APPLICATION_JSON)
                    .build();
            HttpPost post = new HttpPost(requestURL);
            if (isProxyEnabled()) {
                RequestConfig config = RequestConfig.custom()
                        .setProxy(getHttpProxy())
                        .build();
                post.setConfig(config);
            }
            post.setEntity(reqEntity);
            System.out.println("Executing request " + post.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(post);
            try {
                String myResult = EntityUtils.toString(response.getEntity());
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(myResult);
                resultResponse.put(STATUS, response.getStatusLine());
                resultResponse.put(RESULT, myResult);
            } finally {
                response.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return resultResponse;
    }

    public <T extends Object> T doPatchAndParseResult(String requestURL, String body, Class<T> valueType) {
        return parseResult(doPatch(requestURL, body), valueType);
    }

    public Map<String, Object> doPatch(String requestURL, String body) {
        Map<String, Object> resultResponse = new HashMap<>();
        List<Header> defaultHeaders = Arrays.asList(getXDefaultHeader(), getUserAgent(), getAcceptHeader(getJsonApplication()));
        if (getAuthorizationHeaderKey() != null && getAuthorizationHeaderKey().length() > 0) {
            defaultHeaders.add(getAuthorizationHeader());
        }
        CloseableHttpClient httpclient = null;
        if (isProxyEnabled() && isUserAndPasswordSet()) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();

            credsProvider.setCredentials(new AuthScope(getHttpProxy(), AuthScope.ANY_REALM, "ntlm"),
                    new NTCredentials(getHttpProxyUsername(), getHttpProxyPassword(), getConfig().getValue("http.proxy.host"), getConfig().getValue("http.proxy.domain")));
            httpclient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        } else {
            httpclient = HttpClients.custom()
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        }

        try {

            HttpEntity reqEntity = EntityBuilder.create()
                    .setText(body)
                    .setContentType(ContentType.APPLICATION_JSON)
                    .build();
            HttpPatch patch = new HttpPatch(requestURL);
            if (isProxyEnabled()) {
                RequestConfig config = RequestConfig.custom()
                        .setProxy(getHttpProxy())
                        .build();
                patch.setConfig(config);
            }
            patch.setEntity(reqEntity);
            System.out.println("Executing request " + patch.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(patch);
            try {
                String myResult = EntityUtils.toString(response.getEntity());
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(myResult);
                resultResponse.put(STATUS, response.getStatusLine());
                resultResponse.put(RESULT, myResult);
            } finally {
                response.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return resultResponse;
    }

    public String encodeParamValue(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    public <T extends Object> T doGetAndParseResult(String requestURL, Class<T> valueType) {
        return parseResult(doGet(requestURL), valueType);
    }

    public Map<String, Object> doGet(String requestURL) {
        Map<String, Object> resultResponse = new HashMap<>();
        List<Header> defaultHeaders = Arrays.asList(getXDefaultHeader(), getUserAgent(), getAcceptHeader(getJsonApplication()));
        if (getAuthorizationHeaderKey() != null && getAuthorizationHeaderKey().length() > 0) {
            defaultHeaders.add(getAuthorizationHeader());
        }
        CloseableHttpClient httpclient = null;
        if (isProxyEnabled() && isUserAndPasswordSet()) {
            CredentialsProvider credsProvider = new BasicCredentialsProvider();

            credsProvider.setCredentials(new AuthScope(getHttpProxy(), AuthScope.ANY_REALM, "ntlm"),
                    new NTCredentials(getHttpProxyUsername(), getHttpProxyPassword(), getConfig().getValue("http.proxy.host"), getConfig().getValue("http.proxy.domain")));
            httpclient = HttpClients.custom()
                    .setDefaultCredentialsProvider(credsProvider)
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        } else {
            httpclient = HttpClients.custom()
                    .setDefaultHeaders(defaultHeaders)
                    .build();
        }

        try {

            HttpGet get = new HttpGet(requestURL);
            if (isProxyEnabled()) {
                RequestConfig config = RequestConfig.custom()
                        .setProxy(getHttpProxy())
                        .build();
                get.setConfig(config);
            }

            System.out.println("Executing request " + get.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(get);
            try {
                String myResult = EntityUtils.toString(response.getEntity());
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(myResult);
                resultResponse.put(STATUS, response.getStatusLine());
                resultResponse.put(RESULT, myResult);
            } finally {
                response.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return resultResponse;
    }
}
