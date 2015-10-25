package amsen.par.se.twootoot.source.twitter;

import android.support.annotation.Nullable;

import amsen.par.se.twootoot.BuildConfig;
import amsen.par.se.twootoot.boilerplate.Callback;
import amsen.par.se.twootoot.model.twitter.OAuthConfig;
import amsen.par.se.twootoot.source.twitter.result.Failure;
import amsen.par.se.twootoot.source.twitter.result.Result;

/**
 * Source for retrieving an OAuthConfig
 *
 * @author params on 25/10/15
 */
public class AuthSource extends Source<Result<OAuthConfig>> {
	private StorageSource<String, OAuthConfig> storage;

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

	@Override
	protected Result getResult1(String accessToken) {
		//try get from cache
		if(accessToken != null) {
			Result<OAuthConfig> cacheResult = getConfigFromCache();

			if(cacheResult.isSuccess()) {
				return cacheResult;
			} else {
				
			}
		} else {
			//build
			//execute twitter http auth->if success cache resp->callback
			//cache resp
		}
	}

	private Result<OAuthConfig> getConfigFromCache() {
		return null;
	}

	private Result<OAuthConfig> validateConfig(OAuthConfig config) {
		return null;
	}

	private OAuthConfig buildConfig(String accessToken) {
		OAuthConfig auth = new OAuthConfig(
				BuildConfig.OAUTH_CONSUMER_KEY,
				accessToken,
				BuildConfig.VERSION_NAME,
				BuildConfig.OAUTH_SIGNATURE_METHOD,
				generateNewConversationToken());

		return auth;
	}

	private String generateNewConversationToken() {
		return null;
	}

	@Override
	public boolean invalidate() {
		throw new RuntimeException("Not supported by Source");
	}
}