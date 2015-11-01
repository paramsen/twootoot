package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;

import java.net.HttpURLConnection;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource;

import static se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.*;

/**
 * Tests Twitter integration. Placed in this package as it mainly tests AbstractHttpSource.
 *
 * @author params on 01/11/15
 */
public class TwitterIntegrationTest extends InstrumentationTestCase {
	OAuthSource source; //random implementation of AbstractHttpSource

	@Override public void setUp() throws Exception {
		super.setUp();
		source = new OAuthSource();
	}

	public void testPerformRequest() throws Exception {
		OAuthConfig config = new OAuthConfig(new OAuthConfig.OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET));
		Result<OAuthResource.OAuthResp> result = source.performRequest(new OAuthReq(config), OAuthResp.class);

		//TODO in TwitterRequest develop the request stuff

		assertTrue("Result was not a Success: " + (result.isFailure() ? result.asFailure().get().getMessage() : ""), result.isSuccess());
		assertEquals("Result was not resp codeHTTP:200", HttpURLConnection.HTTP_OK, result.asSuccess().get().httpStatus);
	}
}