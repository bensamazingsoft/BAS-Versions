package com.bas.versions.gui;

import java.awt.Dimension;
import java.io.File;
import java.nio.file.Path;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

public class BasJTree extends JPanel {

	DefaultMutableTreeNode root;
	JTree tree;
	JScrollPane jsp;
	String fileNames = "";

	public BasJTree(Path projectPath) {

		root = new DefaultMutableTreeNode(projectPath.toFile().getName());

		for (File nom : projectPath.toFile().listFiles()) {

			DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName() + "\\");
			root.add(this.listFile(nom, node));
		}

		tree = new JTree(root);
		jsp = new JScrollPane(tree);
		jsp.setPreferredSize(new Dimension(280, 250));
		this.add(jsp);

	}

	public BasJTree(Path projectPath, Set<File> fileSet2keep) {

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

		for (int j = 0; j < modelRoot.getChildCount(); j++) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelRoot.getChildAt(j);
			filterNode(node);
		}

		tree = new JTree(model);
		jsp = new JScrollPane(tree);
		jsp.setPreferredSize(new Dimension(280, 250));
		this.add(jsp);
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
		}
		System.out.println(node.toString());
		System.out.println(node.getClass());

		DefaultMutableTreeNode[] nodeTab = new DefaultMutableTreeNode[node.getChildCount()];

		for (int n = 0; n < node.getChildCount(); n++) {
			nodeTab[n] = (DefaultMutableTreeNode) node.getChildAt(n);
		}

		for (DefaultMutableTreeNode subNode : nodeTab) {

			if (subNode.isLeaf()) {
				if (!fileNames.contains(subNode.toString())) {
					subNode.removeFromParent();
				}
				System.out.println(subNode.toString());
				System.out.println(subNode.getClass());
			} else {

				filterNode(subNode);

			}

		}
	}
}
