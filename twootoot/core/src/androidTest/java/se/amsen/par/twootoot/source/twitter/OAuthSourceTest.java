package se.amsen.par.twootoot.source.twitter;

import se.amsen.par.testlib.testlib.UnitTestUtil;
import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.model.twitter.OAuthConfig.OAuthTokens;
import se.amsen.par.twootoot.source.SharedStorageSource;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.source.result.Success;

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
		Result<OAuthConfig> result = source.validateConfig(new OAuthConfig(tokens));
		assertTrue(format("Twitter could not validate", result), result.isSuccess());
	}

	/**
	 * Tests following flow:
	 * User supplies tokens > twitter validate tokens > tokens are cached (mocked) > do more calls and verify
	 * that cache is used instead of going twitter.
	 */
	public void testTwitterIntegrationMockStorage() {
		mockStorage();

		Result<OAuthConfig> resultTwitter = source.getFunc1().doFunc(tokens);
		assertTrue(format("Twitter could not validate", resultTwitter), resultTwitter.isSuccess());
		verify(storage, times(1)).store(anyString(), any(OAuthTokens.class));

		Result<OAuthConfig> resultCache = source.getFunc1().doFunc(null);
		assertTrue(format("Twitter could not validate", resultCache), resultCache.isSuccess());
		verify(storage, times(1)).getByKey(anyString());
	}

	/**
	 * Tests following flow:
	 * User supplies tokens > twitter validate tokens > tokens are cached (mocked) > do more calls and verify
	 * that cache is used instead of going twitter.
	 */
	public void testTwitterIntegration() {
		Result<OAuthConfig> resultTwitter = source.getFunc1().doFunc(tokens);
		assertTrue(format("Twitter could not validate", resultTwitter), resultTwitter.isSuccess());
		verify(storage, times(1)).store(anyString(), any(OAuthTokens.class));

		Result<OAuthConfig> resultCache = source.getFunc1().doFunc(null);
		assertTrue(format("Twitter could not validate", resultCache), resultCache.isSuccess());
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