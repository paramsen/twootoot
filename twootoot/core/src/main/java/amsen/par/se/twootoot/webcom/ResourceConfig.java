package amsen.par.se.twootoot.webcom;

import android.net.Uri;

import java.util.concurrent.TimeUnit;

/**
 * @author params on 27/10/15
 */
public interface ResourceConfig {
	public Uri getUri();
	public Action getAction();
	public AuthorizeMethod getAuthorizeMethod();
	public CacheMode getCacheMode();
	public RateLimit getRateLimit();
	public Window getWindow();

	public enum Action {
		GET,
		POST
	}

	public enum CacheMode {
		NORMAL_CACHE(TimeUnit.HOURS, 3),
		LONG_CACHE(TimeUnit.DAYS, 3),
		YEARS_CACHE(TimeUnit.DAYS, 999),
		NO_CACHE(TimeUnit.NANOSECONDS, 0);

		public final TimeUnit unit;
		public final int value;

		CacheMode(TimeUnit unit, int value) {
			this.unit = unit;
			this.value = value;
		}
	}

	public enum AuthorizeMethod {
		AUTHORIZE,
		NO_AUTHORIZE
	}

	public enum RateLimit {
		LIMIT,
		NO_LIMIT
	}

	public static class Window {
		public final TimeUnit window;
		public final int timeValue;
		public final int windowCalls;

		public Window(TimeUnit window, int timeValue, int windowCalls) {
			this.window = window;
			this.timeValue = timeValue;
			this.windowCalls = windowCalls;
		}
	}
}
