package se.amsen.par.twootoot.model.twitter;

import android.graphics.drawable.Drawable;

/**
 * @author params on 06/11/15
 */
public class Tweet {
	public String idStr;
	public String text;
	public User user;

	public Tweet() {
	}

	public Tweet(String idStr, String text, User user) {
		this.idStr = idStr;
		this.text = text;
		this.user = user;
	}

	public static class User {
		public String name;
		public String profileImageUrl;
		public String screenName;
		public Drawable profileImageDrawable;

		public User() {
		}

		public User(String name, String profileImageUrl, String screenName) {
			this.name = name;
			this.profileImageUrl = profileImageUrl;
			this.screenName = screenName;
		}
	}
}
