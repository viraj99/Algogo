package xyz.algogo.core.evaluator.atom;

/**
 * Creates a new interruption atom.
 * <br>Interruption atom are returned by the expression evaluator when the current evaluation is interrupted.
 */

public class InterruptionAtom extends Atom<Integer> {

	/**
	 * Creates a new interruption atom instance.
	 */

	public InterruptionAtom() {
		super(-1);
	}

	@Override
	public boolean hasSameType(final Atom atom) {
		return hasInterruptionType(atom);
	}

	@Override
	public InterruptionAtom copy() {
		return new InterruptionAtom();
	}

	/**
	 * Checks if the given atom has an interruption type.
	 *
	 * @param atom The given atom.
	 *
	 * @return Whether the given atom has an interruption type.
	 */

	public static boolean hasInterruptionType(final Atom atom) {
		return atom instanceof InterruptionAtom;
	}

}