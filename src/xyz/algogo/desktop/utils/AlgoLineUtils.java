package xyz.algogo.desktop.utils;

import java.util.LinkedHashMap;
import java.util.List;

import fr.skyost.heartbeat.Heartbeat;
import xyz.algogo.core.AlgoLine;
import xyz.algogo.core.Instruction;
import xyz.algogo.core.Keyword;

public class AlgoLineUtils {
	
	public static final String KEYWORD_COLOR = "#D35400";
	public static final String INSTRUCTION_COLOR_1 = "#22313F";
	public static final String INSTRUCTION_COLOR_2 = "#3498DB";
	
	/**
	 * Checks if the line is an IF and must be followed by an ELSE.
	 * 
	 * @param iff The line.
	 * 
	 * @return <b>true</b> If the IF must be followed by an ELSE.
	 * <b>false</b> Otherwise.
	 */
	
	public static final boolean ifFollowedByElse(final AlgoLine iff) {
		return iff.getInstruction() == Instruction.IF && Boolean.valueOf(iff.getArgs()[1]);
	}
	
	/**
	 * Gets the String representation for the specified keyword.
	 * 
	 * @param keyword The keyword.
	 * 
	 * @return The String representation.
	 */
	
	public static final String getLine(final Keyword keyword) {
		return "<html><b style=\"color:" + getLineColor(keyword) + "\">" + LanguageManager.getString("editor.line.keyword." + keyword.toString().toLowerCase()) + "</b></html>";
	}
	
	/**
	 * Gets the String representation for the specified instruction.
	 * 
	 * @param instruction The instruction.
	 * @param args The arguments.
	 * 
	 * @return The String representation.
	 */
	
	public static final String getLine(final Instruction instruction, final String... args) {
		final StringBuilder builder = new StringBuilder("<html><span style=color:\"" + getLineColor(instruction) + "\"><b>" + LanguageManager.getString("editor.line.instruction." + instruction.toString().replace("_", "").toLowerCase()) + "</b> ");
		switch(instruction) {
		case CREATE_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]) + " <b>" + LanguageManager.getString("editor.line.instruction.createvariable.type") + "</b> " + (args[1].equals("0") ? LanguageManager.getString("editor.line.instruction.createvariable.type.string") : LanguageManager.getString("editor.line.instruction.createvariable.type.number")));
			break;
		case ASSIGN_VALUE_TO_VARIABLE:
			builder.append(Utils.escapeHTML(args[0] + " → " + args[1]));
			break;
		case READ_VARIABLE:
			builder.append(Utils.escapeHTML(args[0]));
			final String customMessage = AlgoLineUtils.getCustomMessage(new AlgoLine(instruction, args));
			if(customMessage != null) {
				builder.append(" <b>" + LanguageManager.getString("editor.line.instruction.readvariable.message") + "</b> " + Utils.escapeHTML(customMessage));
			}
			break;
		case SHOW_VARIABLE:
		case SHOW_MESSAGE:
		case IF:
		case WHILE:
			builder.append(Utils.escapeHTML(args[0]));
			break;
		case FOR:
			builder.append(Utils.escapeHTML(args[0]) + " <b>" + LanguageManager.getString("editor.line.instruction.for.from") + "</b> " + args[1] + " <b>" + LanguageManager.getString("editor.line.instruction.for.to") + "</b> " + args[2]);
			break;
		case ELSE:
			builder.setLength(builder.length() - 1);
			break;
		}
		return builder.append("</span></html>").toString();
	}
	
	/**
	 * Gets the color of the specified keyword.
	 * 
	 * @param keyword The keyword.
	 * 
	 * @return The color.
	 */
	
	public static final String getLineColor(final Keyword keyword) {
		return KEYWORD_COLOR;
	}
	
	/**
	 * Gets the color of the specified instruction.
	 * 
	 * @param instruction The instruction.
	 * 
	 * @return The color.
	 */
	
	public static final String getLineColor(final Instruction instruction) {
		switch(instruction) {
		case CREATE_VARIABLE:
		case ASSIGN_VALUE_TO_VARIABLE:
		case SHOW_VARIABLE:
		case READ_VARIABLE:
		case SHOW_MESSAGE:
			return INSTRUCTION_COLOR_1;
		default:
			return INSTRUCTION_COLOR_2;
		}
	}
	
	/**
	 * Validates a line with its arguments.
	 * 
	 * @param variables The algorithm current variables.
	 * @param instruction The instruction.
	 * @param args The arguments.
	 * 
	 * @return An error message or null if the line can be created.
	 */
	
	public static final String validate(final List<String> variables, final Instruction instruction, final String... args) {
		switch(instruction) {
		case CREATE_VARIABLE:
			if(new Heartbeat().hasConstant(args[0]) || !Utils.isAlpha(args[0]) || args[0].isEmpty()) {
				return "addline.createvariable.error.notalpha";
			}
			if(variables != null && variables.contains(args[0])) {
				return "addline.createvariable.error.alreadyexists";
			}
			break;
		case SHOW_MESSAGE:
		case IF:
		case WHILE:
			if(args[0].isEmpty()) {
				return "joptionpane.fillfields";
			}
			break;
		case FOR:
			for(final String arg : args) {
				if(arg.isEmpty()) {
					return "joptionpane.fillfields";
				}
			} // We do not added a break because we want to check if the variable exists.
		case ASSIGN_VALUE_TO_VARIABLE:
		case SHOW_VARIABLE:
		case READ_VARIABLE:
			if(variables != null && !variables.contains(args[0])) {
				return "joptionpane.variabledoesnotexist";
			}
			break;
		case ELSE:
			break;
		}
		return null;
	}
	
	/**
	 * Gets variables as map.
	 * 
	 * @param variables The variables.
	 * 
	 * @return A map :
	 * <br><b>Key :</b> The variable's name.
	 * <br><b>Value :</b> The variable's type.
	 */
	
	public static final LinkedHashMap<String, Boolean> getVariables(final AlgoLine variables) {
		final LinkedHashMap<String, Boolean> variablesMap = new LinkedHashMap<String, Boolean>();
		for(final AlgoLine variable : variables.getChildren()) {
			final String[] args = variable.getArgs();
			variablesMap.put(args[0], args[1].equals("0"));
		}
		return variablesMap;
	}
	
	/**
	 * Checks if the READ_VARIABLE line has a custom prompt message.
	 * 
	 * @param readVariable The READ_VARIABLE line
	 * 
	 * @return The custom prompt message.
	 */
	
	public static final String getCustomMessage(final AlgoLine readVariable) {
		if(readVariable.isKeyword() || readVariable.getInstruction() != Instruction.READ_VARIABLE) {
			return null;
		}
		final String[] args = readVariable.getArgs();
		return args.length > 1 && args[1] != null && !args[1].equals("") ? args[1] : null;
	}

}