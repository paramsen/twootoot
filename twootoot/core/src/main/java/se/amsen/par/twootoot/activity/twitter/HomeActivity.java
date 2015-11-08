package se.amsen.par.twootoot.activity.twitter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.BaseActivity;
import se.amsen.par.twootoot.behavior.exception.NetworkExceptionBehavior;
import se.amsen.par.twootoot.component.twitter.TweetListComponent;
import se.amsen.par.twootoot.model.Event;
import se.amsen.par.twootoot.model.twitter.HomeTimelineList;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.model.twitter.Tweet;
import se.amsen.par.twootoot.source.ImageSource;
import se.amsen.par.twootoot.source.twitter.FireAndForgetSource;
import se.amsen.par.twootoot.source.twitter.HomeTimelineSource;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exception.NetworkException;
import se.amsen.par.twootoot.webcom.twitter.resource.StatusUpdateResource;

/**
 * Activity for the home timeline
 *
 * @author params on 06/11/15
 */
public class HomeActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {
	private TweetListComponent tweetListComponent;
	private AppBarLayout appBarLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);

		initViews(savedInstanceState);
		getTweets();
	}

	@Override
	protected void onPause() {
		super.onPause();

		appBarLayout.removeOnOffsetChangedListener(this);
	}

	private void initViews(Bundle savedInstanceState) {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
		appBarLayout.addOnOffsetChangedListener(this);

		//quickfix for CoordinatorLayout not retaining state yet.. means that on relayout top will be contracted for on orientation change
		if(savedInstanceState != null) {
			appBarLayout.setExpanded(false);
		}

		tweetListComponent = (TweetListComponent) findViewById(R.id.tweetListComponent);

		findViewById(R.id.fabTweet).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HomeActivity.this.startActivity(new Intent(HomeActivity.this, TweetActivity.class));
			}
		});
	}

	private void onStatusUpdate(final String status) {
		new OAuthSource(this).authorizeAsync(null, new Callback<Result<OAuthConfig>>() {
			@Override
			public void onComplete(Result<OAuthConfig> result) {
				if (result.isSuccess()) {
					OAuthConfig config = result.asSuccess().get();
					StatusUpdateResource.StatusUpdateRequest req = new StatusUpdateResource.StatusUpdateRequest(config, status);

					new FireAndForgetSource<>(HomeActivity.this, req, StatusUpdateResource.StatusUpdateResponse.class, config).fire(new Callback<Result<Void>>() {
						@Override
						public void onComplete(Result<Void> result) {
							if (result.isSuccessIgnoreValue()) {
								Snackbar.make(HomeActivity.this.findViewById(android.R.id.content), "Status posted", Snackbar.LENGTH_LONG).show();
							} else {
								onStatusUpdateError(status);
							}
						}
					}, TimeUnit.SECONDS, 30);
				} else {
					onStatusUpdateError(status);
				}
			}
		}, TimeUnit.SECONDS, 30);
	}

	private void onStatusUpdateError(final String status) {
		Snackbar.make(findViewById(android.R.id.content), "Could not update your status", Snackbar.LENGTH_INDEFINITE)
				.setAction("Retry", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onStatusUpdate(status);
					}
				})
				.show();
	}

	private void getTweets() {
		new HomeTimelineSource(this).getAsync(new HomeTimelineSource.Params(199, null, null), new Callback<Result<HomeTimelineList>>() {
			@Override
			public void onComplete(Result<HomeTimelineList> result) {
				if(result.isSuccess()) {
					tweetListComponent.onReady(new Event<>(result.asSuccess().get().tweets));
					getProfileImages(result.asSuccess().get().tweets);
				} else if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
					NetworkExceptionBehavior.showSnackbar(HomeActivity.this, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							getTweets();
						}
					});
				} else {
					Snackbar.make(HomeActivity.this.findViewById(android.R.id.content), "Could not retrieve timeline", Snackbar.LENGTH_INDEFINITE)
							.setAction("Retry", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									getTweets();
								}
							}).show();
				}
			}
		}, TimeUnit.SECONDS, 30);
	}

	private void getProfileImages(final List<Tweet> tweets) {
		List<Pair<Integer, String>> pairedTweets = new ArrayList<>();
		for(int i = 0 ; i < tweets.size() ; i++) {
			pairedTweets.add(new Pair<>(i, tweets.get(i).user.profileImageUrl));
		}

		new ImageSource(this).getDrawables(new ImageSource.Params(tweetListComponent, pairedTweets, new Callback<Pair<Integer, Drawable>>() {
			@Override
			public void onComplete(Pair<Integer, Drawable> result) {
				tweets.get(result.first).user.profileImageDrawable = result.second;
				tweetListComponent.getDataSetAdapter().notifyItemChanged(result.first);
			}
		}), new Callback<Result<Void>>() {
			@Override
			public void onComplete(Result<Void> result) {
				if (result.isSuccessIgnoreValue()) {
					return;
				} else if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
					NetworkExceptionBehavior.showSnackbar(HomeActivity.this, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							getProfileImages(tweets);
						}
					});
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_scrolling, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
		tweetListComponent.swipeRefreshEnabled(verticalOffset == 0);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if(savedInstanceState != null) {
			Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(TweetListComponent.class.getName());
			tweetListComponent.setRestoredInstanceState(savedRecyclerLayoutState);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		tweetListComponent.onSaveInstanceState(outState);
	}
}
