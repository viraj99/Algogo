package xyz.algogo.core.evaluator.expression;

import xyz.algogo.core.evaluator.ExpressionEvaluator;
import xyz.algogo.core.evaluator.atom.BooleanAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.language.Language;

import java.math.BigDecimal;

/**
 * Represents an AND expression.
 */

public class AndExpression extends RelationalExpression {

	/**
	 * Creates a new AND expression.
	 *
	 * @param left The left expression.
	 * @param right The right expression.
	 */

	public AndExpression(final Expression left, final Expression right) {
		super(left, "&&", right);
	}

	@Override
	public final BooleanAtom evaluate(final ExpressionEvaluator evaluator, final EvaluationContext context) {
		final Object left = this.getLeft().evaluate(evaluator, context).getValue();
		final Object right = this.getRight().evaluate(evaluator, context).getValue();

		return new BooleanAtom(left.equals(BigDecimal.ONE) && right.equals(BigDecimal.ONE));
	}

	@Override
	public final String toLanguage(final Language language) {
		return language.translateAndExpression(this);
	}

	@Override
	public final AndExpression copy() {
		return new AndExpression(this.getLeft().copy(), this.getRight().copy());
	}

}