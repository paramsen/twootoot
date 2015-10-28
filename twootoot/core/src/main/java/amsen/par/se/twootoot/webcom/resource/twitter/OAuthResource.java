package amsen.par.se.twootoot.webcom.resource.twitter;

import android.net.Uri;

import amsen.par.se.twootoot.model.twitter.OAuthConfig;
import amsen.par.se.twootoot.webcom.Request;
import amsen.par.se.twootoot.webcom.Resource;
import amsen.par.se.twootoot.webcom.ResourceConfig;
import amsen.par.se.twootoot.webcom.Response;

/**
 * @author params on 27/10/15
 */
public class OAuthResource extends Resource {
	public static class OAuthReq extends Request {
		public OAuthReq(OAuthConfig config) {
			super(config.buildOAuthHeader());
		}

		public final int count = 1;
	}

	public static class OAuthResp extends Response {
		public OAuthResp(CacheMode cacheMode) {
			super(cacheMode);
		}
	}

	public static class OAuthResourceConfig extends ResourceConfig {
		public OAuthResourceConfig() {
			super(Uri.parse("api.twitter.com/1.1/statuses/home_timeline.json"), Action.GET);
		}
	}
}
