package se.amsen.par.twootoot.model;

/**
 * Container for Events of generic type.
 *
 * @author params on 06/11/15
 */
public class Event<T> {
	private T value;

	public Event(T event) {
		this.value = event;
	}

	public T getValue() {
		return value;
	}
}
