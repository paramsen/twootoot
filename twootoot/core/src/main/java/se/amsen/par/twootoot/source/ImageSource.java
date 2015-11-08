package se.amsen.par.twootoot.source;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.View;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.functional.Func1;
import se.amsen.par.twootoot.webcom.twitter.exceptions.NetworkException;

/**
 * @author params on 07/11/15
 */
public class ImageSource extends AbstractSource<ImageSource.Params, Void> {
	private Context context;

	public ImageSource(Context context) {
		this.context = context;
	}

	public AsyncRunner getDrawables(Params params, Callback<Result<Void>> callback) {
		return asyncGetResult1(params, callback, null, null);
	}

	@Override
	protected Func1<Params, Result<Void>> getFunc1() {
		return new Func1<Params, Result<Void>>() {
			@Override
			public Result<Void> doFunc(final Params params) {
				//TODO cache imgs to disk <URL, Path> & prioritize it
				Exception latestException = null;

				final Map<String, Drawable> backlog = new HashMap<>();

				for(final Pair<Integer, String> pair : params.idUrlList) {
					if(!backlog.containsKey(pair.second)) {
						try {
							final Drawable drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream((InputStream) new URL(pair.second).getContent()));
							backlog.put(pair.second, drawable);
							params.mainThreadReference.post(new Runnable() {
								@Override
								public void run() {
									params.callback.onComplete(new Pair<>(pair.first, drawable));
								}
							});
						} catch (Exception e) {
							latestException = new NetworkException(e);
						}
					} else {
						params.mainThreadReference.post(new Runnable() {
							@Override
							public void run() {
								params.callback.onComplete(new Pair<>(pair.first, backlog.get(pair.second)));
							}
						});
					}
				}

				return latestException == null ? new Success() : new Failure(latestException);
			}
		};
	}

	@Override
	public boolean invalidate() {
		return false;
	}

	public static class Params {
		public Params(View mainThreadReference, List<Pair<Integer, String>> idUrlList, Callback<Pair<Integer, Drawable>> callback) {
			this.mainThreadReference = mainThreadReference;
			this.idUrlList = idUrlList;
			this.callback = callback;
		}

		public View mainThreadReference;
		public List<Pair<Integer, String>> idUrlList;
		public Callback<Pair<Integer, Drawable>> callback;
	}
}
