package se.amsen.par.twootoot.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * A Component is a View and a (Micro)Controller (in MVC). It's an encapsulation for rendering,
 * performing and providing a set of actions. It is responsible for providing necessary callbacks
 * and hooks. A Component encapsulates behaviour.
 *
 * @author params on 04/11/15
 */
public abstract class Component extends FrameLayout {
	private FrameLayout root;

	public Component(Context context) {
		super(context);
		onCreate();
	}

	public Component(Context context, AttributeSet attrs) {
		super(context, attrs);
		onCreate();
	}

	public Component(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		onCreate();
	}

	@TargetApi(21) public Component(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		onCreate();
	}

	/**
	 * Instantiate View and call Component.render to render this View.
	 */
	public abstract void onCreate();

	/**
	 * Call Component.render() to inflate and show a view hierarchy
	 *
	 * @param resid Layout to inflate
	 */
	public void render(@LayoutRes int resid) { //TODO if view exists, remove and add new inflated one.
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		root = (FrameLayout) inflater.inflate(resid, (ViewGroup) getParent(), false);
		addView(root);
	}

	public FrameLayout getComponentRoot() {
		return root;
	}
}
