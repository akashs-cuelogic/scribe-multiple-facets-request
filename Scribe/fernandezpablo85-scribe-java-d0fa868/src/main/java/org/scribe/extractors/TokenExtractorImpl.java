package org.scribe.extractors;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

/**
 * Default implementation of {@RequestTokenExtractor} and {@AccessTokenExtractor}. Conforms to OAuth 1.0a
 * 
 * The process for extracting access and request tokens is similar so this class can do both things.
 * 
 * @author Pablo Fernandez
 */
public class TokenExtractorImpl implements RequestTokenExtractor, AccessTokenExtractor {

    private static final String TOKEN_REGEX = "oauth_token=(\\S*)&oauth_token_secret=(\\S*?)(&(.*))?";

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
       // System.out.println("response  " + response);
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

        Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        //System.out.println(matcher.toString());
        if (matcher.matches()) {
            String token = URLUtils.percentDecode(matcher.group(1));
            String secret = URLUtils.percentDecode(matcher.group(2));
            return new Token(token, secret);
        } else {
            System.out.println("got error");
            throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
    }
}
