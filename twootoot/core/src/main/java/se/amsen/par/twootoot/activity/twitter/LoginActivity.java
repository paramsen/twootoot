package se.amsen.par.twootoot.activity.twitter;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.BaseActivity;
import se.amsen.par.twootoot.behavior.NetworkExceptionBehavior;
import se.amsen.par.twootoot.component.twitter.LoginComponent;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
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
		hasConnection(result -> {
			if(result) {
				new OAuthSource(LoginActivity.this).authorizeAsync(null, result1 -> {
					if (result1.isSuccess()) {
						startActivity(new Intent(LoginActivity.this, HomeActivity.class));
						finish();
					} else {
						if (result1.asFailure().get() instanceof MissingOAuthConfigException) {
							login.onReady(null);
						} else {
							NetworkExceptionBehavior.showSnackbar(LoginActivity.this, v -> authorize());
						}
					}
				}, TimeUnit.SECONDS, 30);
			} else {
				NetworkExceptionBehavior.showSnackbar(LoginActivity.this, v -> authorize());
			}
		});
	}
}
