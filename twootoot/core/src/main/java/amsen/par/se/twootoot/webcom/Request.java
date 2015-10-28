package amsen.par.se.twootoot.webcom;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author params on 28/10/15
 */
public abstract class Request extends Message {
	public final transient List<Pair<String, String>> headers;

	public Request() {
		headers = new ArrayList<>();
	}

	public Request(Pair<String, String>... additionalHeaders) {
		this();
		this.headers.addAll(Arrays.asList(additionalHeaders));
	}
}
