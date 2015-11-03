package se.amsen.par.twootoot.source.twitter;

import android.test.InstrumentationTestCase;

import se.amsen.par.twootoot.twitter.OAuthConfig;

/**
 * @author params on 03/11/15
 */
public class SharedStorageSourceTest extends InstrumentationTestCase {
	SharedStorageSource<OAuthConfig.OAuthTokens> storage;

	@Override protected void setUp() throws Exception {
		super.setUp();
//		storage = new SharedStorageSource<>();
	}
}