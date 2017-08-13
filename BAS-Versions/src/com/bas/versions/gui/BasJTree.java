package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.io.File;
import java.nio.file.Path;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

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

	public BasJTree(Path projectPath) {
		
		this.setLayout(new BorderLayout(0, 0));

		root = new DefaultMutableTreeNode(projectPath.toFile().getName());

		for (File nom : projectPath.toFile().listFiles()) {

			DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName() + "\\");
			root.add(this.listFile(nom, node));
		}

		tree = new JTree(root);
		jsp = new JScrollPane(tree);
//		jsp.setPreferredSize(jsp.getParent().getSize());
		this.add(jsp, BorderLayout.CENTER);

	}

	public BasJTree(Path projectPath, Set<File> fileSet2keep) {
		
		this.setLayout(new BorderLayout(0, 0));

		for (File file : fileSet2keep) {
			fileNames += file.getAbsolutePath();
		}

		root = new DefaultMutableTreeNode(projectPath.toFile().getName());

		for (File nom : projectPath.toFile().listFiles()) {

			DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName() + "\\");
			root.add(this.listFile(nom, node));
		}

		tree = new JTree(root);

		TreeModel model = tree.getModel();

		DefaultMutableTreeNode modelRoot = (DefaultMutableTreeNode) model.getRoot();

		while (stop == false && modelRoot.getChildCount() != 0) {
			j = 0;
			stop = true;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelRoot.getChildAt(j);
			filterNode(node);
		}
		
		if (modelRoot.getChildCount() == 0){
			modelRoot.add(new DefaultMutableTreeNode("(vide)"));
		}

		tree = new JTree(model);
		jsp = new JScrollPane(tree);

		this.add(jsp, BorderLayout.CENTER);
	}

	private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node) {

		if (file.isFile())
			return new DefaultMutableTreeNode(file.getName());
		else {
			File[] list = file.listFiles();
			if (list == null)
				return new DefaultMutableTreeNode(file.getName());

			for (File nom : list) {

				DefaultMutableTreeNode subNode;
				if (nom.isDirectory()) {
					subNode = new DefaultMutableTreeNode(nom.getName() + "\\");
					node.add(this.listFile(nom, subNode));
				} else {
					subNode = new DefaultMutableTreeNode(nom.getName());
				}
				node.add(subNode);

			}
			return node;
		}

	}

	public void filterNode(DefaultMutableTreeNode node) {

		if (!fileNames.contains(node.toString())) {
			node.removeFromParent();
			stop = false;
		}
		
		DefaultMutableTreeNode[] nodeTab = new DefaultMutableTreeNode[node.getChildCount()];

		for (int n = 0; n < node.getChildCount(); n++) {
			nodeTab[n] = (DefaultMutableTreeNode) node.getChildAt(n);
		}

		for (DefaultMutableTreeNode subNode : nodeTab) {

			if (subNode.isLeaf()) {
				if (!fileNames.contains(subNode.toString())) {
					subNode.removeFromParent();
					stop = false;
				}

			} else {

				filterNode(subNode);

			}

		}
		j++;
	}
}
