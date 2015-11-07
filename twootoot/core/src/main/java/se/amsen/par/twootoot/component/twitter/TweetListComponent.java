package se.amsen.par.twootoot.component.twitter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.component.Component;
import se.amsen.par.twootoot.component.behavior.WaitBehavior;
import se.amsen.par.twootoot.model.Event;
import se.amsen.par.twootoot.model.twitter.Tweet;

/**
 * TweetList component managing a RecyclerView.
 *
 * More info: https://www.bignerdranch.com/blog/recyclerview-part-1-fundamentals-for-listview-experts/
 *
 * @author params on 06/11/15
 */
public class TweetListComponent extends Component implements WaitBehavior<List<Tweet>> {
	private List<Tweet> tweets;
	private RecyclerView tweetList;

	public TweetListComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onCreate() {
		render(R.layout.component_tweet_list);

		setLoaderVisibility(VISIBLE);
		getComponentRoot().setVisibility(GONE);
	}

	@Override
	public void onReady(Event<List<Tweet>> event) {
		tweets = event.getValue();

		setLoaderVisibility(GONE);

		tweetList = (RecyclerView) findViewById(R.id.recyclerTweetList);
		tweetList.setLayoutManager(new LinearLayoutManager(getActivity()));
		tweetList.setAdapter(new TweetAdapter());

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
				Snackbar.make(getComponentRoot(), "Retweet", Snackbar.LENGTH_SHORT).show();
			}


		};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(tweetList);
		initAnimation();
	}

	public RecyclerView.Adapter getDataSetAdapter() {
		return tweetList.getAdapter();
	}

	private void initAnimation() {
		getComponentRoot().setVisibility(VISIBLE);
		getComponentRoot().setAlpha(0);
		ObjectAnimator anim = ObjectAnimator.ofFloat(getComponentRoot(), View.ALPHA, 0, 1);
		anim.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
		anim.setInterpolator(new DecelerateInterpolator());
		anim.setStartDelay(getResources().getInteger(android.R.integer.config_shortAnimTime));

		anim.start();
	}

	private class TweetAdapter extends RecyclerView.Adapter<TweetHolder> {
		@Override
		public TweetHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new TweetHolder(LayoutInflater.from(getActivity()).inflate(R.layout.list_item_tweet, parent, false));
		}

		@Override
		public void onBindViewHolder(TweetHolder holder, int position) {
			holder.bindTweet(tweets.get(position));
		}

		@Override
		public int getItemCount() {
			return tweets.size();
		}
	}

	private class TweetHolder extends RecyclerView.ViewHolder {
		private ImageView profilePicture;
		private TextView userDisplayName;
		private TextView userName;
		private TextView content;

		private Tweet tweet;

		public TweetHolder(View itemView) {
			super(itemView);
			profilePicture = (ImageView) itemView.findViewById(R.id.profilePicture);
			userDisplayName = (TextView) itemView.findViewById(R.id.userDisplayName);
			userName = (TextView) itemView.findViewById(R.id.userName);
			content = (TextView) itemView.findViewById(R.id.content);
		}

		public void bindTweet(Tweet tweet) {
			this.tweet = tweet;

			if(tweet.user.profileImageDrawable != null) {
				profilePicture.setImageDrawable(tweet.user.profileImageDrawable);
			}

			userDisplayName.setText(tweet.user.name);
			userName.setText("@"+tweet.user.screenName);
			content.setText(tweet.text);


		}
	}
}
