package se.amsen.par.twootoot.component.twitter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.component.Component;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;

/**
 * @author params on 04/11/15
 */
public class LoginComponent extends Component {
	private LoginBehaviour behaviour;

	public LoginComponent(Context context) {
		super(context);
	}

	public LoginComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoginComponent(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public LoginComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void onCreate() {
		behaviour = new LoginBehaviour();

		render(R.layout.component_login);

		behaviour.onCreate();
	}

	private class LoginBehaviour {
		OAuthSource oauth;

		public LoginBehaviour() {
			oauth = new OAuthSource(getContext());
		}

		public void onCreate() {
			findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					authorize();
				}
			});
		}

		public void authorize() {
			//TODO Enter waiting state
			//TODO Enter timeout
			oauth.authorizeAsync(new OAuthConfig.OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET), getAuthorizeCallback());
		}

		public Callback<Result<OAuthConfig>> getAuthorizeCallback() {
			return new Callback<Result<OAuthConfig>>() {
				@Override
				public void onComplete(Result<OAuthConfig> result) {
					Snackbar.make(LoginComponent.this, result.isSuccess() ? "Success" : "Failure", Snackbar.LENGTH_SHORT).show();
				}
			};
		}
	}
}
