package se.amsen.par.twootoot.component;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.activity.BaseActivity;

/**
 * A Component is a View and a (Micro)Controller (in MVC). It's an encapsulation for rendering,
 * performing and providing a set of actions. It is responsible for providing necessary callbacks
 * and hooks. A Component encapsulates behaviour.
 *
 * @author params on 04/11/15
 */
public abstract class Component extends FrameLayout {
	private BaseActivity activity;

	private ViewGroup root;
	private ViewGroup componentRoot;
	private View loader;

	public Component(Context context, AttributeSet attrs) {
		super(context, attrs);

		try {
			activity = (BaseActivity) context;
		} catch(ClassCastException e) {
			throw new RuntimeException("Component is compatible only with BaseActivity");
		}

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
		root = (ViewGroup) inflater.inflate(resid, this, false);
		componentRoot = (ViewGroup) root.findViewById(R.id.component);

		if(componentRoot != null) {
			addView(root);

			loader = findViewById(R.id.loader);

			if(loader == null) {
				loader = inflater.inflate(R.layout.loader, this, false);

				int dimen = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
				LayoutParams params = new LayoutParams(dimen, dimen);
				params.gravity = Gravity.CENTER;
				loader.setLayoutParams(params);
				addView(loader);
			}

			loader.setVisibility(GONE);
		} else {
			throw new RuntimeException("Inflated layout for Component did not contain id:component");
		}
	}

	public ViewGroup getComponentRoot() {
		return componentRoot;
	}

	/**
	 * @param visibility == View.VISIBLE, View.INVISIBLE or View.GONE
	 */
	public void setLoaderVisibility(int visibility) {
		loader.setVisibility(visibility);
	}

	public BaseActivity getActivity() {
		return activity;
	}
}
