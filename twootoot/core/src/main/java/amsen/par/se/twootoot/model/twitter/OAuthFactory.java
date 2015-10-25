package amsen.par.se.twootoot.model.twitter;

import java.util.Random;

import amsen.par.se.twootoot.BuildConfig;

/**
 * @author params on 25/10/15
 */
public class OAuthFactory {
	public OAuthConfig build(String accessToken) {
		return new OAuthConfig(
				BuildConfig.OAUTH_CONSUMER_KEY,
				accessToken,
				BuildConfig.VERSION_NAME,
				BuildConfig.OAUTH_SIGNATURE_METHOD,
				generateRawNonce(),
				System.currentTimeMillis() / 1000);
	}

	/**
	 * Generate the Twitter OAuth nonce header token. The nonce is a client generated token that
	 * identifies a single request.
	 */
	private String generateRawNonce() {
		String nonce = "";
		byte[] nonsense = new byte[32];
		new Random().nextBytes(nonsense);

		for(byte b : nonsense) {
			nonce.concat(String.valueOf(b));
		}

		return nonce;
	}
}
