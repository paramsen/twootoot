package se.amsen.par.twootoot.behavior.coordinator_layout;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * CoordinatorLayout behavior test, source from:
 * https://github.com/ianhanniballake/cheesesquare/blob/92bcf7c8b57459051424cd512a032c12d24a41b3/app/src/main/java/com/support/android/designlibdemo/ScrollAwareFABBehavior.java
 *
 * @author params on 08/11/15
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
	public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
		super();
	}

	@Override
	public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
									   final View directTargetChild, final View target, final int nestedScrollAxes) {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
				|| super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton child,
							   final View target, final int dxConsumed, final int dyConsumed,
							   final int dxUnconsumed, final int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
		if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
			child.hide();
		} else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
			child.show();
		}
	}
}
