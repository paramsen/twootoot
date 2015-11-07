package se.amsen.par.twootoot.source;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.functional.Func1;

/**
 * @author params on 07/11/15
 */
public class ImageSource extends AbstractSource<ImageSource.Params, Void> {
	private Context context;

	public ImageSource(Context context) {
		this.context = context;
	}

	public AsyncRunner getDrawables(Params params) {
		return asyncGetResult1(params, new Callback<Result<Void>>() {
			@Override
			public void onComplete(Result<Void> result) {}
		}, null, null);
	}

	@Override
	protected Func1<Params, Result<Void>> getFunc1() {
		return new Func1<Params, Result<Void>>() {
			@Override
			public Result<Void> doFunc(Params params) {
				for(Pair<Integer, String> pair : params.idUrlList) {
					try {
						Drawable drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream((InputStream) new URL(pair.second).getContent()));
						params.callback.onComplete(new Pair<>(pair.first, drawable));
					} catch (Exception e) {
					}
				}

				return new Success<>();
			}
		};
	}

	@Override
	public boolean invalidate() {
		return false;
	}

	public static class Params {
		public Params(List<Pair<Integer, String>> idUrlList, Callback<Pair<Integer, Drawable>> callback) {
			this.idUrlList = idUrlList;
			this.callback = callback;
		}

		public List<Pair<Integer, String>> idUrlList;
		public Callback<Pair<Integer, Drawable>> callback;
	}
}
