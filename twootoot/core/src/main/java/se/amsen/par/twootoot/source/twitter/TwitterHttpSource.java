package se.amsen.par.twootoot.source.twitter;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.AbstractSource;
import se.amsen.par.twootoot.source.result.Failure;
import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.source.result.Success;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.Request;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.exception.NetworkException;
import se.amsen.par.twootoot.webcom.twitter.exception.HttpStatusException;

/**
 * HTTPSource is a Source that provides HTTP logic for webcom.
 *
 * @author params on 28/10/15
 */
public abstract class TwitterHttpSource<Req extends Request, Resp extends Response, Param1, Result1> extends AbstractSource<Param1, Result1> {
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

			if(code == HttpURLConnection.HTTP_OK) {
				reader = new InputStreamReader(new BufferedInputStream(conn.getInputStream()), BuildConfig.TWITTER_CHARSET);
				int c = -1;

				while ((c = reader.read()) != -1) {
					builder.append((char) c);
				}

				return buildResponse(builder.toString(), responseClass);
			} else {
				return new Failure<>(new HttpStatusException("Status code is not OK (200) and responseMessage is: " + (message != null ? message : "null"), code));
			}
		} catch (IOException e) {
			Log.e(TAG, "Exception during HTTP logic", e);

			return new Failure<>(new NetworkException("Could not connect to Twitter", e));
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

	protected Result<Resp> buildResponse(String json, Class<Resp> responseClass) {
		return new Success<>(GsonUtil.twitterGson.fromJson(json, responseClass));
	}

	public Context getContext() {
		return context;
	}
}
