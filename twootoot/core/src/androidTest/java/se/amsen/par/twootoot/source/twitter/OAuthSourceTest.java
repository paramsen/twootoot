package se.amsen.par.twootoot.source.twitter;

import amsen.par.se.testlib.UnitTestUtil;
import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig.OAuthTokens;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author params on 30/10/15
 */
public class OAuthSourceTest extends UnitTestUtil {
	OAuthSource source;
	SharedStorageSource<OAuthTokens> storage;

	@Override protected void setUp() throws Exception {
		super.setUp();

		source = new OAuthSource(getInstrumentation().getContext());
		source.storage.invalidate(); //drop tables in storage (or shared.clear)
		source.storage = spy(source.storage);
		storage = source.storage;
	}

	public void testValidateConfig() {
		assertTrue("Twitter could not validate", source.validateConfig(new OAuthConfig(tokens)).isSuccess());
	}

	/**
	 * Tests following flow:
	 * User supplies tokens > twitter validate tokens > tokens are cached (mocked) > do more calls and verify
	 * that cache is used instead of going twitter.
	 */
	public void testTwitterIntegrationMockStorage() {
		mockStorage();

		Result<OAuthConfig> resultTwitter = source.getFunc1().doFunc(tokens);
		assertTrue("Twitter could not validate", resultTwitter.isSuccess());
		verify(storage, times(1)).store(anyString(), any(OAuthTokens.class));

		Result<OAuthConfig> resultCache = source.getFunc1().doFunc(null);
		assertTrue("Twitter could not validate", resultCache.isSuccess());
		verify(storage, times(1)).getByKey(anyString());
	}

	/**
	 * Tests following flow:
	 * User supplies tokens > twitter validate tokens > tokens are cached (mocked) > do more calls and verify
	 * that cache is used instead of going twitter.
	 */
	public void testTwitterIntegration() {
		Result<OAuthConfig> resultTwitter = source.getFunc1().doFunc(tokens);
		assertTrue("Twitter could not validate", resultTwitter.isSuccess());
		verify(storage, times(1)).store(anyString(), any(OAuthTokens.class));

		Result<OAuthConfig> resultCache = source.getFunc1().doFunc(null);
		assertTrue("Twitter could not validate", resultCache.isSuccess());
		verify(storage, times(1)).getByKey(anyString());
	}

	//==============================================================================================
	// Helpers
	//==============================================================================================

	OAuthTokens tokens = new OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET);

	public void mockStorage() {
		doReturn(true).when(storage).store(anyString(), any(OAuthTokens.class));
		doReturn(new Success<>(tokens)).when(storage).getByKey(anyString());
	}
}