package se.amsen.par.twootoot.behavior;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.concurrent.TimeoutException;

import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.webcom.twitter.exceptions.NetworkException;

/**
 * @author params on 06/11/15
 */
public class NetworkExceptionBehavior {
	public <T extends Result> Callback<T> fallback(final Callback<T> callback, final Activity activity, final View.OnClickListener onClick) {
		return new Callback<T>() {
			@Override
			public void onComplete(T result) {
				if(result.isSuccess()) {
					callback.onComplete(result);
				} else if(result.asFailure().get() instanceof TimeoutException || result.asFailure().get() instanceof NetworkException) {
					showSnackbar(activity, onClick);
				} else {
					Snackbar.make(activity.findViewById(android.R.id.content), "We're sorry, something went wrong!", Snackbar.LENGTH_INDEFINITE)
							.setAction("Retry", onClick)
							.show();
				}
			}
		};
	}

	public static void showSnackbar(Activity activity, View.OnClickListener onClick) {
		Snackbar.make(activity.findViewById(android.R.id.content), "No connection", Snackbar.LENGTH_INDEFINITE)
				.setAction("Retry", onClick)
				.show();
	}
}
