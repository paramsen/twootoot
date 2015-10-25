package amsen.par.se.twootoot.model.twitter;

/**
 * Config for Twitter OAuth authorization
 *
 * @author params on 25/10/15
 */
public class OAuthConfig extends AbstractModel {
	private String consumerKey;
	private String accessToken;
	private String version;
	private String signatureMethod;
	private String nonce;
	private long timestamp;

	protected OAuthConfig(String consumerKey, String accessToken, String version, String signatureMethod, String nonce, long timestamp) {
		this.consumerKey = consumerKey;
		this.accessToken = accessToken;
		this.version = version;
		this.signatureMethod = signatureMethod;
		this.nonce = nonce;
		this.timestamp = timestamp;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSignatureMethod() {
		return signatureMethod;
	}

	public void setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}