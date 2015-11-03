package se.amsen.par.twootoot.util.functional;

/**
 * Function with 2 parameters
 *
 * @author params on 03/11/15
 */
public interface Func2<Param1, Param2, Result>{
	Result doFunc(Param1 p1, Param2 p2);
}