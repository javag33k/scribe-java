/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.scribe.builder.api;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;

/**
 *
 * @author javageek
 */
public class MagentoOauth10a extends MagentoDefaultApi10a {
  private static String BASE_URL = null;

		@Override
		public String getRequestTokenEndpoint() {
			return BASE_URL + "oauth/initiate";
		}

		@Override
		public String getAccessTokenEndpoint() {
			return BASE_URL + "oauth/token";
		}

		@Override
		public String getAuthorizationUrl(Token requestToken) {
			return BASE_URL + "admin/oauth_authorize?oauth_token="
					+ requestToken.getToken(); //this implementation is for admin roles only...
		}
    
    @Override
    public String setBaseURL(String baseURL) {
      BASE_URL = baseURL;
      if (BASE_URL == null) throw new OAuthException("The BASE_URL is still null.  Please set it correctly");
      return BASE_URL;
    }

}
