package se.amsen.par.twootoot.source.twitter;

import android.support.annotation.Nullable;

import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig.OAuthTokens;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.OAuthReq;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.OAuthResp;

/**
 * Source for retrieving an OAuthResourceConfig
 *
 * @author params on 25/10/15
 */
public class OAuthSource extends AbstractHttpSource<OAuthReq, OAuthResp, OAuthConfig, OAuthTokens, Void> {
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
	 * @param tokens (@Nullable) accessToken from user.
	 * @param callback Callback to be called when a Result is ready.
	 */
	public void authorizeAsync(@Nullable OAuthTokens tokens, Callback<OAuthConfig> callback) {
		getResult1Async(tokens, callback);
	}

	@Override protected Result<OAuthConfig> getResult1(final OAuthTokens tokens) {
		if(tokens == null) {
			Result<OAuthConfig> cacheResult = storage.getByKey(STORAGE_KEY);

			if(cacheResult.isSuccess()) {
				return validateConfig(cacheResult.asSuccess().get());
			}

			return cacheResult;
		} else {
			OAuthConfig config = new OAuthConfig(tokens);
			Result<OAuthConfig> result = validateConfig(config);

			if(result.isSuccess()) {
				storage.store(STORAGE_KEY, result.asSuccess().get());
			}

			return result;
		}
	}

	/**
	 * Validate using Twitter APIs.
	 */
	protected Result<OAuthConfig> validateConfig(OAuthConfig config) {
		Result<OAuthResp> result = performRequest(new OAuthReq(config), OAuthResp.class);

		if(result.isSuccess()) {
			return new Success<>(config);
		} else {
			return new Failure<>(result.asFailure());
		}
	}

	@Override public boolean invalidate() {
		return storage.invalidate(STORAGE_KEY);
	}
}