package amsen.par.se.twootoot.webcom.resource.twitter;

import android.net.Uri;

import java.util.concurrent.TimeUnit;

import amsen.par.se.twootoot.webcom.Message;
import amsen.par.se.twootoot.webcom.Resource;
import amsen.par.se.twootoot.webcom.ResourceConfig;

/**
 * @author params on 27/10/15
 */
public class OAuthResource extends Resource {
	public static class OAuthReq extends Message {

	}

	public static class OAuthResp extends Message {

	}

	public static class OAuthResourceConfig implements ResourceConfig {
		@Override
		public Uri getUri() {
			return Uri.parse("uri");
		}

		@Override
		public Action getAction() {
			return Action.POST;
		}

		@Override
		public AuthorizeMethod getAuthorizeMethod() {
			return AuthorizeMethod.AUTHORIZE;
		}

		@Override
		public CacheMode getCacheMode() {
			return CacheMode.YEARS_CACHE;
		}

		@Override
		public RateLimit getRateLimit() {
			return RateLimit.NO_LIMIT;
		}

		@Override
		public Window getWindow() {
			return new Window(TimeUnit.MINUTES, 15, 15);
		}
	}
}
