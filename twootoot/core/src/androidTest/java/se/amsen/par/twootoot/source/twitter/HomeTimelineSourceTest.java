package se.amsen.par.twootoot.source.twitter;

import se.amsen.par.testlib.testlib.UnitTestUtil;
import se.amsen.par.testlib.testlib.twitter.Mocks;
import se.amsen.par.twootoot.model.twitter.HomeTimelineList;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * @author params on 04/11/15
 */
public class HomeTimelineSourceTest extends UnitTestUtil {
	public void testGetHomeTimelineFromTokens() throws Exception {
		Result<OAuthConfig> result1 = new OAuthSource(getContext()).authorizeSync(Mocks.tokens);

		assertTrue(format("Could not auth OAuthConfig", result1), result1.isSuccess());

		OAuthConfig config = result1.asSuccess().get();

		HomeTimelineSource homeSrc = new HomeTimelineSource(getContext());
		Result<HomeTimelineList> result2 = homeSrc.getSync(new HomeTimelineSource.Params(50, null, config));

		assertTrue(format("Could not perform HomeTimeline request to twitter", result2), result2.isSuccess());
	}

	public void testGetHomeTimelineFromCache() throws Exception {
		new OAuthSource(getContext()).authorizeSync(Mocks.tokens);

		HomeTimelineSource homeSrc = new HomeTimelineSource(getContext());
		Result<HomeTimelineList> result = homeSrc.getSync(new HomeTimelineSource.Params(50, null, null));

		assertTrue(format("Could not perform HomeTimeline request to twitter", result), result.isSuccess());
	}
}