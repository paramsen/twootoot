package se.amsen.par.twootoot.webcom.twitter;

import android.net.Uri;

import amsen.par.se.testlib.UnitTestUtil;
import se.amsen.par.twootoot.BuildConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig;
import se.amsen.par.twootoot.twitter.OAuthConfig.OAuthTokens;
import se.amsen.par.twootoot.webcom.Request;

/**
 * @author params on 03/11/15
 */
public class TwitterRequestTest extends UnitTestUtil {
	TwitterRequest request;
	OAuthConfig oauth;

	@Override public void setUp() throws Exception {
		super.setUp();
		oauth = new OAuthConfig(new OAuthTokens(BuildConfig.OAUTH_ACCESS_TOKEN, BuildConfig.OAUTH_ACCESS_TOKEN_SECRET));
		oauth.consumerKey = twitterReqMockConsumerKey;
		oauth.accessToken = twitterReqMockAccessToken;
		oauth.nonce = twitterReqNonce;
		oauth.timestamp = twitterReqTimestamp;

		request = new TwitterRequest(Uri.parse(twitterReqUrl), Request.Method.GET, oauth);
	}

	public void testBuildSignatureBase() {
		assertEquals("Did not equal Twitter signature base", twitterReqSignatureBaseString, request.buildSignatureBase());
	}

	//==============================================================================================
	// Helpers
	//==============================================================================================

	String twitterReqMockConsumerKey = "hej-consumer";
	String twitterReqMockAccessToken = "hej-access";

	/**
	 * Exact base signature String processed by Twitter & request data for base signature below
	 */
	String twitterReqSignatureBaseString = "GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fhome_timeline.json&oauth_consumer_key%3D" + twitterReqMockConsumerKey + "%26oauth_nonce%3D3b1be1070a4fd7f9d71326498d68e82d%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1446549114%26oauth_token%3D" + twitterReqMockAccessToken + "%26oauth_version%3D1.0";
	String twitterReqUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json";
	String twitterReqNonce = "3b1be1070a4fd7f9d71326498d68e82d";
	long twitterReqTimestamp = 1446549114;
}