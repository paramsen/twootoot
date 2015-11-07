package se.amsen.par.twootoot.activity;

import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.functional.Func;
import se.amsen.par.twootoot.util.utils.ConnectionUtil;

/**
 * @author params on 06/11/15
 */
public class BaseActivity extends AppCompatActivity {
	public void hasConnection(final Callback<Boolean> callback) {
		new AsyncRunner<Void, Boolean>().exec(new Func<Result<Boolean>>() {
			@Override
			public Result<Boolean> doFunc() {
				return new Success<>(ConnectionUtil.isOnline());
			}
		}, new Callback<Result<Boolean>>() {
			@Override
			public void onComplete(Result<Boolean> result) {
				callback.onComplete(result.isSuccess() && result.asSuccess().get());
			}
		}, TimeUnit.SECONDS, 30);
	}
}
