package se.amsen.par.twootoot.component.twitter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.behavior.NetworkExceptionBehavior;
import se.amsen.par.twootoot.component.Component;
import se.amsen.par.twootoot.component.behavior.WaitBehavior;
import se.amsen.par.twootoot.model.Event;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exceptions.NetworkException;

/**
 * @author params on 04/11/15
 */
public class LoginComponent extends Component implements WaitBehavior<Void> {
	private OAuthSource oauth;

	/**
	 * Expose other constructors if instantiation through other than default Android XML inflation
	 * needed.
	 */
	public LoginComponent(Context context, AttributeSet attrs) {
		super(context, attrs);

		oauth = new OAuthSource(getContext());
	}

	@Override
	public void onCreate() {
		render(R.layout.component_login);

		getComponentRoot().setVisibility(GONE);
		setLoaderVisibility(VISIBLE);

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

	public void authorize() {
		oauth.authorizeAsync(new OAuthConfig.OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET), getAuthorizeCallback(), TimeUnit.SECONDS, 30);
	}

	public Callback<Result<OAuthConfig>> getAuthorizeCallback() {
		return new Callback<Result<OAuthConfig>>() {
			@Override
			public void onComplete(Result<OAuthConfig> result) {
				if(result.isSuccess()) {
					//TODO open next
				} else {
					if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
						NetworkExceptionBehavior.showSnackbar(getActivity(), new OnClickListener() {
							@Override
							public void onClick(View v) {
								authorize();
							}
						});
					} else {
						Snackbar.make(getRootView(), "Incorrect token or secret", Snackbar.LENGTH_SHORT);
					}
				}
			}
		};
	}
}
