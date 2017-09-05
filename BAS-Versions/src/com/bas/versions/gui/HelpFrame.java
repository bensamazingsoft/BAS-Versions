package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame {
	
	public HelpFrame(){
		
		setTitle("Help");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(750, 350));
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JEditorPane helpHtml = new JTextPane();
		helpHtml.setEditable(false);
		helpHtml.setPreferredSize(new Dimension(750, 350));
		helpHtml.setContentType("text/html");
		helpHtml.setText("\r\n<html>\r\n<h1><Strong>Ben's Amazing Software 'Versions' help v1.0</Strong></h1>\r\n<h2>Introduction</h2>\r\n<p>Ben's Amazing Software 'Versions' (BASV) is a versionning tool that backups files as you work.</p>\r\n<p>It allows you to create \"Checkpoints\" that are basically a backup of your project at any given time. \r\nYou can focus on your work and don't need to create extra copy of your files. </p>\r\n<p>It's also handy when in a team work environment you need to keep up with new/modified files.</p>\r\n<p>The software can restore your project to any previous Checkpoints state, restoring every file as needed. \r\nThis can save time if you work with a lot of source files.</p>\r\n<p></p>\r\n<h2>Starting a project</h2>\r\n<h3>Setting up</h3>\r\n<p>First you need to create a project. \r\nSelect Project -> New in the menu. \r\nYou are prompted to select a directory. \r\nThe software then creates his own folder and project files.</p>\r\n<h3>Filtering</h3>\r\n<p>At this point you may want to filter your files to determine which files you want to include in the Checkpoints. \r\nBe aware that any filtered out files won't be backed up and modification to these files won't be available to restore afterward. \r\nOf course you can choose to modify your filters or disable filtering at any time to include every files in further Checkpoints. \r\n\"Filter In\" is used to <em>include</em> files in Checkpoints. Filter Out is used to <em>exclude</em> files from Checkpoints. \r\nThe filtering is case insensitive, meaning that \"SourcesCli\" will match to \"sourcescli\" and vice-versa. \r\n<h2>Creating a Checkpoint</h2>\r\n<p>It is recommended that you commit a Checkpoint prior to going back to work on your files. \r\nThis will finish the set-up process and is generally good practice.</p>\r\n<p>To create, or commit, a Checkpoint simply type a message and click the commit button. \r\nThe message should be simple and meaningfull to ease future restoration processes. \r\nIf you end the message with a number index it will be automatically updated (you can format the number index xx or (xx) or [xx]). \r\nYou can set-up the software to automatically commit a new Checkpoint at a certain interval. \r\nUse the checkbox or menu to disable or enable auto-commit.</p>\r\n<p></p>\r\n<h1>Working with BASV</h1>\r\n<h3>Restoring files</h3>\r\n<p>There are two ways you can recover previous versions of your files. \r\n<ul>\r\n<li>Go and get them in the folders created for each Checkpoint. \r\nThis method is the more straightforward and should be used to recover parts of a Checkpoint, like a single file. \r\nThe folders are located in the Bas-Checkpoints folder of your project directory.</li>\r\n<li>Use the Restore tool. You access it via the menu Tools->Restore and then are prompted with the restoration window. \r\nChoose a Checkpoint from the drop-down menu and click the Restore button. After process completes your project is restored to the state it was in when you committed the Checkpoint.</li> \r\n</ul>\r\n<h3>Archiving projects</h3>\r\n<p>The archive feature of BASV creates a copy of the entire project folder to another location. It basically is the same as manually copying the  files but it  provides a fail-safe method to do it.\r\n\r\n<h3>Using the BASV Widget</h3>\r\n<p>You can use the BASV widget to get a small allways-on-top window. It is minimalistic and only provide a text area and a commit button. Simply drag the widget wherever convenient and commit your checkpoints from there without leaving your work application.</p> \r\n\r\n</html>\r\n");
		helpHtml.setCaretPosition(0);
		JScrollPane scp = new JScrollPane(helpHtml);
		scp.setBounds(new Rectangle(0,0,450,350));
		panel.setLayout(new BorderLayout(0, 0));
		
		panel.add(new JScrollPane(helpHtml), BorderLayout.CENTER);
		
		pack();
		setVisible(true);
	}

}
