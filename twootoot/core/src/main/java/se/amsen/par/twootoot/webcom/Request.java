package se.amsen.par.twootoot.webcom;

import android.net.Uri;
import android.util.Pair;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.amsen.par.twootoot.source.twitter.result.Result;
import se.amsen.par.twootoot.util.annotation.Exclude;

/**
 * @author params on 28/10/15
 */
public abstract class Request extends Message {
	@Exclude private Uri uri;
	@Exclude private Method method;
	@Exclude private List<Pair<String, String>> headers;

	public Request(Uri uri, Method method) {
		this.uri = uri;
		this.method = method;
		headers = new ArrayList<>();
	}

	public Request(Uri uri, Method method, List<Pair<String, String>> headers) {
		this(uri, method);
		this.headers.addAll(headers);
	}

	public String urlEncode(String data, String charset) {
		try {
			return URLEncoder.encode(data, charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(String.format("Failed to URL encode String [%s] using charset [%s]", data, charset), e);
		}
	}

	public void addHeaders(Pair<String, String>... headers) {
		this.headers.addAll(Arrays.asList(headers));
	}

	public Uri getUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public Method getMethod() {
		return method;
	}

	public List<Pair<String, String>> getHeaders() {
		return headers;
	}

	/**
	 * Do necessary processing of request and return an opened HttpURLConnection. Returns wrapped
	 * as a Result due to high risk of exceptions to be handled.
	 */
	public abstract Result<HttpURLConnection> buildProcessedRequest();

	public enum Method {
		GET,
		POST
	}
}
