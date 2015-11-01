package se.amsen.par.twootoot.webcom.twitter;

import android.net.Uri;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.HttpURLConnection;
import java.util.Map;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.util.annotation.Exclude;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.Request;

/**
 * @author params on 29/10/15
 */
public class TwitterRequest extends Request {
	@Exclude private OAuthConfig oauth;

	public TwitterRequest(Uri uri, Method method, OAuthConfig oauth) {
		super(uri, method);
		this.oauth = oauth;
	}

	@Override
	public HttpURLConnection buildProcessedRequest() {
		JsonObject bodyTree = GsonUtil.gson.toJsonTree(this).getAsJsonObject();
		JsonObject oauthTree = GsonUtil.gson.toJsonTree(oauth).getAsJsonObject();

		JsonObject sorted = GsonUtil.lexSort(GsonUtil.merge(bodyTree, oauthTree));

		for(Map.Entry<String, JsonElement> entry : sorted.entrySet()) {
			/*
			 * For each key/value pair:
			 * Append the encoded key to the output string.
			 * Append the ‘=’ character to the output string.
			 * Append the encoded value to the output string.
			 * If there are more key/value pairs remaining, append a ‘&’ character to the output string.
			 */

			String key = urlEncode(entry.getKey(), BuildConfig.TWITTER_CHARSET);
			String value = urlEncode(GsonUtil.gson.toJson(entry.getValue().getAsString()), BuildConfig.TWITTER_CHARSET);
		}

		throw new RuntimeException("Not implemented");
	}

	public OAuthConfig getOauth() {
		return oauth;
	}

	public void setOauth(OAuthConfig oauth) {
		this.oauth = oauth;
	}
}
