package se.amsen.par.twootoot.util.functional;

import android.os.AsyncTask;
import android.util.Log;

import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * @author params on 03/11/15
 */
public class AsyncRunner<Param1, Param2, Result1> {
	private static final String TAG = AsyncRunner.class.getName();

	/**
	 * AsyncRunner.execN(params.., func, callback) is a wrapper for the AsyncTask implementing a
	 * more functional pattern.
	 *
	 * TODO Could reuse code here
	 */
	public void exec(final Func<Result<Result1>> func, final Callback<Result<Result1>> callback) {
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

	public void exec1(final Param1 p1, final Func1<Param1, Result<Result1>> func, final Callback<Result<Result1>> callback) {
		new AsyncTask<Void, Void, Result<Result1>>() {
			@Override protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
			}

			@Override protected Result<Result1> doInBackground(Void... voids) {
				try {
					 return func.doFunc(p1);
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

	public void exec2(final Param1 p1, final Param2 p2, final Func2<Param1, Param2, Result<Result1>> func, final Callback<Result<Result1>> callback) {
		new AsyncTask<Void, Void, Result<Result1>>() {
			@Override protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
			}

			@Override protected Result<Result1> doInBackground(Void... voids) {
				try {
					return func.doFunc(p1, p2);
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