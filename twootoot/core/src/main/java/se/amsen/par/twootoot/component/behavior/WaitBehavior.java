package se.amsen.par.twootoot.component.behavior;

import se.amsen.par.twootoot.model.Event;

/**
 * Behavior that expose a onReady hook. Classes implementing this Behavior is meant to wait for a
 * "Go!" Event.
 *
 * @author params on 06/11/15
 */
public interface WaitBehavior<T> extends Behavior {
	void onReady(Event<T> event);
}
