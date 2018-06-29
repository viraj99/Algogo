package xyz.algogo.core.evaluator.function.trigonometric;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.EvaluationContext;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.function.Function;

import java.math.BigDecimal;

/**
 * Represents the <a href="https://en.wikipedia.org/wiki/Trigonometric_functions#Inverse_functions">Inverse cosine function</a>.
 */

public class ACosFunction extends Function {

	/**
	 * Creates a new inverse cosine function.
	 */

	public ACosFunction() {
		super("ACOS");
	}

	@Override
	public final NumberAtom evaluate(final EvaluationContext context, final Atom... arguments) {
		if(arguments.length == 0 || !NumberAtom.hasNumberType(arguments[0])) {
			return NumberAtom.ZERO;
		}

		return new NumberAtom(BigDecimalMath.acos((BigDecimal)arguments[0].getValue(), context.getMathContext()));
	}

}
