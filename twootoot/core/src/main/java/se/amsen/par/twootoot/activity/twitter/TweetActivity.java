package se.amsen.par.twootoot.activity.twitter;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.BaseActivity;
import se.amsen.par.twootoot.behavior.exception.NetworkExceptionBehavior;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.FireAndForgetSource;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exception.NetworkException;
import se.amsen.par.twootoot.webcom.twitter.resource.StatusUpdateResource;

/**
 * Activity for writing or answering tweets
 *
 * @author params on 07/11/15
 */
public class TweetActivity extends BaseActivity {
	TextView charsLeftText;
	EditText tweetView;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tweet);

		charsLeftText = (TextView) findViewById(R.id.charsLeft);
		tweetView = (EditText) findViewById(R.id.tweet);

		tweetView.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				charsLeftText.setText(String.valueOf(140 - tweetView.getText().length()));
			}
		});
	}

	public void onPositive(View v) {
		Editable text = tweetView.getText();

		if(text.length() > 0 && text.length() < 140) {
			onStatusUpdate(text.toString());
		}
	}

	public void onNegative(View v) {
		finish();
	}

	private void onStatusUpdate(final String status) {
		new OAuthSource(this).authorizeAsync(null, new Callback<Result<OAuthConfig>>() {
			@Override
			public void onComplete(Result<OAuthConfig> result) {
				if (result.isSuccess()) {
					OAuthConfig config = result.asSuccess().get();
					StatusUpdateResource.StatusUpdateRequest req = new StatusUpdateResource.StatusUpdateRequest(config, status);

					new FireAndForgetSource<>(TweetActivity.this, req, StatusUpdateResource.StatusUpdateResponse.class, config).fire(new Callback<Result<Void>>() {
						@Override
						public void onComplete(Result<Void> result) {
							if (result.isSuccessIgnoreValue()) {
								Snackbar.make(TweetActivity.this.findViewById(android.R.id.content), "Status posted", Snackbar.LENGTH_LONG).show();
								postDelayed(new Runnable() {
									@Override
									public void run() {
										finish();
									}
								}, TimeUnit.SECONDS, 1);
							}  else if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
								NetworkExceptionBehavior.showSnackbar(TweetActivity.this, new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										onStatusUpdate(status);
									}
								});
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
		Snackbar.make(findViewById(android.R.id.content), "Could not update your status", Snackbar.LENGTH_LONG)
				.setAction("Retry", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onStatusUpdate(status);
					}
				})
				.show();
	}
}
