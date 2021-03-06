package xyz.algogo.core.evaluator.expression;

import java.math.BigDecimal;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.NumberAtom;
import xyz.algogo.core.evaluator.atom.StringAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents an additive expression.
 */

public class AdditiveExpression extends LeftOpRightExpression {

	/**
	 * Creates an additive expression.
	 *
	 * @param left The left expression.
	 * @param relation The relation (plus or minus).
	 * @param right The right expression.
	 */

	public AdditiveExpression(final Expression left, final String relation, final Expression right) {
		super(left, relation, right);
	}

	@Override
	public Atom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Atom left = this.getLeft().evaluate(evaluator, context);
		final Atom right = this.getRight().evaluate(evaluator, context);

		if(!NumberAtom.hasNumberType(left) || !NumberAtom.hasNumberType(right)) {
			if(StringAtom.hasStringType(left) && StringAtom.hasStringType(right) && this.getOperator().equals("+")) {
				return new StringAtom((String)left.getValue() + right.getValue());
			}

			return NumberAtom.ZERO;
		}

		final BigDecimal leftNumber = (BigDecimal)left.getValue();
		final BigDecimal rightNumber = (BigDecimal)right.getValue();

		switch(this.getOperator()) {
			case "+":
				return new NumberAtom(leftNumber.add(rightNumber, context.getMathContext()));
			case "-":
				return new NumberAtom(leftNumber.subtract(rightNumber, context.getMathContext()));
			default:
				return NumberAtom.ZERO;
		}
	}

	@Override
	public AdditiveExpression copy() {
		return new AdditiveExpression(this.getLeft().copy(), this.getOperator(), this.getRight().copy());
	}

}