package se.amsen.par.twootoot.source.twitter;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import se.amsen.par.twootoot.source.AbstractSource;
import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.webcom.Request;
import se.amsen.par.twootoot.webcom.Response;

/**
 * HTTPSource is a Source that provides HTTP logic for webcom.
 *
 * @author params on 28/10/15
 */
public abstract class AbstractHTTPSource<Req extends Request, Resp extends Response, Result1, Param1, Param2> extends AbstractSource<Result1,  Param1, Param2> {
	private static final String TAG = AbstractHTTPSource.class.getCanonicalName();

	public Result<Resp> performRequest(Req req, Class<Resp> responseClass) {
		Result<Resp> response = null;

		HttpURLConnection urlConnection = null;
		InputStreamReader reader = null;
		StringBuilder builder = new StringBuilder();

		try {
			throw new IOException("hej");
//			urlConnection = req.buildProcessedRequest();
//
//			reader = new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream()), BuildConfig.TWITTER_CHARSET);
//			int c = -1;
//
//			while((c = reader.read()) != -1) {
//				builder.append((char) c);
//			}
//
//			GsonUtil.gson.fromJson(builder.toString(), responseClass);
		} catch (IOException e) {
			Log.e(TAG, "Exception during HTTP logic", e);
			response = new Failure<>(e);
		} finally {
			try {
				if(urlConnection != null) {
					urlConnection.disconnect();
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception disconnecting", e);
			}

			try {
				if(reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "Exception closing reader", e);
			}
		}

		return response;
	}
}
