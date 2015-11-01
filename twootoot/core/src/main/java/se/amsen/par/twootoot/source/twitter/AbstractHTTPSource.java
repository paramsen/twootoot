package se.amsen.par.twootoot.source.twitter;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.source.AbstractSource;
import se.amsen.par.twootoot.source.GenericSourceException;
import se.amsen.par.twootoot.source.twitter.result.Failure;
import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.utils.GsonUtil;
import se.amsen.par.twootoot.webcom.Request;
import se.amsen.par.twootoot.webcom.Response;

/**
 * HTTPSource is a Source that provides HTTP logic for webcom.
 *
 * @author params on 28/10/15
 */
public abstract class AbstractHttpSource<Req extends Request, Resp extends Response, Result1, Param1, Param2> extends AbstractSource<Result1,  Param1, Param2> {
	private static final String TAG = AbstractHttpSource.class.getCanonicalName();

	protected Result<Resp> performRequest(Req req, Class<Resp> responseClass) {
		Result<Resp> response = null;

		HttpURLConnection urlConnection = null;
		InputStreamReader reader = null;
		StringBuilder builder = new StringBuilder();

		try {
			urlConnection = req.buildProcessedRequest();

			reader = new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream()), BuildConfig.TWITTER_CHARSET);
			int c = -1;

			while((c = reader.read()) != -1) {
				builder.append((char) c);
			}

			GsonUtil.gson.fromJson(builder.toString(), responseClass);
		} catch (Exception e) {
			Log.e(TAG, "Exception during HTTP logic", e);
			response = new Failure<>(new GenericSourceException("Exception during HTTP logic", e));
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
