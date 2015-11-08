package se.amsen.par.twootoot.webcom.twitter.resource;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.util.annotation.UrlParameter;
import se.amsen.par.twootoot.webcom.Resource;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.TwitterRequest;

/**
 * @author params on 27/10/15
 */
public class HomeTimelineResource extends Resource {
	public static class HomeTimelineReq extends TwitterRequest {
		public HomeTimelineReq(OAuthConfig oauth) {
			super(Uri.parse("https://api.twitter.com/1.1/statuses/home_timeline.json"), Method.GET, oauth);
		}

		public HomeTimelineReq(OAuthConfig oauth, Integer count, Integer sinceId, Boolean includeEntities) {
			this(oauth);

			this.count = count;
			this.sinceId = sinceId;
			this.includeEntities = includeEntities;
		}

		@UrlParameter @SerializedName("count") public Integer count = 1;
		@UrlParameter @SerializedName("since_id") public Integer sinceId = 1;
		@UrlParameter @SerializedName("include_entities") public Boolean includeEntities = false;
	}

	public static class HomeTimelineListResp extends Response {
		public List<HomeTimelineResp> resps;

		public HomeTimelineListResp() {
			super(CacheMode.NORMAL_CACHE);
			resps = new ArrayList<>();
		}
	}

	public static class HomeTimelineResp {
		public String idStr;
		public String text;
		public User user;

	}

	public static class User {
			public String name;
			public String profileImageUrl;
			public String screenName;
	}
}
