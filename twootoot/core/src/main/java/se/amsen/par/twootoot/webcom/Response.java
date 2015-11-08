package se.amsen.par.twootoot.webcom;

import java.util.concurrent.TimeUnit;

/**
 * Generic Response that requires implementations to specify a fixed cache time. Could be overkill
 * to have these in rock hard numbers
 *
 * @author params on 28/10/15
 */
public abstract class Response extends Message {
	public final transient CacheMode cacheMode;

	public Response(CacheMode cacheMode) {
		this.cacheMode = cacheMode;
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
}
