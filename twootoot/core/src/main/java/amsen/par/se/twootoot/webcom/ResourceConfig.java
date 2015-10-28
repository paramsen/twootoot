package amsen.par.se.twootoot.webcom;

import android.net.Uri;

import java.util.concurrent.TimeUnit;

/**
 * @author params on 28/10/15
 */
public abstract class ResourceConfig {
	public final Uri uri;
	public final Action action;

	public ResourceConfig(Uri uri, Action action) {
		this.uri = uri;
		this.action = action;
	}

	public enum Action {
		GET,
		POST
	}
}
