package se.amsen.par.twootoot.model.twitter;

import android.util.Base64;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.model.AbstractModel;

/**
 * Config for Twitter OAuth authorization
 *
 * @author params on 25/10/15
 */
public class OAuthConfig extends AbstractModel {
	public String consumerKey;
	public String accessToken;
	public String accessSecret;
	public String version;
	public String signatureMethod;
	public String nonce;
	public long timestamp;

	public OAuthConfig() {
	}

	public OAuthConfig(OAuthTokens OAuthTokens) {
		this(BuildConfig.OAUTH_CONSUMER_KEY, OAuthTokens.accessToken, OAuthTokens.accessSecret, BuildConfig.OAUTH_VERSION, BuildConfig.OAUTH_SIGNATURE_METHOD);
	}

	public OAuthConfig(String consumerKey, String accessToken, String accessSecret, String version, String signatureMethod) {
		this.consumerKey = consumerKey;
		this.accessToken = accessToken;
		this.accessSecret = accessSecret;
		this.version = version;
		this.signatureMethod = signatureMethod;

		this.nonce = generateNonce();
		this.timestamp = getTimestamp();
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

	private long getTimestamp() {
		return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
	}

	/**
	 * Generate the Twitter OAuth nonce header token. The nonce is a client generated token that
	 * identifies a single request.
	 */
	private String generateNonce() {
		byte[] nonsense = new byte[32];
		new Random().nextBytes(nonsense);

		return Base64.encodeToString(nonsense, Base64.NO_WRAP).replaceAll("[/+=]", "").toLowerCase();
	}

	public static class OAuthTokens {
		public String accessToken;
		public String accessSecret;

		public OAuthTokens(String accessToken, String accessSecret) {
			this.accessToken = accessToken;
			this.accessSecret = accessSecret;
		}
	}
}