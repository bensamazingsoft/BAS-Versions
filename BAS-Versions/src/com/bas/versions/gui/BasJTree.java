package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class BasJTree extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2096094195155925324L;
	DefaultMutableTreeNode root;
	JTree tree;
	JScrollPane jsp;
	String fileNames = "";
	boolean stop = false;
	boolean subFound = false;
	int j;

	public BasJTree(Path projectPath, Set<File> fileSet2keep) {

		this.setLayout(new BorderLayout(0, 0));

		for (File file : fileSet2keep) {
			fileNames += file.getAbsolutePath();
		}

		root = new DefaultMutableTreeNode(projectPath.toFile().getName());

		for (File nom : projectPath.toFile().listFiles()) {

			DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName() + "\\");
			DefaultMutableTreeNode populate = this.listFile(nom, node);
			
			if (populate != null) {
				root.add(populate);
			}
		}

		if (root.getChildCount() == 0){
			root.add(new DefaultMutableTreeNode("No file to backup"));
		}
		tree = new JTree(root);
		jsp = new JScrollPane(tree);

		this.add(jsp, BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node) {

		if (file.isFile()) {
			if (fileNames.contains(file.getAbsolutePath())) {
				return new DefaultMutableTreeNode(file.getName());
			} else {
				return null;
			}
		} else {
			File[] list = file.listFiles();
			if (list == null) {
				return null;
			}
			for (File nom : list) {

				DefaultMutableTreeNode subNode;
				if (nom.isDirectory()) {
					subNode = new DefaultMutableTreeNode(nom.getName() + "\\");
					DefaultMutableTreeNode subPopulate = this.listFile(nom, subNode);
					if (subPopulate != null) {
						node.add(subPopulate);
					}
				} else {
					subNode = new DefaultMutableTreeNode(nom.getName());
					if (fileNames.contains(nom.getAbsolutePath())) {
						node.add(subNode);
					}
				}
			}
			if (node.getChildCount() != 0) {
				return node;
			} else {return null;}
		}

	}

}
