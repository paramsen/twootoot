package se.amsen.par.twootoot.activity.twitter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.BaseActivity;
import se.amsen.par.twootoot.component.twitter.TweetListComponent;
import se.amsen.par.twootoot.model.Event;
import se.amsen.par.twootoot.model.twitter.HomeTimelineList;
import se.amsen.par.twootoot.model.twitter.Tweet;
import se.amsen.par.twootoot.source.ImageSource;
import se.amsen.par.twootoot.source.twitter.HomeTimelineSource;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;

/**
 * @author params on 06/11/15
 */
public class HomeActivity extends BaseActivity {
	private TweetListComponent tweetListComponent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		tweetListComponent = (TweetListComponent) findViewById(R.id.tweetListComponent);
		tweetListComponent.onReady(new Event<>(getMockData()));
//		getTweets();
	}

	private void getTweets() {
		new HomeTimelineSource(this).getAsync(new HomeTimelineSource.Params(10, null, null), new Callback<Result<HomeTimelineList>>() {
			@Override
			public void onComplete(Result<HomeTimelineList> result) {
				tweetListComponent.onReady(new Event<>(result.asSuccess().get().tweets));
			}
		}, TimeUnit.SECONDS, 30);
	}

	private void getProfileImages(final List<Tweet> tweets) {
		List<Pair<Integer, String>> pairedTweets = new ArrayList<>();
		for(int i = 0 ; i < tweets.size() ; i++) {
			pairedTweets.add(new Pair<>(i, tweets.get(i).user.profileImageUrl));
		}

		new ImageSource(this).getDrawables(new ImageSource.Params(pairedTweets, new Callback<Pair<Integer, Drawable>>() {
			@Override
			public void onComplete(Pair<Integer, Drawable> result) {
				tweets.get(result.first).user.profileImageDrawable = result.second;
				tweetListComponent.getDataSetAdapter().notifyItemChanged(result.first);
			}
		}));
	}

	private List<Tweet> getMockData() {
		List<Tweet> tweets = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			tweets.add(new Tweet(Double.toHexString(999999999999d*Math.random()), "Just another tweet, tweety tweety tweet. Lorum ipsum dolor sit amet, like seriously!", new Tweet.User("Pär Amsen", "url", "paramsen")));
		}

		return tweets;
	}
}
