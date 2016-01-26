package de.kazkazi.svnExtensionTools;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;

public class SVNExtensionTools {

	private static Logger logger = LoggerFactory.getLogger(SVNExtensionTools.class);

	public static void main(String args[]) {

		File file = new File(args[0]);

		SVNClientManager clientManager = SVNClientManager.newInstance();

		SVNStatusClient statusClient = clientManager.getStatusClient();
		SVNStatus status = null;
		try {
			status = statusClient.doStatus(file, false);
		} catch (SVNException e) {
			logger.error("SVN Status was not possible", e);
		}
		if (status != null) {
			SVNURL fileSVNUrl = status.getRemoteURL();
			logger.info("SVN URL " + fileSVNUrl);
		}
		
	}
}