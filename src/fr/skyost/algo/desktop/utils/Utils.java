package fr.skyost.algo.desktop.utils;

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import fr.skyost.algo.desktop.dialogs.ErrorDialog;

public class Utils {

	/**
	 * Escapes HTML characters of the specified String.
	 * 
	 * @param string The String.
	 * 
	 * @return An escaped String.
	 */

	public static final String escapeHTML(final String string) {
		final StringBuilder builder = new StringBuilder(Math.max(16, string.length()));
		for(int i = 0; i < string.length(); i++) {
			final char charr = string.charAt(i);
			if(charr > 127 || charr == '"' || charr == '<' || charr == '>' || charr == '&') {
				builder.append("&#");
				builder.append((int)charr);
				builder.append(';');
			}
			else {
				builder.append(charr);
			}
		}
		return builder.toString();
	}

	/**
	 * Joins a String array.
	 * 
	 * @param joiner The character used to join arrays.
	 * @param strings The array.
	 * 
	 * @return The joined array.
	 */

	public static final String join(final char joiner, final String... strings) {
		final StringBuilder builder = new StringBuilder();
		for(final String string : strings) {
			builder.append(string + joiner);
		}
		builder.setLength(builder.length() - 1);
		return builder.toString();
	}

	/**
	 * Creates a dialog.
	 * 
	 * @param component The parent component.
	 * @param title The dialog's title.
	 * @param message The message of the dialog.
	 * @param tip The dialog's tip.
	 * @param objects The objects of the dialog.
	 * 
	 * @return <b>true</b> If the user has clicked on "OK".
	 * <br><b>false</b> Otherwise.
	 */

	public static final boolean createDialog(final Component component, final String title, final String message, final String tip, final Object... objects) {
		final List<Object> components = new ArrayList<Object>();
		components.add(new JLabel(message));
		components.addAll(Arrays.asList(objects));
		if(tip != null) {
			components.add(new JLabel("<html><b>" + LanguageManager.getString("utils.tip") + " :</b> <i>" + tip + "</i></html>"));
		}
		return JOptionPane.showConfirmDialog(component, components.toArray(new Object[components.size()]), title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION;
	}

	/**
	 * Reloads a JTree.
	 * 
	 * @param tree The JTree.
	 */

	public static final void reloadTree(final JTree tree) {
		reloadTree(tree, null);
	}

	/**
	 * Reloads a node in a JTree.
	 * 
	 * @param tree The JTree.
	 * @param node The node, can be null.
	 */

	public static final void reloadTree(final JTree tree, final TreeNode node) {
		if(node == null) {
			((DefaultTreeModel)tree.getModel()).reload();
		}
		else {
			((DefaultTreeModel)tree.getModel()).reload(node);
		}
		for(int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	/**
	 * Checks if a String is alpha.
	 * 
	 * @param string The String.
	 * 
	 * @return <b>true</b> If the String is alpha.
	 * <br><b>false</b> Otherwise.
	 */

	public static final boolean isAlpha(final String string) {
		for(char charr : string.toCharArray()) {
			if(!Character.isLetter(charr)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Gets a node's content.
	 * 
	 * @param node The node.
	 * @param spaces Current spaces.
	 * 
	 * @return The node's content.
	 */

	public static final String getNodeContent(final AlgoTreeNode node, final StringBuilder spaces) {
		final StringBuilder builder = new StringBuilder();
		builder.append(node.toString() + System.lineSeparator());
		final int childCount = node.getChildCount();
		if(childCount > 0) {
			spaces.append("  ");
			for(int i = 0; i != childCount; i++) {
				builder.append(spaces.toString() + getNodeContent((AlgoTreeNode)node.getChildAt(i), spaces));
			}
			spaces.delete(0, 2);
		}
		return builder.toString();
	}

	/**
	 * Gets the JAR parent folder.
	 * 
	 * @return The JAR parent folder.
	 * 
	 * @throws UnsupportedEncodingException If the URLDecoder fails to decode the string.
	 */

	public static final File getParentFolder() throws UnsupportedEncodingException {
		return new File(URLDecoder.decode(ClassLoader.getSystemClassLoader().getResource(".").getPath(), StandardCharsets.UTF_8.toString()));
	}

	/**
	 * GZIP a String.
	 * 
	 * @param string The String.
	 * 
	 * @return The compressed String.
	 * 
	 * @throws IOException If an Exception occurs.
	 */

	public static final byte[] gzip(final String string) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final GZIPOutputStream gzip = new GZIPOutputStream(output);
		final OutputStreamWriter writer = new OutputStreamWriter(gzip, StandardCharsets.UTF_8);
		writer.write(string);
		writer.close();
		gzip.close();
		writer.close();
		return output.toByteArray();
	}

	/**
	 * UnGZIP a String.
	 * 
	 * @param bytes The String (bytes).
	 * 
	 * @return The unGZIPed String.
	 * 
	 * @throws IOException If an Exception occurs.
	 */

	public static final String ungzip(final byte[] bytes) throws IOException {
		final InputStreamReader input = new InputStreamReader(new GZIPInputStream(new ByteArrayInputStream(bytes)), StandardCharsets.UTF_8);
		final StringWriter writer = new StringWriter();
		final char[] chars = new char[1024];
		for(int length; (length = input.read(chars)) > 0;) {
			writer.write(chars, 0, length);
		}
		return writer.toString();
	}
	
	/**
	 * Gets a list of resources in the selected package.
	 * 
	 * @param packagee The package.
	 * 
	 * @return A list of resources.
	 * 
	 * @author <a href="http://stackoverflow.com/a/3923182/3608831">Jigar Joshi</a>.
	 */
	
	public static final Collection<String> getResourcesInPackage(final String packagee) {
		final ArrayList<String> retval = new ArrayList<String>();
		final String classPath = System.getProperty("java.class.path", ".");
		final String[] classPathElements = classPath.split(File.pathSeparator);
		for(final String element : classPathElements) {
			retval.addAll(getResourcesInPackage(element, packagee));
		}
		return retval;
	}

	private static final Collection<String> getResourcesInPackage(final String element, final String packagee) {
		final File file = new File(element);
		return file.isDirectory() ? getResourcesFromDirectory(file, packagee) : getResourcesFromJarFile(file, packagee);
	}

	private static final Collection<String> getResourcesFromJarFile(final File file, final String packagee) {
		final ArrayList<String> resources = new ArrayList<String>();
		try {
			final ZipFile zip = new ZipFile(file);
			final Enumeration<? extends ZipEntry> enumeration = zip.entries();
			while(enumeration.hasMoreElements()) {
				final ZipEntry entry = enumeration.nextElement();
				final String fileName = entry.getName();
				if(fileName.contains(packagee)) {
					resources.add(fileName);
				}
			}
			zip.close();
		}
		catch(final Exception ex) {
			ErrorDialog.errorMessage(null, ex, true);
		}
		return resources;
	}

	private static final Collection<String> getResourcesFromDirectory(final File directory, final String packagee) {
		final ArrayList<String> values = new ArrayList<String>();
		for(final File file : directory.listFiles()) {
			if(file.isDirectory()) {
				values.addAll(getResourcesFromDirectory(file, packagee));
			}
			else {
				try {
					final String fileName = file.getCanonicalPath();
					if(fileName.contains(packagee)) {
						values.add(fileName);
					}
				}
				catch(final Exception ex) {
					ErrorDialog.errorMessage(null, ex, true);
				}
			}
		}
		return values;
	}

}