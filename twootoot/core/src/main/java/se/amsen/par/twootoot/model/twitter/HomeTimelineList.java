package se.amsen.par.twootoot.model.twitter;

import java.util.ArrayList;
import java.util.List;

import se.amsen.par.twootoot.model.AbstractModel;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.twitter.resource.HomeTimelineResource.HomeTimelineListResp;
import se.amsen.par.twootoot.webcom.twitter.resource.HomeTimelineResource.HomeTimelineResp;

/**
 * @author params on 04/11/15
 */
public class HomeTimelineList extends AbstractModel{
	public List<Tweet> tweets;

	public HomeTimelineList() {
		tweets = new ArrayList<>();
	}

	public HomeTimelineList(HomeTimelineListResp resp) {
		this();

		for(HomeTimelineResp raw : resp.resps) {
			tweets.add(GsonUtil.twitterGson.fromJson(GsonUtil.twitterGson.toJson(raw), Tweet.class));
		}
	}
}
