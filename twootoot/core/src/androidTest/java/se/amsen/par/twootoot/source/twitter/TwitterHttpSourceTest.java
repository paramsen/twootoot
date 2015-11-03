package se.amsen.par.twootoot.source.twitter;

import java.util.concurrent.TimeUnit;

import amsen.par.se.testlib.UnitTestUtil;
import amsen.par.se.testlib.twitter.Mocks;
import se.amsen.par.twootoot.twitter.OAuthConfig;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author params on 03/11/15
 */
public class TwitterHttpSourceTest extends UnitTestUtil {
	OAuthSource oauth; //random impl to test with

	@Override public void setUp() throws Exception {
		super.setUp();
		oauth = new OAuthSource(getInstrumentation().getContext());
		oauth.storage = spy(oauth.storage);
		oauth.storage.invalidate();
	}

	/**
	 * Test experimental Source Async logic using OAuthSource as impl
	 */
	public void testGetResult1Async() throws InterruptedException {
		//get through twitter
		TimeoutCallback callback1 = new TimeoutCallback();
		oauth.authorizeAsync(Mocks.tokens, callback1);
		timeout(callback1, TimeUnit.SECONDS, 10);

		assertTrue("Did not finish before timeout", callback1.isFinished.get());
		assertTrue("Failed to get from twitter", callback1.ref.get().isSuccess());
		verify(oauth.storage, times(1)).store(anyString(), any(OAuthConfig.OAuthTokens.class));

		//get from cache
		TimeoutCallback callback2 = new TimeoutCallback();
		oauth.authorizeAsync(null, callback2);
		timeout(callback2, TimeUnit.SECONDS, 10);

		assertTrue("Did not finish before timeout", callback2.isFinished.get());
		assertTrue("Failed to get from twitter", callback2.ref.get().isSuccess());
		verify(oauth.storage, times(1)).getByKey(anyString());
	}
}