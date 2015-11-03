package se.amsen.par.twootoot.source.twitter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.AbstractSource;
import se.amsen.par.twootoot.source.GenericSourceException;
import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.source.twitter.result.Success;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.Request;
import se.amsen.par.twootoot.webcom.Response;

/**
 * HTTPSource is a Source that provides HTTP logic for webcom.
 *
 * @author params on 28/10/15
 */
public abstract class TwitterHttpSource<Req extends Request, Resp extends Response, Result1, Param1, Param2> extends AbstractSource<Result1,  Param1, Param2> {
	private static final String TAG = TwitterHttpSource.class.getName();

	private Context context;

	public TwitterHttpSource(Context context) {
		this.context = context;
	}

	protected Result<Resp> performRequest(Req req, Class<Resp> responseClass) {
		Result<HttpURLConnection> rawConn = req.buildProcessedRequest();

		if(rawConn.isSuccess()) {
			return processConnection(rawConn.asSuccess().get(), responseClass);
		} else {
			return new Failure<>(rawConn.asFailure().get());
		}
	}

	protected Result<Resp> processConnection(HttpURLConnection conn, Class<Resp> responseClass) {
		InputStreamReader reader = null;
		StringBuilder builder = new StringBuilder();

		try {
			int code = conn.getResponseCode();
			String message = conn.getResponseMessage();

			reader = new InputStreamReader(new BufferedInputStream(conn.getInputStream()), BuildConfig.TWITTER_CHARSET);
			int c = -1;

			while ((c = reader.read()) != -1) {
				builder.append((char) c);
			}

			return new Success<>(GsonUtil.gson.fromJson(builder.toString(), responseClass));
		} catch (Exception e) {
			Log.e(TAG, "Exception during HTTP logic", e);

			return new Failure<>(new GenericSourceException("Exception during HTTP logic", e)); //TODO create more declarative Exception
		} finally {
			try {
				if (conn != null) {
					conn.disconnect();
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception disconnecting", e);
			}

			try {
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception closing reader", e);
			}
		}
	}

	protected Failure<Resp> getTwitterExceptionForStatusCode(int code) {
		//401 auth fail
		//429 too many reqs
		return null;
	}

	public Context getContext() {
		return context;
	}
}
