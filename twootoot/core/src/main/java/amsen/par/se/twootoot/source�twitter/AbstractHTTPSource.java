package amsen.par.se.twootoot.source.twitter;

import amsen.par.se.twootoot.source.AbstractSource;
import amsen.par.se.twootoot.source.twitter.result.Result;
import amsen.par.se.twootoot.webcom.Message;
import amsen.par.se.twootoot.webcom.ResourceConfig;

/**
 * HTTPSource is a Source that provides HTTP logic for webcom.
 *
 * @author params on 28/10/15
 */
public abstract class AbstractHTTPSource<Request extends Message, Response extends Message, Config extends ResourceConfig, Result1 extends Result, Param1, Param2> extends AbstractSource<Result1, Param1, Param2> {
	public Result<Response> performRequest(Request req, Config config) {
		throw new RuntimeException("Not yet implemented");
	}
}
