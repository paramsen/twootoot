package se.amsen.par.twootoot.webcom.twitter.resource;

import android.net.Uri;

import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.webcom.Resource;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.TwitterRequest;

/**
 * @author params on 07/11/15
 */
public class RetweetResource extends Resource {
	public static class RetweetRequest extends TwitterRequest {
		public RetweetRequest(OAuthConfig oauth, String id) {
			super(Uri.parse("https://api.twitter.com/1.1/statuses/retweet/" + id + ".json"), Method.POST, oauth);
		}
	}

	public static class RetweetResponse extends Response {
		public RetweetResponse() {
			super(CacheMode.NO_CACHE);
		}
	}
}
