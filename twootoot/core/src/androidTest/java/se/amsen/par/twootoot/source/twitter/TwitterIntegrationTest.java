package se.amsen.par.twootoot.source.twitter;

import amsen.par.se.testlib.UnitTestUtil;
import amsen.par.se.testlib.twitter.Mocks;
import se.amsen.par.twootoot.model.twitter.HomeTimelineList;
import se.amsen.par.twootoot.source.twitter.HomeTimelineSource.Params;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;

/**
 * Tests Twitter integration. Placed in this package as it mainly tests TwitterHttpSource.
 *
 * @author params on 01/11/15
 */
public class TwitterIntegrationTest extends UnitTestUtil {
	OAuthSource oauth;

	@Override public void setUp() throws Exception {
		super.setUp();
		oauth = new OAuthSource(getContext());
		oauth.storage.invalidate();
	}

	public void testGetHomeTimeline() throws Exception {
		//User has supplied credentials, validate them
		Result<OAuthConfig> result1 = oauth.authorizeSync(Mocks.tokens);

		assertTrue("Could not perform OAuth request to twitter", result1.isSuccess());

		//Use credentials to get user home timeline
		HomeTimelineSource homeSrc = new HomeTimelineSource(getContext());
		Result<HomeTimelineList> result2 = homeSrc.getSync(new Params(50, null, result1.asSuccess().get()));

		assertTrue("Could not perform HomeTimeline request to twitter", result2.isSuccess());
	}
}