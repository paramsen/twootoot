package se.amsen.par.twootoot.source.twitter;

import amsen.par.se.testlib.UnitTestUtil;
import amsen.par.se.testlib.twitter.Mocks;
import se.amsen.par.twootoot.model.twitter.HomeTimelineList;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * @author params on 04/11/15
 */
public class HomeTimelineSourceTest extends UnitTestUtil {
	public void testGetHomeTimeline() throws Exception {
		HomeTimelineSource homeSrc = new HomeTimelineSource(getContext());
		Result<HomeTimelineList> result2 = homeSrc.getSync(new HomeTimelineSource.Params(50, null, new OAuthConfig(Mocks.tokens)));

		assertTrue("Could not perform HomeTimeline request to twitter", result2.isSuccess());
	}
}