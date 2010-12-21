package org.scribe.model;

import java.io.*;
import java.net.*;
import java.util.*;

import org.scribe.utils.*;

/**
 * Represents an HTTP Response.
 * 
 * @author Pablo Fernandez
 */
public class Response {

    private static final String EMPTY = "";

    private int                 code;

    private String              body;

    private InputStream         stream;

    private List<ParamInfo>     headers;

    Response(HttpURLConnection connection) throws IOException {
        try {
            System.out.println("SB   - " + connection.getURL().getQuery());
            connection.connect();
           // System.out.println("url ::" + connection.getURL());
            code = connection.getResponseCode();
            headers = parseHeaders(connection);
            stream = wasSuccessful() ? connection.getInputStream() : connection.getErrorStream();
        } catch (UnknownHostException e) {
            code = 404;
            body = EMPTY;
        }
    }

    private String parseBodyContents() {
        body = StreamUtils.getStreamContents(getStream());
        return body;
    }

    private List<ParamInfo> parseHeaders(HttpURLConnection conn) {
        List<ParamInfo> headers = new ArrayList<ParamInfo>();

        for (String key : conn.getHeaderFields().keySet()) {
            ParamInfo paramInfo = new ParamInfo();
            paramInfo.setKey(key);
            paramInfo.setValue(conn.getHeaderFields().get(key).get(0));
            //headers.put(key, conn.getHeaderFields().get(key).get(0));
            headers.add(paramInfo);
        }
        return headers;
    }

    private boolean wasSuccessful() {
        return getCode() >= 200 && getCode() < 400;
    }

    /**
     * Obtains the HTTP Response body
     * 
     * @return response body
     */
    public String getBody() {
        return body != null ? body : parseBodyContents();
    }

    /**
     * Obtains the meaningful stream of the HttpUrlConnection, either inputStream or errorInputStream, depending on the status code
     * 
     * @return input stream / error stream
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * Obtains the HTTP status code
     * 
     * @return the status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Obtains a {@link Map} containing the HTTP Response Headers
     * 
     * @return headers
     */
    public List<ParamInfo> getHeaders() {
        return headers;
    }

    /**
     * Obtains a single HTTP Header value, or null if undefined
     * 
     * @param header name
     * 
     * @return header value or null
     */
//    public String getHeader(String name) {
//        return headers.get(name);
//    }

}