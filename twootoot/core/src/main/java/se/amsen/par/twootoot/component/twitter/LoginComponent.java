package se.amsen.par.twootoot.component.twitter;

import android.content.Context;
import android.util.AttributeSet;

import se.amsen.par.twootoot.R;
import se.amsen.par.twootoot.component.Component;

/**
 * @author params on 04/11/15
 */
public class LoginComponent extends Component {
	public LoginComponent(Context context) {
		super(context);
	}

	public LoginComponent(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LoginComponent(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public LoginComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void onCreate() {
		render(R.layout.component_login);
	}

}
