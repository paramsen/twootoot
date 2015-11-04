package amsen.par.se.testlib.twitter;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.model.twitter.OAuthConfig.OAuthTokens;

/**
 * @author params on 03/11/15
 */
public class Mocks {
	public static OAuthTokens tokens = new OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET);
}
