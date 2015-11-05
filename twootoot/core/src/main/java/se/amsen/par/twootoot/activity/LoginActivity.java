package se.amsen.par.twootoot.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.component.twitter.LoginComponent;
import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.twitter.OAuthSource;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;

public class LoginActivity extends AppCompatActivity {

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Twootoot_Base_Login);
		setContentView(R.layout.activity_login);

		final LoginComponent login = (LoginComponent) findViewById(R.id.login_component);

		new OAuthSource(this).authorizeAsync(null, new Callback<Result<OAuthConfig>>() {
			@Override
			public void onComplete(Result<OAuthConfig> result) {
				Snackbar.make(login, result.isSuccess() ? "User is logged in" : "User is not logged in", Snackbar.LENGTH_SHORT).show();
			}
		});
	}
}
