package amsen.par.se.twootoot.webcom.resource.twitter;

import android.net.Uri;

import java.util.concurrent.TimeUnit;

/**
 * @author params on 27/10/15
 */
public abstract class CommonConfig implements ResourceConfig {
	@Override
	public Uri getUri() {
		return Uri.parse("api.twitter.com/1.1/" + getSubUri().toString());
	}

	public abstract Uri getSubUri();

	@Override
	public AuthorizeMethod getAuthorizeMethod() {
		return AuthorizeMethod.AUTHORIZE;
	}

	@Override
	public CacheMode getCacheMode() {
		return CacheMode.LONG_CACHE;
	}

	@Override
	public RateLimit getRateLimit() {
		return RateLimit.LIMIT;
	}

	@Override
	public Window getWindow() {
		return new Window(TimeUnit.MINUTES, 15, 15);
	}
}
