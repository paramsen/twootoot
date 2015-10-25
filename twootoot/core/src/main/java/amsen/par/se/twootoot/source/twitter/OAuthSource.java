package amsen.par.se.twootoot.source.twitter;

import android.support.annotation.Nullable;

import amsen.par.se.twootoot.util.functional.Callback;
import amsen.par.se.twootoot.model.twitter.OAuthConfig;
import amsen.par.se.twootoot.model.twitter.OAuthFactory;
import amsen.par.se.twootoot.source.twitter.result.Result;

/**
 * Source for retrieving an OAuthConfig
 *
 * @author params on 25/10/15
 */
public class OAuthSource extends Source<Result<OAuthConfig>, String, Void> {
	private static final String STORAGE_KEY = "OAuthSource";

	private StorageSource<OAuthConfig, Result<OAuthConfig>> storage;

	/**
	 * Validate with Twitter APIs that provided accessToken is valid. If accessToken is valid
	 * Result instance will be a Success, otherwise a Failure. If the accessToken is null the cache,
	 * if any, will be validated using Twitter APIs and the return value will be either a Success or
	 * a Failure depending on validity.
	 *
	 * When a Result is ready it is provided to the Callback.
	 *
	 * @param accessToken (@Nullable) accessToken from user.
	 * @param callback Callback to be called when a Result is ready.
	 */
	public void authorizeAsync(@Nullable String accessToken, Callback<Result<OAuthConfig>> callback) {
		getResult1Async(accessToken, callback);
	}

	@Override protected Result<OAuthConfig> getResult1(final String accessToken) {
		if(accessToken == null) {
			Result<OAuthConfig> cacheResult = storage.getByKey(STORAGE_KEY);

			if(cacheResult.isSuccess()) {
				return validateConfig(cacheResult.asSuccess().get());
			}

			return cacheResult;
		} else {
			invalidate();
			Result<OAuthConfig> result = validateConfig(buildConfig(accessToken));

			if(result.isSuccess()) {
				storage.store(STORAGE_KEY, result.asSuccess().get());
			}

			return result;
		}
	}

	/**
	 * Validate using Twitter APIs.
	 */
	private Result<OAuthConfig> validateConfig(OAuthConfig config) {
		return null;
	}

	private OAuthConfig buildConfig(String accessToken) {
		return new OAuthFactory().build(accessToken);
	}

	@Override public boolean invalidate() {
		throw new RuntimeException("Not supported by Source");
	}
}