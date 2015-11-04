package se.amsen.par.twootoot.source.twitter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import se.amsen.par.twootoot.model.twitter.HomeTimelineList;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.util.functional.Func1;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.twitter.resource.HomeTimelineResource.HomeTimelineListResp;
import se.amsen.par.twootoot.webcom.twitter.resource.HomeTimelineResource.HomeTimelineReq;
import se.amsen.par.twootoot.webcom.twitter.resource.HomeTimelineResource.HomeTimelineResp;

/**
 * TODO abstract all different Twitter Sources into a single Source, depend more on Requests.
 *
 * @author params on 04/11/15
 */
public class HomeTimelineSource extends TwitterHttpSource<HomeTimelineReq, HomeTimelineListResp, HomeTimelineSource.Params, HomeTimelineList> {
	private static final String TAG = TwitterHttpSource.class.getName();
	private static final String STORAGE_KEY = "HomeTimelineSource";

	protected SharedStorageSource<HomeTimelineList> storage;

	public HomeTimelineSource(Context context) {
		super(context);
		storage = new SharedStorageSource<>(context, STORAGE_KEY);
	}

	public Result<HomeTimelineList> getSync(Params params) {
		try {
			return getFunc1().doFunc(params);
		} catch(Exception e) {
			return new Failure<>(e);
		}
	}

	@Override
	protected Func1<Params, Result<HomeTimelineList>> getFunc1() {
		return new Func1<Params, Result<HomeTimelineList>>() {
			@Override
			public Result<HomeTimelineList> doFunc(Params params) {
				OAuthConfig config = params.config;

				if(config == null) {
					config = new OAuthSource(getContext()).authorizeSync(null).asSuccess().get();
				}

				HomeTimelineReq req = new HomeTimelineReq(config);
				req.count = params.count;
				req.sinceId = params.sinceId;

				Result<HomeTimelineListResp> respResult = performRequest(req, HomeTimelineListResp.class);
				HomeTimelineListResp resp = respResult.asSuccess().get();

				return new Success<>(new HomeTimelineList(resp));
			}
		};
	}

	@Override
	protected Result<HomeTimelineListResp> buildResponse(String json, Class<HomeTimelineListResp> responseClass) {
		HomeTimelineListResp resp = new HomeTimelineListResp();

		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(json).getAsJsonArray();

		for(JsonElement element : array) {
			resp.resps.add(GsonUtil.twitterGson.fromJson(element, HomeTimelineResp.class));
		}

		return new Success<>(resp);
	}

	@Override
	public boolean invalidate() {
		return false;
	}

	public static class Params {
		public Params(Integer count, Integer sinceId, @Nullable OAuthConfig config) {
			this.count = count;
			this.sinceId = sinceId;
			this.config = config;
		}

		public Integer count;
		public Integer sinceId;
		public OAuthConfig config;
	}
}
