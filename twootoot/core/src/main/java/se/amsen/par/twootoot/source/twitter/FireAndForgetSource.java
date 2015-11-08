package se.amsen.par.twootoot.source.twitter;

import android.content.Context;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.source.GenericSourceException;
import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.functional.Func1;
import se.amsen.par.twootoot.webcom.Request;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.exceptions.NetworkException;

/**
 * Source to be used for requests that require no particular result else than Success or Failure.
 *
 * @author params on 07/11/15
 */
public class FireAndForgetSource<Req extends Request, Resp extends Response> extends TwitterHttpSource<Req, Resp, Void, Void> {
	private Req req;
	private Class<Resp> resp;
	private OAuthConfig config;

	public FireAndForgetSource(Context context, Req req, Class<Resp> resp, OAuthConfig config) {
		super(context);

		this.req = req;
		this.resp = resp;
		this.config = config;
	}

	public AsyncRunner fire(Callback<Result<Void>> callback, TimeUnit unit, int timeout) {
		return asyncGetResult1(null, callback, unit, timeout);
	}

	@Override
	protected Func1<Void, Result<Void>> getFunc1() {
		return new Func1<Void, Result<Void>>() {
			@Override
			public Result<Void> doFunc(Void params) {
				Result<Resp> result = performRequest(req, resp);

				if(result.isSuccessIgnoreValue()) {
					return new Success<>();
				} else {
					return new Failure<>(result.asFailure().get());
				}
			}
		};
	}

	@Override
	protected Result<Resp> processConnection(HttpURLConnection conn, Class<Resp> responseClass) {
		try {
			int responseCode = conn.getResponseCode();
			String responseMessage = conn.getResponseMessage();

			if(responseCode == HttpURLConnection.HTTP_OK) {
				return new Success<>();
			} else {
				return new Failure<>(new GenericSourceException(String.format("Error during HTTP login [Code: %d | Msg: %s]", responseCode, responseMessage)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Failure<>(new NetworkException("Could not reach"));
	}
}
