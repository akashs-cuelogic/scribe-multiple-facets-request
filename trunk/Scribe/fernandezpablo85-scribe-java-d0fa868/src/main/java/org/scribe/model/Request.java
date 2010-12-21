package org.scribe.model;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.scribe.exceptions.*;
import org.scribe.utils.*;

/**
 * Represents an HTTP Request object
 * 
 * @author Pablo Fernandez
 */
class Request {

    private static final String CONTENT_LENGTH = "Content-Length";

    private String              url;

    private Verb                verb;

    // private Map<String, String> bodyParams;

    // private Map<String, String> headers;
    private List<ParamInfo>     headers;

    private List<ParamInfo>     bodyParams;

    private String              payload        = null;

    private HttpURLConnection   connection;

    /**
     * Creates a new Http Request
     * 
     * @param verb Http Verb (GET, POST, etc)
     * @param url url with optional querystring parameters.
     */
    public Request(Verb verb, String url) {
        this.verb = verb;
        this.url = url;
        this.bodyParams = new ArrayList<ParamInfo>();
        this.headers = new ArrayList<ParamInfo>();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (IOException ioe) {
            throw new OAuthException("Could not open connection to: " + url, ioe);
        }
    }

    /**
     * Execute the request and return a {@link Response}
     * 
     * @return Http Response
     * @throws RuntimeException if the connection cannot be created.
     */
    public Response send() {
        try {
            return doSend();
        } catch (IOException ioe) {
            throw new OAuthException("Problems while creating connection", ioe);
        }
    }

    Response doSend() throws IOException {
        connection.setRequestMethod(this.verb.name());
        addHeaders(connection);
        if (verb.equals(Verb.PUT) || verb.equals(Verb.POST)) {
            addBody(connection, getBodyContents());
        }
        return new Response(connection);
    }

    void addHeaders(HttpURLConnection conn) {
        //        for (String key : headers.keySet())
        //            conn.setRequestProperty(key, headers.get(key));
        for (ParamInfo paramInfo : this.headers) {
            // System.out.println("adding header " + paramInfo.getKey() + ":" + paramInfo.getValue());
            conn.setRequestProperty(paramInfo.getKey(), paramInfo.getValue());
        }

        //        for (int i = 0; i < count; i++) {
        //            System.out.println("key :: " + this.keyHeaders[i] + "  value :: " + this.valueHeaders[i]);
        //            conn.setRequestProperty(this.keyHeaders[i], this.valueHeaders[i]);
        //        }
    }

    void addBody(HttpURLConnection conn, String content) throws IOException {
        conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.getBytes().length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(content.getBytes());
    }

    /**
     * Add an HTTP Header to the Request
     * 
     * @param key the header name
     * @param value the header value
     */
    public void addHeader(String key, String value) {
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setKey(key);
        paramInfo.setValue(value);
        this.headers.add(paramInfo);
    }

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     * 
     * @param key the parameter name
     * @param value the parameter value
     */
    public void addBodyParameter(String key, String value) {
        ParamInfo paramInfo = new ParamInfo();
        paramInfo.setKey(key);
        paramInfo.setValue(value);
        this.bodyParams.add(paramInfo);
    }

    /**
     * Add body payload.
     * 
     * This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example XML.
     * 
     * Note: The contents are not part of the OAuth signature
     * 
     * @param payload the body of the request
     */
    public void addPayload(String payload) {
        this.payload = payload;
    }

    /**
     * Get a {@link Map} of the query string parameters.
     * 
     * @return a map containing the query string parameters
     */
    public List<ParamInfo> getQueryStringParams() {
        try {
            List<ParamInfo> params = new ArrayList<ParamInfo>();
            String query = new URL(url).getQuery();
            if (query != null) {
                for (String param : query.split("&")) {
                    String pair[] = param.split("=");
                    ParamInfo paramInfo = new ParamInfo();
                    paramInfo.setKey(pair[0]);
                    paramInfo.setValue(pair[1]);
                    params.add(paramInfo);
                }
            }
            return params;
        } catch (MalformedURLException mue) {
            throw new OAuthException("Malformed URL", mue);
        }
    }

    /**
     * Obtains a {@link Map} of the body parameters.
     * 
     * @return a map containing the body parameters.
     */
    public List<ParamInfo> getBodyParams() {
        return bodyParams;
    }

    /**
     * Obtains the URL of the HTTP Request.
     * 
     * @return the original URL of the HTTP Request
     */
    public String getUrl() {
        return url;
    }

    /**
     * Returns the URL without the port and the query string part.
     * 
     * @return the OAuth-sanitized URL
     */
    public String getSanitizedUrl() {
        return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
       // return url;
    }

    /**
     * Returns the body of the request
     * 
     * @return form encoded string
     */
    public String getBodyContents() {
        return (payload != null) ? payload : URLUtils.formURLEncodeMap(bodyParams);
    }

    /**
     * Returns the HTTP Verb
     * 
     * @return the verb
     */
    public Verb getVerb() {
        return verb;
    }

    /**
     * Returns the connection headers as a {@link Map}
     * 
     * @return map of headers
     */
    public List<ParamInfo> getHeaders() {
        return headers;
    }

    /**
     * Sets the connect timeout for the underlying {@link HttpURLConnection}
     * 
     * @param duration duration of the timeout
     * 
     * @param unit unit of time (milliseconds, seconds, etc)
     */
    public void setConnectTimeout(int duration, TimeUnit unit) {
        this.connection.setConnectTimeout((int) unit.toMillis(duration));
    }

    /**
     * Sets the read timeout for the underlying {@link HttpURLConnection}
     * 
     * @param duration duration of the timeout
     * 
     * @param unit unit of time (milliseconds, seconds, etc)
     */
    public void setReadTimeout(int duration, TimeUnit unit) {
        this.connection.setReadTimeout((int) unit.toMillis(duration));
    }

    /*
     * We need this in order to stub the connection object for test cases
     */
    void setConnection(HttpURLConnection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return String.format("@Request(%s %s)", getVerb(), getUrl());
    }
}
