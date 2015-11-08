package se.amsen.par.twootoot.component.twitter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.twitter.HomeActivity;
import se.amsen.par.twootoot.behavior.exception.NetworkExceptionBehavior;
import se.amsen.par.twootoot.component.Component;
import se.amsen.par.twootoot.component.behavior.WaitBehavior;
import se.amsen.par.twootoot.model.Event;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exception.NetworkException;

/**
 * Standalone Component for login, handles errors and retries etc. Utilizes Source pattern for async
 * validation of tokens supplied etc.
 *
 * @author params on 04/11/15
 */
public class LoginComponent extends Component implements WaitBehavior<Void> {
	private OAuthSource oauth;
	private EditText token;
	private EditText secret;

	public LoginComponent(Context context, AttributeSet attrs) {
		super(context, attrs);

		oauth = new OAuthSource(getContext());
	}

	@Override
	public void onCreate() {
		render(R.layout.component_login);

		getComponentRoot().setVisibility(GONE);
		setLoaderVisibility(VISIBLE);

		token = (EditText) findViewById(R.id.editTextToken);
		secret = (EditText) findViewById(R.id.editTextTokenSecret);
		findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				authorize();
			}
		});
	}

	@Override
	public void onReady(@Nullable Event<Void> aVoid) {
		setLoaderVisibility(GONE);
		initAnimation();
	}

	private void initAnimation() {
		AnimatorSet anims = new AnimatorSet();
		anims.setInterpolator(new DecelerateInterpolator());
		getComponentRoot().setVisibility(VISIBLE);
		getComponentRoot().setAlpha(0);
		anims.playTogether(ObjectAnimator.ofFloat(getComponentRoot(), View.TRANSLATION_Y, 150, 0),
				ObjectAnimator.ofFloat(getComponentRoot(), View.ALPHA, 0, 1));
		anims.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
		anims.setStartDelay(getResources().getInteger(android.R.integer.config_mediumAnimTime));
		anims.setInterpolator(new DecelerateInterpolator());
		anims.start();
	}

	/**
	 * This app requires you to authorize with my (Pär Amsens) private keypair so the login is
	 * "token" and "secret" to get acces to my private tokens for simplicity.
	 */
	public void authorize() {
		if(token.getText().toString().equalsIgnoreCase("token") && secret.getText().toString().equalsIgnoreCase("secret")) {
			oauth.authorizeAsync(new OAuthConfig.OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET), getAuthorizeCallback(), TimeUnit.SECONDS, 30);
		} else {
			onFailedAuth();
		}
	}

	public Callback<Result<OAuthConfig>> getAuthorizeCallback() {
		return new Callback<Result<OAuthConfig>>() {
			@Override
			public void onComplete(Result<OAuthConfig> result) {
				if(result.isSuccess()) {
					getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
					getActivity().finish();
				} else {
					if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
						NetworkExceptionBehavior.showSnackbar(getActivity(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								authorize();
							}
						});
					} else {
						onFailedAuth();
					}
				}
			}
		};
	}

	private void onFailedAuth() {
		Snackbar.make(getRootView(), "Incorrect token or secret", Snackbar.LENGTH_SHORT).show();
	}
}
