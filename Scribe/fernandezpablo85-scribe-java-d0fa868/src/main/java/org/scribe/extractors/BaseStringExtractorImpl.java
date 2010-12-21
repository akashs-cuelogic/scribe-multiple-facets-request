package org.scribe.extractors;

import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@link BaseStringExtractor}. Conforms to OAuth 1.0a
 * 
 * @author Pablo Fernandez
 * 
 */
public class BaseStringExtractorImpl implements BaseStringExtractor {

    private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

    /**
     * {@inheritDoc}
     */
    public String extract(OAuthRequest request) {
        checkPreconditions(request);
        String verb = URLUtils.percentEncode(request.getVerb().name());
        String url = URLUtils.percentEncode(request.getSanitizedUrl());
        String params = getSortedAndEncodedParams(request);
        System.out.println("FORMATED " + String.format(AMPERSAND_SEPARATED_STRING, verb, url, params));
        return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
    }

    private String getSortedAndEncodedParams(OAuthRequest request) {
        List<ParamInfo> params = new ArrayList<ParamInfo>();

        params.addAll(request.getQueryStringParams());
        params.addAll(request.getBodyParams());
        params.addAll(request.getOauthParameters());
        //        params.putAll(request.getQueryStringParams());
        //        params.putAll(request.getBodyParams());
        //        params.putAll(request.getOauthParameters());
        params = MapUtils.sort(params);
        return URLUtils.percentEncode(URLUtils.formURLEncodeMap(params));
    }

    private void checkPreconditions(OAuthRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract base string from null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }
}
