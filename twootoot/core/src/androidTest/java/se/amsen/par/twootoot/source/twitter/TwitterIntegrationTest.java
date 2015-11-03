package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource;

import static se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.OAuthReq;
import static se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.OAuthResp;

/**
 * Tests Twitter integration. Placed in this package as it mainly tests TwitterHttpSource.
 *
 * @author params on 01/11/15
 */
public class TwitterIntegrationTest extends InstrumentationTestCase {
	OAuthSource oauth;

	@Override public void setUp() throws Exception {
		super.setUp();
		oauth = new OAuthSource(getInstrumentation().getContext());
	}

	public void testGetHomeTimeline() throws Exception {
		//User has supplied credentials, validate them
		OAuthConfig config = new OAuthConfig(new OAuthConfig.OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET));
		Result<OAuthResource.OAuthResp> result = oauth.performRequest(new OAuthReq(config), OAuthResp.class);

		assertTrue("Could not perform request to twitter", result.isSuccess());

		//Use credentials to get user home timeline
		throw new RuntimeException("Develop test");
	}
}