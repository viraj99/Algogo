package xyz.algogo.core.evaluator.expression;

import java.math.BigDecimal;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;

/**
 * Represents a OR expression.
 */

public class OrExpression extends RelationalExpression {

	/**
	 * Creates a new OR expression.
	 *
	 * @param left The left expression.
	 * @param right The right expression.
	 */

	public OrExpression(final Expression left, final Expression right) {
		super(left, "||", right);
	}

	@Override
	public BooleanAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Object left = this.getLeft().evaluate(evaluator, context).getValue();
		final Object right = this.getRight().evaluate(evaluator, context).getValue();

		return new BooleanAtom(left.equals(BigDecimal.ONE) || right.equals(BigDecimal.ONE));
	}

	@Override
	public OrExpression copy() {
		return new OrExpression(this.getLeft().copy(), this.getRight().copy());
	}

}