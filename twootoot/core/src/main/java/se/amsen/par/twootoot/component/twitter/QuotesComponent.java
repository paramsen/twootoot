package se.amsen.par.twootoot.component.twitter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.component.Component;

/**
 * Component animating over a set a quotes.
 *
 * @author params on 04/11/15
 */
public class QuotesComponent extends Component {
	private String[] quotes;
	private int iterator = 0;

	public QuotesComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onCreate() {
		render(R.layout.component_quotes);

		quotes = getResources().getStringArray(R.array.quotes);
		nextQuote(1000, 5000);
	}

	//TODO tie this to Activity lifecycle through Component so that we can pause/resume.
	public void nextQuote(int delay, int millisVisible) {
		List<Animator> introAnimators = new ArrayList<>();
		final List<Animator> outroAnimators = new ArrayList<>();

		String[] words = quotes[iterator].split(" ");

		applyWords(words);

		for(int i = 0 ; i < words.length ; i++) {
			long delayI = (long) (delay + i * 50 + (100 * Math.random()));
			long delayO = (long) (millisVisible + i * 50 + (100 * Math.random()));

			introAnimators.addAll(Arrays.asList(getWordAnim(i, true, delayI)));
			outroAnimators.addAll(Arrays.asList(getWordAnim(words.length-1-i, false, delayO)));
		}

		introAnimators.get(introAnimators.size()-1).addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				outroAnimators.get(outroAnimators.size() - 1).addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						nextQuote(500, 5000);
					}
				});

				for (Animator a : outroAnimators) {
					a.start();
				}
			}
		});

		iterator = ++iterator % quotes.length;

		for(Animator a : introAnimators) {
			a.start();
		}
	}

	private Animator[] getWordAnim(int pos, boolean isIntro, long delay) {
		int startX = isIntro ? 50 : 0;
		int endX = isIntro ? 0 : -50;

		int startAlpha = isIntro ? 0 : 1;
		int endAlpha = isIntro ? 1 : 0;

		int duration = isIntro ? 300 : 150;
		Interpolator interpolator = isIntro ? new BounceInterpolator() : new AccelerateInterpolator();

		ObjectAnimator xAnim = ObjectAnimator.ofFloat(getComponentRoot().getChildAt(pos), View.TRANSLATION_Y, startX, endX);
		ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(getComponentRoot().getChildAt(pos), View.ALPHA, startAlpha, endAlpha);

		xAnim.setStartDelay(delay);
		xAnim.setInterpolator(interpolator);

		alphaAnim.setStartDelay(delay);
		alphaAnim.setInterpolator(interpolator);

		return new Animator[]{xAnim, alphaAnim};
	}

	public void applyWords(String[] words) {
		for(int i = 0 ; i < words.length ; i++) {
			TextView view = (TextView) getComponentRoot().getChildAt(i);
			view.setText(words[i]);
			view.setVisibility(VISIBLE);
			view.setAlpha(0);
		}
		int childCount = getComponentRoot().getChildCount();

		for(int j = words.length ; j < childCount ; j++) {
			TextView view = (TextView) getComponentRoot().getChildAt(j);
			view.setText("");
			view.setVisibility(GONE);
		}
	}
}
