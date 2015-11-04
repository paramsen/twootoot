package se.amsen.par.twootoot.webcom.twitter;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.GenericSourceException;
import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.util.annotation.Exclude;
import se.amsen.par.twootoot.util.annotation.UrlParameter;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.Request;

import static se.amsen.par.twootoot.BuildConfig.TWITTER_CHARSET;

/**
 * @author params on 29/10/15
 */
public class TwitterRequest extends Request {
	private static final String TAG = TwitterRequest.class.getCanonicalName();

	@Exclude private OAuthConfig oauth;

	public TwitterRequest(Uri uri, Method method, OAuthConfig oauth) {
		super(uri, method);
		this.oauth = oauth;
	}

	@Override
	public Result<HttpURLConnection> buildProcessedRequest() {
		Result<String> sign = signRequest();

		appendUriParameters();

		try {
			if(sign.isSuccess()) {
				OAuthWrapper oauth = new OAuthWrapper(this.oauth);
				oauth.signature = urlEncode(sign.asSuccess().get(), TWITTER_CHARSET); //Twitter spec says nothing about encoding here, but signing is invalid without it
				HttpURLConnection conn = (HttpURLConnection) new URL(getUri().toString()).openConnection();
				conn.setRequestProperty("Authorization", buildTwitterAuthHeader(oauth));
				conn.setRequestMethod(getMethod().name());

				return new Success<>(conn);
			} else {
				return new Failure<>(new GenericSourceException("Signing was not valid (OR network err)")); //TODO return signing specific ex, could be generic network err
			}
		} catch(Exception e) {
			Log.e(TAG, "Exception composing HTTP", e);

			return new Failure<>(e);
		}
	}

	/**
	 * _After_ signing is done, call this method to append all fields in Class hierarchy annotated
	 * as UrlParameter. Annotation SerializedName is also used for the parameter key.
	 *
	 * As for now fields appended must have access modifier public.
	 *
	 */
	private void appendUriParameters() {
		Uri.Builder builder = getUri().buildUpon();

		for(Field field : getClass().getFields()) {
			if(field.getAnnotation(UrlParameter.class) != null) {
				try {
					String serializedName = field.getName();
					SerializedName nameAnnotation = field.getAnnotation(SerializedName.class);

					if(nameAnnotation != null) {
						serializedName = nameAnnotation.value();
					}

					builder.appendQueryParameter(serializedName, field.get(this).toString());
				} catch (Exception e) {
					Log.e(TAG, "Could not append field, is field public?", e);
				}
			}
		}

		setUri(builder.build());
	}

	protected String buildTwitterAuthHeader(OAuthWrapper oauth) {
		StringBuilder builder = new StringBuilder("OAuth");
		JsonObject json = GsonUtil.lexSort(GsonUtil.twitterGson.toJsonTree(oauth).getAsJsonObject());

		for(Entry<String, JsonElement> entry : json.entrySet()) {
			builder.append(" ")
					.append(entry.getKey())
					.append("=")
					.append("\"")
					.append(strip(entry.getValue().getAsString()))
					.append("\"")
					.append(",");
		}

		return builder.delete(builder.length()-1, builder.length()).toString();
	}

	/**
	 * Process & sign the whole request (uri, method, params etc)
	 *
	 * @return Valid Twitter OAuth base64:ed hash sign to be used in Authorization header
	 */
	protected Result<String> signRequest() {
		return hmacSha1Hash(buildSignatureBase(), buildSignatureKey());
	}

	/**
	 * @return Assembled Method, URL & params according as seen in Twitter OAuth spec:
	 * https://dev.twitter.com/oauth/overview/creating-signatures
	 */
	protected String buildSignatureBase() {
		return getMethod().name()
				.concat("&")
				.concat(urlEncode(getUri().toString(), TWITTER_CHARSET))
				.concat("&")
				.concat(urlEncode(processParamsAndBody(), TWITTER_CHARSET));
	}

	/**
	 * @return Assembled consumer key & access secret as seen in Twitter OAuth spec:
	 * https://dev.twitter.com/oauth/overview/creating-signatures
	 */
	protected String buildSignatureKey() {

		return urlEncode(BuildConfig.OAUTH_CONSUMER_SECRET, TWITTER_CHARSET)
				.concat("&")
				.concat(urlEncode(oauth.accessSecret, TWITTER_CHARSET));
	}

	/**
	 * @return Result of hashing input with HMAC-SHA1 encoded as Base64
	 */
	protected Result<String> hmacSha1Hash(String value, String key) {
		try {
			Mac mac = Mac.getInstance(BuildConfig.OAUTH_SIGNATURE_METHOD);
			mac.init(new SecretKeySpec(key.getBytes(TWITTER_CHARSET), BuildConfig.OAUTH_SIGNATURE_METHOD));

			return new Success<>(Base64.encodeToString(mac.doFinal(value.getBytes(TWITTER_CHARSET)), Base64.NO_WRAP));
		} catch (Exception e) {
			Log.e(TAG, "Exception during hmac-sha1 hashing", e); //TODO better exception

			return new Failure<>(e);
		}
	}

	/**
	 * Process delicate params & req body as specified in Twitter OAuth spec:
	 * https://dev.twitter.com/oauth/overview/creating-signatures
	 */
	protected String processParamsAndBody() {
		JsonObject bodyTree = GsonUtil.twitterGson.toJsonTree(this).getAsJsonObject();
		JsonObject oauthTree = GsonUtil.twitterGson.toJsonTree(new OAuthWrapper(oauth)).getAsJsonObject();
		JsonObject sorted = GsonUtil.lexSort(GsonUtil.merge(bodyTree, oauthTree));

		StringBuilder builder = new StringBuilder();

		for(Entry<String, JsonElement> entry : sorted.entrySet()) {
			if(builder.length() > 0) {
				builder.append("&");
			}

			String key = urlEncode(strip(entry.getKey()), TWITTER_CHARSET);
			String value = urlEncode(strip(GsonUtil.twitterGson.toJson(entry.getValue().getAsString())), TWITTER_CHARSET);

			builder.append(key).append("=").append(value);
		}

		return builder.toString();
	}

	/**
	 * Strip all unwanted chars (unwanted by Twitter OAuth spec)
	 */
	private String strip(String s) {
		return s.replace("\"", "");
	}

	/**
	 * Simple class for handling the Twitter OAuth API parameters in header & signing
	 */
	protected static class OAuthWrapper {
		@SerializedName("oauth_consumer_key") public String consumerKey;
		@SerializedName("oauth_token") public String accessToken;
		@SerializedName("oauth_version") public String version;
		@SerializedName("oauth_signature_method") public String signatureMethod;
		@SerializedName("oauth_nonce") public String nonce;
		@SerializedName("oauth_timestamp") public long timestamp;

		/**
		 * Not used in signing, but as header
		 */
		@SerializedName("oauth_signature") public String signature;


		public OAuthWrapper() {
		}

		public OAuthWrapper(OAuthConfig config) {
			this(config.consumerKey, config.accessToken, config.version, config.signatureMethod, config.nonce, config.timestamp, null);
		}

		public OAuthWrapper(String consumerKey, String accessToken, String version, String signatureMethod, String nonce, long timestamp, String signature) {
			this.consumerKey = consumerKey;
			this.accessToken = accessToken;
			this.version = version;
			this.signatureMethod = signatureMethod;
			this.nonce = nonce;
			this.timestamp = timestamp;
			this.signature = signature;
		}
	}
}
