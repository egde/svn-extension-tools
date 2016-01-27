package de.kazkazi.svnExtensionTools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;

import javax.swing.JOptionPane;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;

public class SVNExtensionTools {

	private static final int ERROR_END = -1;
	private static Logger logger = LoggerFactory.getLogger(SVNExtensionTools.class);

	public static void main(String args[]) {

		Options options = new Options();
		options.addOption("c", true, "Copy the remote repository URL for the selected file or folder");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			showUsage(options);
			System.exit(ERROR_END);
		}
		
		if (cmd.hasOption("c")) {
			String fileName = cmd.getOptionValue("c");
			copySVNURL(fileName);
		} else {
			showUsage(options);
		}
	}

	private static void showUsage(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "svn-ext", options );
	}

	private static void copySVNURL(String fileName) {
		File file = new File(fileName);

		SVNClientManager clientManager = SVNClientManager.newInstance();

		SVNStatusClient statusClient = clientManager.getStatusClient();
		SVNStatus status = null;
		try {
			status = statusClient.doStatus(file, false);
		} catch (SVNException e) {
			logger.error("SVN Status was not possible", e);
			String message = "Some error with SVN. Probably the file selected is not in SVN.";
			String title = "Error";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
			System.exit(ERROR_END);
		}
		if (status != null) {
			SVNURL fileSVNUrl = status.getRemoteURL();
			logger.info("SVN URL " + fileSVNUrl);
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection stringSelection = new StringSelection(fileSVNUrl.toString());
			clpbrd.setContents(stringSelection, null);
			String message= "Remote SVN URL has been coppied to the clipboard";
			String title = "Success!";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		} else {
			String message = "File "+fileName+" cannot be found in SVN";
			String title = "File not found in Repository";
			JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
		}
	}
}