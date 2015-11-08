package se.amsen.par.twootoot.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.source.result.Success;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.utils.ConnectionUtil;

/**
 * BaseActivity that all Activities in this project should extend. Provides shared logic for ex.
 * Components and Sources etc.
 *
 * @author params on 06/11/15
 */
public class BaseActivity extends AppCompatActivity {
	/**
	 * Async connection check
	 */
	public void hasConnection(final Callback<Boolean> callback) {
		new AsyncRunner<Void, Boolean>().exec(() ->
				new Success<>(ConnectionUtil.isOnline()), result ->
				callback.onComplete(result.isSuccess() && result.asSuccess().get()), TimeUnit.SECONDS, 30);
	}

	/**
	 * Post on current Thread. Due to scoping current Thread == Main Thread so touching GUI in Runnable
	 * is ok
	 */
	protected void postDelayed(Runnable run, TimeUnit unit, int delay) {
		new Handler().postDelayed(run, unit.toMillis(delay));
	}
}
