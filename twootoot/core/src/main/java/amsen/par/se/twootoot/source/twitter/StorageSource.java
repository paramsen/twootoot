package amsen.par.se.twootoot.source.twitter;

import amsen.par.se.twootoot.source.twitter.result.Result;

/**
 * Source for storing/getting to shared prefs.
 *
 * @author params on 25/10/15
 */
public class StorageSource <Value, Result1 extends Result> extends Source<Result1, String, Void>{
	public boolean store(String key, Value value) {
		return false;
	}

	public Result1 getByKey(String key) {
		throw new RuntimeException("Not yet implemented");
	}

	@Override public boolean invalidate() {
		return false;
	}
}
