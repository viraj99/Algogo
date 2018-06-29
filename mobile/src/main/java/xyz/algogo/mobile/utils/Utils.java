package xyz.algogo.mobile.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import de.mateware.snacky.Snacky;
import xyz.algogo.mobile.R;

import java.io.*;

/**
 * Utilities methods.
 */

public class Utils {

	/**
	 * UTF-8 charsets.
	 */

	public static final String UTF_8 = "UTF-8";

	/**
	 * Reads a file.
	 *
	 * @param file The file.
	 *
	 * @return The file content.
	 *
	 * @throws IOException If any I/O exception occurs.
	 */

	public static String read(final File file) throws IOException {
		final FileInputStream input = new FileInputStream(file);

		final InputStreamReader inputStreamReader = new InputStreamReader(input);
		final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		final StringBuilder builder = new StringBuilder();
		String line;
		while((line = bufferedReader.readLine()) != null) {
			builder.append(line).append(System.getProperty("line.separator"));
		}

		input.close();
		return builder.toString();
	}

	/**
	 * Writes to a file.
	 *
	 * @param file The file.
	 * @param content Content to write.
	 *
	 * @throws IOException If any I/O exception occurs.
	 */

	public static void write(final File file, final String content) throws IOException {
		if(file.exists()) {
			file.delete();
		}

		final FileOutputStream output = new FileOutputStream(file);
		output.write(content.getBytes(UTF_8));
		output.close();
	}

	public static void notifyError(final Activity activity, final Throwable throwable) {
		Snacky.builder().setActivity(activity).setText(activity.getString(R.string.snackbar_error_generic, throwable.getClass().getName())).error().show();
	}

	/**
	 * Escapes a HTML string.
	 *
	 * @param string The HTML string.
	 *
	 * @return The escaped HTML string.
	 */

	public static String escapeHTML(final String string) {
		final StringBuilder out = new StringBuilder(Math.max(16, string.length()));
		for(int i = 0; i < string.length(); i++) {
			final char c = string.charAt(i);
			if(c > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
				out.append("&#");
				out.append((int) c);
				out.append(';');
			}
			else {
				out.append(c);
			}
		}
		return out.toString();
	}

	/**
	 * Converts a stack trace to a string.
	 *
	 * @param throwable The throwable.
	 *
	 * @return The stack trace as a string.
	 */

	public static String fromStackTrace(final Throwable throwable) {
		final StringWriter stringWriter = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);

		return stringWriter.toString();
	}

	/**
	 * Opens an URL.
	 *
	 * @param url The URL.
	 * @param activity Activity needed to start another activity.
	 */

	public static void openURL(final String url, final Activity activity) {
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}