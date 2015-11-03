package se.amsen.par.twootoot.util.functional;

import se.amsen.par.twootoot.source.twitter.result.Result;

/**
 * @author params on 03/11/15
 */
public interface Func<Expected>{
	Result<Expected> doFunc();
}