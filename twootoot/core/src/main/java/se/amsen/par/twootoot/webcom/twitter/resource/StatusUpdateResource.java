package se.amsen.par.twootoot.webcom.twitter.resource;

import android.net.Uri;

import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.util.annotation.Required;
import se.amsen.par.twootoot.util.annotation.UrlParameter;
import se.amsen.par.twootoot.webcom.Resource;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.TwitterRequest;

/**
 * @author params on 07/11/15
 */
public class StatusUpdateResource extends Resource {
	public static class StatusUpdateRequest extends TwitterRequest {
		public StatusUpdateRequest(OAuthConfig oauth, String status) {
			super(Uri.parse("https://api.twitter.com/1.1/statuses/update.json"), Method.POST, oauth);

			this.status = status;
		}

		@Required @UrlParameter public String status;
	}

	public static class StatusUpdateResponse extends Response {
		public StatusUpdateResponse() {
			super(CacheMode.NO_CACHE);
		}
	}
}
