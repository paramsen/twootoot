package amsen.par.se.twootoot.model.twitter;

import android.util.Pair;

import java.util.concurrent.TimeUnit;

import amsen.par.se.twootoot.BuildConfig;

/**
 * Config for Twitter OAuth authorization
 *
 * @author params on 25/10/15
 */
public class OAuthConfig extends AbstractModel {
	public final String consumerKey;
	public final String accessToken;
	public final String accessSecret;
	public final String version;
	public final String signatureMethod;
	public String nonce;
	public long timestamp;

	public OAuthConfig(OAuthTokens OAuthTokens) {
		this(BuildConfig.OAUTH_CONSUMER_KEY, OAuthTokens.accessToken, OAuthTokens.accessSecret, BuildConfig.OAUTH_VERSION, BuildConfig.OAUTH_SIGNATURE_METHOD, generateNonce(), getTimestamp());
	}

	public OAuthConfig(String consumerKey, String accessToken, String accessSecret, String version, String signatureMethod, String nonce, long timestamp) {
		this.consumerKey = consumerKey;
		this.accessToken = accessToken;
		this.accessSecret = accessSecret;
		this.version = version;
		this.signatureMethod = signatureMethod;
		this.nonce = nonce;
		this.timestamp = timestamp;
	}

	public OAuthConfig prepareForRequest() {
		nonce = generateNonce();
		timestamp = getTimestamp();

		return this;
	}

	/**
	 * Construct the delicately formatted 'Authorization' header following the Twitter/OAuth
	 * protocol
	 */
	public Pair<String, String> buildOAuthHeader() {
		String key = "Authorization";

		return null;
	}

	private static long getTimestamp() {
		return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
	}

	private static String generateNonce() {
		return null;
	}

	public static class OAuthTokens {
		public String accessToken;
		public String accessSecret;
	}
}