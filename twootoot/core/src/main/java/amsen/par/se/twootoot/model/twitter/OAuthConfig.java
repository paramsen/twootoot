package amsen.par.se.twootoot.model.twitter;

/**
 * Config for Twitter OAuth authorization
 *
 * @author params on 25/10/15
 */
public class OAuthConfig {
	public String consumerKey;
	public String accessToken;
	public String version;
	public String signatureMethod;
	public String clientConversationToken;

	public OAuthConfig() {
	}

	public OAuthConfig(String consumerKey, String accessToken, String version, String signatureMethod, String clientConversationToken) {
		this.consumerKey = consumerKey;
		this.accessToken = accessToken;
		this.version = version;
		this.signatureMethod = signatureMethod;
		this.clientConversationToken = clientConversationToken;
	}
}