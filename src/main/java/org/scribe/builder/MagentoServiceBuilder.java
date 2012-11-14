package org.scribe.builder;

import java.io.*;
import org.scribe.builder.api.*;
import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.oauth.*;
import org.scribe.utils.*;

/**
 * Implementation of the Builder pattern, with a fluent interface that creates a
 * {@link OAuthService}
 * 
 * @author z
 *
 */
public class MagentoServiceBuilder
{
  protected String apiKey;
  protected String apiSecret;
  protected String callback;
  protected ApiInitializer api;
  protected String scope;
  protected SignatureType signatureType;
  protected OutputStream debugStream;
  
  /**
   * Default constructor
   */
  public MagentoServiceBuilder()
  {
    this.callback = OAuthConstants.OUT_OF_BAND;
    this.signatureType = SignatureType.Header;
    this.debugStream = null;
  }
  
  /**
   * Configures the {@link Api}
   * 
   * @param apiClass the class of one of the existent {@link Api}s on org.scribe.api package
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder provider(Class<? extends ApiInitializer> apiClass, String baseURL)
  {
    this.api = createApi(apiClass);
    this.api.setBaseURL(baseURL);
    return this;
  }

  protected ApiInitializer createApi(Class<? extends ApiInitializer> apiClass)
  {
    Preconditions.checkNotNull(apiClass, "Api class cannot be null");
    ApiInitializer api;
    try
    {
      api = apiClass.newInstance();  
    }
    catch(Exception e)
    {
      throw new OAuthException("Error while creating the Api object", e);
    }
    return api;
  }

  /**
   * Configures the {@link Api}
   *
   * Overloaded version. Let's you use an instance instead of a class.
   *
   * @param api instance of {@link Api}s
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder provider(ApiInitializer api)
  {
	  Preconditions.checkNotNull(api, "Api cannot be null");
	  this.api = api;
	  return this;
  }

  /**
   * Adds an OAuth callback url
   * 
   * @param callback callback url. Must be a valid url or 'oob' for out of band OAuth
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder callback(String callback)
  {
    Preconditions.checkNotNull(callback, "Callback can't be null");
    this.callback = callback;
    return this;
  }
  
  /**
   * Configures the api key
   * 
   * @param apiKey The api key for your application
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder apiKey(String apiKey)
  {
    Preconditions.checkEmptyString(apiKey, "Invalid Api key");
    this.apiKey = apiKey;
    return this;
  }
  
  /**
   * Configures the api secret
   * 
   * @param apiSecret The api secret for your application
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder apiSecret(String apiSecret)
  {
    Preconditions.checkEmptyString(apiSecret, "Invalid Api secret");
    this.apiSecret = apiSecret;
    return this;
  }
  
  /**
   * Configures the OAuth scope. This is only necessary in some APIs (like Google's).
   * 
   * @param scope The OAuth scope
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder scope(String scope)
  {
    Preconditions.checkEmptyString(scope, "Invalid OAuth scope");
    this.scope = scope;
    return this;
  }

  /**
   * Configures the signature type, choose between header, querystring, etc. Defaults to Header
   *
   * @param scope The OAuth scope
   * @return the {@link MagentoServiceBuilder} instance for method chaining
   */
  public MagentoServiceBuilder signatureType(SignatureType type)
  {
    Preconditions.checkNotNull(type, "Signature type can't be null");
    this.signatureType = type;
    return this;
  }

  public MagentoServiceBuilder debugStream(OutputStream stream)
  {
    Preconditions.checkNotNull(stream, "debug stream can't be null");
    this.debugStream = stream;
    return this;
  }

  public MagentoServiceBuilder debug()
  {
    this.debugStream(System.out);
    return this;
  }
  
  /**
   * Returns the fully configured {@link OAuthService}
   * 
   * @return fully configured {@link OAuthService}
   */
  public OAuthService build()
  {
    Preconditions.checkNotNull(api, "You must specify a valid api through the provider() method");
    Preconditions.checkEmptyString(apiKey, "You must provide an api key");
    Preconditions.checkEmptyString(apiSecret, "You must provide an api secret");
    return api.createService(new OAuthConfig(apiKey, apiSecret, callback, signatureType, scope, debugStream));
  }
}
