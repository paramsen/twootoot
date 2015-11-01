package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig;

import static se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.*;

/**
 * @author params on 01/11/15
 */
public class AbstractHTTPSourceTest extends InstrumentationTestCase {
	OAuthSource source; //random implementation of AbstractHTTPSourceTest

	@Override public void setUp() throws Exception {
		super.setUp();
		source = new OAuthSource();
	}

	public void testPerformRequest() throws Exception {
		OAuthConfig config = new OAuthConfig(new OAuthConfig.OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET));
		source.performRequest(new OAuthReq(config), OAuthResp.class);
	}
}