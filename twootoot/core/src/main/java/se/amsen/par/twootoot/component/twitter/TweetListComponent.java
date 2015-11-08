package se.amsen.par.twootoot.component.twitter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.behavior.exception.NetworkExceptionBehavior;
import se.amsen.par.twootoot.component.Component;
import se.amsen.par.twootoot.component.behavior.WaitBehavior;
import se.amsen.par.twootoot.model.Event;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.model.twitter.Tweet;
import se.amsen.par.twootoot.source.twitter.FireAndForgetSource;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exception.NetworkException;
import se.amsen.par.twootoot.webcom.twitter.resource.RetweetResource;

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
	private SwipeRefreshLayout refreshLayout;
	@Nullable private Parcelable restoredInstanceState;

	public TweetListComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onCreate() {
		render(R.layout.component_tweet_list);
		setLoaderVisibility(VISIBLE);
		getComponentRoot().setVisibility(GONE);
		initRefreshLayout();
	}

	private void initRefreshLayout() {
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
		refreshLayout.setEnabled(false);
		refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccentDark, R.color.colorPrimaryDark, R.color.colorAccent);
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				TweetListComponent.this.postDelayed(new Runnable() {
					@Override
					public void run() {
						Snackbar.make(TweetListComponent.this, "Updating timeline not yet implemented", Snackbar.LENGTH_SHORT).show();
						refreshLayout.setRefreshing(false);
					}
				}, TimeUnit.SECONDS.toMillis(3));
			}
		});
	}

	@Override
	public void onReady(Event<List<Tweet>> event) {
		tweets = event.getValue();

		initRecyclerView();
		initAnimation();
	}

	private void initRecyclerView() {
		setLoaderVisibility(GONE);

		tweetList = (RecyclerView) findViewById(R.id.recyclerTweetList);
		tweetList.setAdapter(new TweetAdapter());

		tweetList.getLayoutManager().onRestoreInstanceState(restoredInstanceState);

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
			@Override
			public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
				return false;
			}

			@Override
			public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
				onRetweet(viewHolder);
			}

			@Override
			public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
				return .99f;
			}

			@Override
			public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
			}
		};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(tweetList);

	}

	public RecyclerView.Adapter getDataSetAdapter() {
		return tweetList.getAdapter();
	}

	private void onRetweet(final RecyclerView.ViewHolder viewHolder) {
		new OAuthSource(getActivity()).authorizeAsync(null, new Callback<Result<OAuthConfig>>() {
			@Override
			public void onComplete(Result<OAuthConfig> result) {
				if (result.isSuccess()) {
					OAuthConfig config = result.asSuccess().get();
					RetweetResource.RetweetRequest req = new RetweetResource.RetweetRequest(config, tweets.get(viewHolder.getAdapterPosition()).idStr);

					new FireAndForgetSource<>(getActivity(), req, RetweetResource.RetweetResponse.class, config).fire(new Callback<Result<Void>>() {
						@Override
						public void onComplete(Result<Void> result) {
							if (result.isSuccessIgnoreValue()) {
								Snackbar.make(getComponentRoot(), "Retweeted", Snackbar.LENGTH_LONG).show();
								getDataSetAdapter().notifyItemChanged(viewHolder.getLayoutPosition());
							} else if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
								NetworkExceptionBehavior.showSnackbar(getActivity(), new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										onRetweet(viewHolder);
									}
								});
							} else {
								onRetweetError(viewHolder);
							}
						}
					}, TimeUnit.SECONDS, 30);
				} else {
					onRetweetError(viewHolder);
				}
			}
		}, TimeUnit.SECONDS, 30);
	}

	private void onRetweetError(final RecyclerView.ViewHolder viewHolder) {
		Snackbar.make(getComponentRoot(), "Could not retweet", Snackbar.LENGTH_LONG)
				.setAction("Retry", new OnClickListener() {
					@Override
					public void onClick(View v) {
						onRetweet(viewHolder);
					}
				})
				.show();
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

	public void swipeRefreshEnabled(boolean bool) {
		refreshLayout.setEnabled(bool);
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
		private boolean hasTransitioned;

		public TweetHolder(View itemView) {
			super(itemView);
			profilePicture = (ImageView) itemView.findViewById(R.id.profilePicture);
			userDisplayName = (TextView) itemView.findViewById(R.id.userDisplayName);
			userName = (TextView) itemView.findViewById(R.id.userName);
			content = (TextView) itemView.findViewById(R.id.content);
		}

		public void bindTweet(Tweet tweet) {
			if(tweet.user.profileImageDrawable != null) {
				if(!hasTransitioned) {
					profilePicture.setAlpha(0f);
					profilePicture.animate().alphaBy(1f).start();
					hasTransitioned = true;
				}

				profilePicture.setVisibility(VISIBLE);
				profilePicture.setImageDrawable(tweet.user.profileImageDrawable);
			} else {
				profilePicture.setVisibility(INVISIBLE);
			}

			userDisplayName.setText(tweet.user.name);
			userName.setText("@"+tweet.user.screenName);
			content.setText(tweet.text);


		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if(getRecyclerView() != null) {
			outState.putParcelable(TweetListComponent.class.getName(), getRecyclerView().getLayoutManager().onSaveInstanceState());
		}
	}

	public void setRestoredInstanceState(Parcelable restoredInstanceState) {
		this.restoredInstanceState = restoredInstanceState;
	}

	public RecyclerView getRecyclerView() {
		return tweetList;
	}
}
