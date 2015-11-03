package se.amsen.par.twootoot.util.functional;

import android.os.AsyncTask;
import android.util.Log;

import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * @author params on 03/11/15
 */
public class AsyncRunner<Result1, Func1 extends Func<Result1>> {
	private static final String TAG = AsyncRunner.class.getName();

	public void exec(final Func1 func, final Callback<Result1> callback) {
		new AsyncTask<Void, Void, Result<Result1>>() {
			@Override protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
			}

			@Override protected Result<Result1> doInBackground(Void... voids) {
				try {
					 return func.doFunc();
				} catch(Exception e) {
					Log.e(TAG, "Exception during func execution", e);

					return new Failure<>(e);
				}
			}

			@Override protected void onCancelled(Result<Result1> result) {
				super.onCancelled(result);
				callback.onComplete(result);
			}
		}.execute();
	}
}