package se.amsen.par.twootoot.source;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileReader;
import java.io.Serializable;

import se.amsen.par.twootoot.source.result.Failure;
import se.amsen.par.twootoot.source.result.Result;

/**
 * @author params on 07/11/15
 */
public class CacheStorageSource<Value extends Serializable> extends AbstractSource<Void, Void> {
	private Context context;

	public CacheStorageSource(Context context) {
		this.context = context;
	}

	public boolean store(String key, Value value) {
			String fileName = Uri.parse(key).getLastPathSegment();
		 	//TODO tobytes>base64>write
			return true;
	}

	public Result<Value> getPathByKey(String key) {
		try {
			FileReader fileReader = new FileReader(new File(context.getCacheDir(), key));
			//TODO read bytes>base64>cast
			return null;

		} catch (Exception e) {
			return new Failure<>(e);
		}
	}

	@Override
	public boolean invalidate() {
		return false; //TODO clean cache
	}
}
