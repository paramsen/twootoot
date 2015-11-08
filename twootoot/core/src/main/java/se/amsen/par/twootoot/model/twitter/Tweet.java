package se.amsen.par.twootoot.model.twitter;

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
}
