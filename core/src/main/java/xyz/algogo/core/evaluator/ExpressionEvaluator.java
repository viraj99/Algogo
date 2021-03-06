package xyz.algogo.core.evaluator;

import java.math.MathContext;
import java.util.HashMap;

import ch.obermuhlner.math.big.BigDecimalMath;
import xyz.algogo.core.evaluator.atom.Atom;
import xyz.algogo.core.evaluator.atom.InterruptionAtom;
import xyz.algogo.core.evaluator.context.EvaluationContext;
import xyz.algogo.core.evaluator.expression.Expression;
import xyz.algogo.core.evaluator.function.Function;
import xyz.algogo.core.evaluator.function.neper.ExpFunction;
import xyz.algogo.core.evaluator.function.neper.Log10Function;
import xyz.algogo.core.evaluator.function.neper.Log2Function;
import xyz.algogo.core.evaluator.function.neper.LogFunction;
import xyz.algogo.core.evaluator.function.other.AbsFunction;
import xyz.algogo.core.evaluator.function.other.BernoulliFunction;
import xyz.algogo.core.evaluator.function.other.CeilingFunction;
import xyz.algogo.core.evaluator.function.other.FactorialFunction;
import xyz.algogo.core.evaluator.function.other.FloorFunction;
import xyz.algogo.core.evaluator.function.other.MaxFunction;
import xyz.algogo.core.evaluator.function.other.MinFunction;
import xyz.algogo.core.evaluator.function.other.RandomFunction;
import xyz.algogo.core.evaluator.function.root.RootFunction;
import xyz.algogo.core.evaluator.function.root.SqrtFunction;
import xyz.algogo.core.evaluator.function.trigonometric.ACosFunction;
import xyz.algogo.core.evaluator.function.trigonometric.ACosHFunction;
import xyz.algogo.core.evaluator.function.trigonometric.ASinFunction;
import xyz.algogo.core.evaluator.function.trigonometric.ASinHFunction;
import xyz.algogo.core.evaluator.function.trigonometric.ATanFunction;
import xyz.algogo.core.evaluator.function.trigonometric.ATanHFunction;
import xyz.algogo.core.evaluator.function.trigonometric.CosFunction;
import xyz.algogo.core.evaluator.function.trigonometric.CosHFunction;
import xyz.algogo.core.evaluator.function.trigonometric.SinFunction;
import xyz.algogo.core.evaluator.function.trigonometric.SinHFunction;
import xyz.algogo.core.evaluator.function.trigonometric.TanFunction;
import xyz.algogo.core.evaluator.function.trigonometric.TanHFunction;
import xyz.algogo.core.evaluator.variable.Variable;
import xyz.algogo.core.evaluator.variable.VariableType;

/**
 * Represents an expression evaluator which allows to parse, evaluate and return the result of given expression.
 */

public class ExpressionEvaluator {

	/**
	 * Evaluator variables.
	 */

	private final HashMap<String, Variable> variables = new HashMap<>();

	/**
	 * Evaluator functions.
	 */

	private final HashMap<String, Function> functions = new HashMap<>();

	/**
	 * Creates a new expression evaluator.
	 */

	public ExpressionEvaluator() {
		addDefaultVariables();
		addDefaultFunctions();
	}

	/**
	 * Parses and evaluates a given expression.
	 *
	 * @param expression The expression.
	 *
	 * @return The evaluation result.
	 */

	public final Atom evaluate(final String expression) {
		return evaluate(Expression.parse(expression));
	}

	/**
	 * Evaluates a given expression.
	 *
	 * @param expression The expression.
	 *
	 * @return The evaluation result.
	 */

	public final Atom evaluate(final Expression expression) {
		return evaluate(expression, new EvaluationContext());
	}

	/**
	 * Evaluates a given expression.
	 *
	 * @param expression The expression.
	 * @param context The evaluation context.
	 *
	 * @return The evaluation result.
	 */

	public final Atom evaluate(final Expression expression, final EvaluationContext context) {
		if(context.isStopped()) {
			return new InterruptionAtom();
		}

		return expression.evaluate(this, context);
	}

	/**
	 * Returns a variable.
	 *
	 * @param identifier The variable identifier.
	 *
	 * @return The variable.
	 */

	public Variable getVariable(final String identifier) {
		return variables.get(identifier);
	}

	/**
	 * Puts a new variable.
	 *
	 * @param variable The variable.
	 */

	public final void putVariable(final Variable variable) {
		variables.put(variable.getIdentifier(), variable);
	}

	/**
	 * Checks if the evaluator has the corresponding variable.
	 *
	 * @param identifier The variable identifier.
	 *
	 * @return Whether the evaluator has the corresponding variable.
	 */

	public final boolean hasVariable(final String identifier) {
		return variables.containsKey(identifier);
	}

	/**
	 * Removes a variable.
	 *
	 * @param identifier The variable identifier.
	 */

	public final void removeVariable(final String identifier) {
		variables.remove(identifier);
	}

	/**
	 * Clears all variables.
	 */

	public final void clearVariables() {
		variables.clear();
	}

	/**
	 * Returns all variables.
	 *
	 * @return All variables.
	 */

	public final Variable[] getVariables() {
		return variables.values().toArray(new Variable[variables.size()]);
	}

	/**
	 * Returns a function.
	 *
	 * @param identifier The function identifier.
	 *
	 * @return The function.
	 */

	public Function getFunction(final String identifier) {
		return functions.get(identifier);
	}

	/**
	 * Puts a new function.
	 *
	 * @param function The function.
	 */

	public final void putFunction(final Function function) {
		functions.put(function.getIdentifier(), function);
	}

	/**
	 * Checks if the evaluator has the corresponding function.
	 *
	 * @param identifier The function identifier.
	 *
	 * @return Whether the evaluator has the corresponding function.
	 */

	public final boolean hasFunction(final String identifier) {
		return functions.containsKey(identifier);
	}

	/**
	 * Removes a function.
	 *
	 * @param identifier The function identifier.
	 */

	public final void removeFunction(final String identifier) {
		functions.remove(identifier);
	}

	/**
	 * Clears all functions.
	 */

	public final void clearFunctions() {
		functions.clear();
	}

	/**
	 * Returns all functions.
	 *
	 * @return All functions.
	 */

	public final Function[] getFunctions() {
		return functions.values().toArray(new Function[functions.size()]);
	}

	/**
	 * Add default variables to the context.
	 */

	public final void addDefaultVariables() {
		putVariable(new Variable("pi", VariableType.NUMBER, BigDecimalMath.pi(MathContext.DECIMAL128)));
		putVariable(new Variable("e", VariableType.NUMBER, BigDecimalMath.e(MathContext.DECIMAL128)));
	}

	/**
	 * Add default functions to the context.
	 */

	public final void addDefaultFunctions() {
		putFunction(new ExpFunction());
		putFunction(new LogFunction());
		putFunction(new Log10Function());
		putFunction(new Log2Function());

		putFunction(new SqrtFunction());
		putFunction(new RootFunction());

		putFunction(new AbsFunction());
		putFunction(new BernoulliFunction());
		putFunction(new CeilingFunction());
		putFunction(new FactorialFunction());
		putFunction(new FloorFunction());
		putFunction(new MaxFunction());
		putFunction(new MinFunction());
		putFunction(new RandomFunction());

		putFunction(new CosFunction());
		putFunction(new SinFunction());
		putFunction(new TanFunction());

		putFunction(new CosHFunction());
		putFunction(new SinHFunction());
		putFunction(new TanHFunction());

		putFunction(new ACosFunction());
		putFunction(new ASinFunction());
		putFunction(new ATanFunction());

		putFunction(new ACosHFunction());
		putFunction(new ASinHFunction());
		putFunction(new ATanHFunction());
	}

}