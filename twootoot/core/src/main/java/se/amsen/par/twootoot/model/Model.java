package se.amsen.par.twootoot.model;

/**
 * The Model is the M in MVC. All Twitter models except OAuthConfig are seen as immutable Value objects
 * and should generally not be modified after creation. Most Models for Twitter will be exact
 * representations of the matching Resource object (and therefore the exposed Twitter API itself).
 *
 * Models have access modifier public on all fields for keeping down code complexity.
 *
 * @author params on 26/10/15
 */
public interface Model {
}
