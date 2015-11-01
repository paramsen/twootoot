package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;

import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig.OAuthTokens;

/**
 * @author params on 30/10/15
 */
public class OAuthSourceTest extends InstrumentationTestCase {
	OAuthSource source;

	@Override protected void setUp() throws Exception {
		super.setUp();

		source = new OAuthSource();
	}

	public void testValidateConfig() {
		Result<OAuthConfig> result = source.validateConfig(getMockConfig());
	}

	//==============================================================================================
	// Helpers
	//==============================================================================================

	public OAuthConfig getMockConfig() {
		return new OAuthConfig(new OAuthTokens("test", "test"));
	}
}