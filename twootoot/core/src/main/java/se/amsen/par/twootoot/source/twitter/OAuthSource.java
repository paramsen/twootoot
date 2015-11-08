package se.amsen.par.twootoot.source.twitter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.source.SharedStorageSource;
import se.amsen.par.twootoot.source.result.Failure;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.source.result.Success;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.model.twitter.OAuthConfig.OAuthTokens;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.functional.Func1;
import se.amsen.par.twootoot.webcom.twitter.exception.MissingOAuthConfigException;
import se.amsen.par.twootoot.webcom.twitter.exception.NetworkException;
import se.amsen.par.twootoot.webcom.twitter.exception.HttpStatusException;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.OAuthReq;
import se.amsen.par.twootoot.webcom.twitter.resource.OAuthResource.OAuthResp;

/**
 * Source for retrieving an OAuthConfig
 *
 * @author params on 25/10/15
 */
public class OAuthSource extends TwitterHttpSource<OAuthReq, OAuthResp, OAuthTokens, OAuthConfig> {
	private static final String TAG = OAuthSource.class.getName();

	private static final String STORAGE_KEY = "OAuthSource";

	protected SharedStorageSource<OAuthTokens> storage;

	public OAuthSource(Context context) {
		super(context);
		storage = new SharedStorageSource<>(context, STORAGE_KEY);
	}

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
	public AsyncRunner authorizeAsync(@Nullable OAuthTokens tokens, Callback<Result<OAuthConfig>> callback, @Nullable TimeUnit unit, @Nullable Integer timeout) {
		return asyncGetResult1(tokens, callback, unit, timeout);
	}

	public Result<OAuthConfig> authorizeSync(@Nullable OAuthTokens tokens) {
		return getFunc1().doFunc(tokens);
	}

	/**
	 * Will always instantiate a new OAuthConfig as nonce/timestamp must be unique for each request.
	 */
	@Override protected Func1<OAuthTokens, Result<OAuthConfig>> getFunc1() {
		return new Func1<OAuthTokens, Result<OAuthConfig>>() {
			@Override
			public Result<OAuthConfig> doFunc(OAuthTokens tokens) {
				if(tokens == null) {
					Result<OAuthTokens> fromCache = storage.getByKey(STORAGE_KEY);

					if(fromCache.isSuccess()) {
						return new Success<>(new OAuthConfig(fromCache.asSuccess().get()));
					}

					return new Failure<>(new MissingOAuthConfigException("No tokens in storage"));
				} else {
					OAuthConfig config = new OAuthConfig(tokens);
					Result<OAuthConfig> result = validateConfig(config);

					if(result.isSuccess()) {
						storage.store(STORAGE_KEY, tokens);
					}

					return result;
				}
			}
		};
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

	/**
	 * OAuth validation doesn't care about any response data, just the resp code. Hence overriding
	 * default Twitter response behaviour.
	 */
	@Override protected Result<OAuthResp> processConnection(HttpURLConnection conn, Class<OAuthResp> responseClass) {
		try {
			int code = conn.getResponseCode();

			if(code == HttpURLConnection.HTTP_OK) {
				return new Success<>(new OAuthResp());
			} else {
				return new Failure<>(new HttpStatusException(String.format("Wrong status code from Twitter[%d]", code), code));
			}
		} catch (IOException e) {
			Log.e(TAG, "Exception getting response code", e);
			return new Failure<>(new NetworkException("Could not connect to twitter", e));
		}
	}

	@Override public boolean invalidate() {
		return storage.invalidate(STORAGE_KEY);
	}
}