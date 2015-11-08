package se.amsen.par.twootoot.util.functional;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import se.amsen.par.twootoot.source.result.Failure;
import se.amsen.par.twootoot.source.result.Result;

/**
 * AsyncTask runner that performs inter-thread synchronization.
 *
 * AsyncRunner will only run ONE task at a time. If multiple tasks are started before previous is
 * finished, the currently running task will be cancelled.
 *
 * AsyncRunner expose a cancel() method for cancelling the currently running task, if any.
 *
 * AsyncRunner now features timeouts. Ex. this provides functionality to run dangerous HTTP logic
 * without risking that your Callback never get called.
 *
 * @author params on 03/11/15
 */
public class AsyncRunner<Param1, Result1> {
	private static final String TAG = AsyncRunner.class.getName();

	/**
	 * AtomicReference to abstract from synchronization
	 */
	private final AtomicReference<AsyncTask> taskRef = new AtomicReference<>();

	/**
	 * AsyncRunner.execN(params.., func, callback) is a wrapper for the AsyncTask implementing a
	 * more functional pattern.
	 *
	 * If TimeUnit != null a timeout will be set for the task. If the task runtime exceeds the timeout
	 * the task will be canceled, Callback.onComplete will be called with a Failure(TimeoutException)
	 *
	 * Also performs synchronized check for whether a AsyncTask belonging to this instance is running,
	 * if so currently running task will be canceled.
	 *
	 * @param unit Is Nullable, if no timeout is wanted, provide null as unit/timeout.
	 *
	 * TODO Could reuse code here
	 */
	public AsyncRunner exec(final Func<Result<Result1>> func, final Callback<Result<Result1>> callback, @Nullable final TimeUnit unit, @Nullable final Integer timeout) {
		AsyncTask old = taskRef.getAndSet(new AsyncTask<Void, Void, Result<Result1>>() {
			CountDownTimer timer;

			@Override
			protected void onPreExecute() {
				if(unit != null) {
					timer = new CountDownTimer(unit.toMillis(timeout), unit.toMillis(timeout)) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							AsyncRunner.this.cancel();
						}
					}.start();
				}
			}

			@Override
			protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
				taskRef.set(null);

				if(unit != null) {
					timer.cancel();
				}
			}

			@Override
			protected Result<Result1> doInBackground(Void... voids) {
				try {
					return func.doFunc();
				} catch (Exception e) {
					Log.e(TAG, "Exception during func execution", e);

					return new Failure<>(e);
				}
			}

			@Override
			protected void onCancelled(Result<Result1> result) {
				callback.onComplete(result != null ? result : new Failure<Result1>(new TimeoutException("timed out")));
			}
		}.execute());

		if(old != null) {
			old.cancel(true);
		}

		return this;
	}

	/**
	 * See exec(...) for info
	 */
	public AsyncRunner exec1(final Param1 p1, final Func1<Param1, Result<Result1>> func, final Callback<Result<Result1>> callback, @Nullable final TimeUnit unit, @Nullable final Integer timeout) {

		AsyncTask old = taskRef.getAndSet(new AsyncTask<Void, Void, Result<Result1>>() {
			CountDownTimer timer;

			@Override
			protected void onPreExecute() {
				if(unit != null) {
					timer = new CountDownTimer(unit.toMillis(timeout), unit.toMillis(timeout)) {
						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							AsyncRunner.this.cancel();
						}
					}.start();
				}
			}

			@Override
			protected void onPostExecute(Result<Result1> result) {
				callback.onComplete(result);
				taskRef.set(null);
				if(unit != null) {
					timer.cancel();
				}
			}

			@Override
			protected Result<Result1> doInBackground(Void... voids) {
				try {
					return func.doFunc(p1);
				} catch (Exception e) {
					Log.e(TAG, "Exception during func execution", e);

					return new Failure<>(e);
				}
			}

			@Override
			protected void onCancelled(Result<Result1> result) {
				callback.onComplete(result != null ? result : new Failure<Result1>(new TimeoutException("timed out")));
			}
		}.execute());

		if(old != null) {
			old.cancel(true);
		}

		return this;
	}

	/**
	 * @return true if there was an AsyncTask && if it cancelled successfully
	 */
	public boolean cancel() {
		AsyncTask old = taskRef.getAndSet(null);
		return old != null && old.cancel(true);
	}
}