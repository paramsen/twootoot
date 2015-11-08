package se.amsen.par.twootoot.model.twitter;

import android.graphics.drawable.Drawable;

import se.amsen.par.twootoot.util.annotation.Exclude;

/**
 * @author params on 07/11/15
 */
public class User {
	public String name;
	public String profileImageUrl;
	public String screenName;
	@Exclude public Drawable profileImageDrawable;

	public User() {
	}

	public User(String name, String profileImageUrl, String screenName) {
		this.name = name;
		this.profileImageUrl = profileImageUrl;
		this.screenName = screenName;
	}
}