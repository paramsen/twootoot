package se.amsen.par.twootoot.webcom.twitter.resource;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.util.annotation.UrlParameter;
import se.amsen.par.twootoot.webcom.Resource;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.TwitterRequest;

/**
 * @author params on 27/10/15
 */
public class OAuthResource extends Resource {
	public static class OAuthReq extends TwitterRequest {
		public OAuthReq(OAuthConfig oauth) {
			super(Uri.parse("https://api.twitter.com/1.1/statuses/home_timeline.json"), Method.GET, oauth);
		}

		/**
		 * final UrlParameters to minimize the twitter resp as much as possible.
		 *
		 * TODO when adding a network framework such as Volley, just ignore response body.
		 */
		@UrlParameter @SerializedName("count") public final int count = 1;
		@UrlParameter @SerializedName("trim_user")  public final boolean trimUser = true;
		@UrlParameter @SerializedName("exclude_replies") public final boolean excludeReplies = true;
		@UrlParameter @SerializedName("include_entities") public final boolean includeEntities = false;
	}

	public static class OAuthResp extends Response {
		public OAuthResp() {
			super(CacheMode.NO_CACHE);
		}
	}
}
