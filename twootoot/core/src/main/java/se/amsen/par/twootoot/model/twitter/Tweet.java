package se.amsen.par.twootoot.model.twitter;

import se.amsen.par.twootoot.model.AbstractModel;

/**
 * Tweet model
 *
 * @author params on 06/11/15
 */
public class Tweet extends AbstractModel {
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
}
