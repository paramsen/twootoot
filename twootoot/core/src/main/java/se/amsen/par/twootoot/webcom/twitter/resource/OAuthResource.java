package se.amsen.par.twootoot.webcom.twitter.resource;

import android.net.Uri;

import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.webcom.Resource;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.TwitterRequest;

/**
 * @author params on 27/10/15
 */
public class OAuthResource extends Resource {
	public static class OAuthReq extends TwitterRequest {
		public OAuthReq(OAuthConfig oauth) {
			super(Uri.parse(""), Method.GET, oauth);
		}

		public final int count = 1;
	}

	public static class OAuthResp extends Response {
		public OAuthResp(CacheMode cacheMode) {
			super(cacheMode);
		}

		public int httpStatus;
	}
}
