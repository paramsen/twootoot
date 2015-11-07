package se.amsen.par.twootoot.activity.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.BaseActivity;
import se.amsen.par.twootoot.behavior.NetworkExceptionBehavior;
import se.amsen.par.twootoot.component.twitter.LoginComponent;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exceptions.MissingOAuthConfigException;

public class LoginActivity extends BaseActivity {
	private LoginComponent login;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTheme(R.style.Twootoot_Login);
		setContentView(R.layout.activity_login);

		login = (LoginComponent) findViewById(R.id.login_component);

		authorize();
	}

	private void authorize() {
		hasConnection(new Callback<Boolean>() {
			@Override
			public void onComplete(Boolean result) {
				if(result) {
					new OAuthSource(LoginActivity.this).authorizeAsync(null, new Callback<Result<OAuthConfig>>() {
						@Override
						public void onComplete(Result<OAuthConfig> result) {
							if (result.isSuccess()) {
								startActivity(new Intent(LoginActivity.this, HomeActivity.class));
							} else {
								if (result.asFailure().get() instanceof MissingOAuthConfigException) {
									login.onReady(null);
								} else {
									NetworkExceptionBehavior.showSnackbar(LoginActivity.this, new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											authorize();
										}
									});
								}
							}
						}
					}, TimeUnit.SECONDS, 30);
				} else {
					NetworkExceptionBehavior.showSnackbar(LoginActivity.this, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							authorize();
						}
					});
				}
			}
		});
	}
}
