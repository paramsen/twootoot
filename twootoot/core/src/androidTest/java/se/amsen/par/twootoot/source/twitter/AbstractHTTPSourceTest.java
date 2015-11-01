package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;

import java.net.HttpURLConnection;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource;

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
		Result<OAuthResource.OAuthResp> result = source.performRequest(new OAuthResource.OAuthReq(config), OAuthResource.OAuthResp.class);

		assertTrue("Result was not a Success", result.isSuccess());
		assertEquals("Result was not resp codeHTTP:200", HttpURLConnection.HTTP_OK, result.asSuccess().get().httpStatus);
	}
}