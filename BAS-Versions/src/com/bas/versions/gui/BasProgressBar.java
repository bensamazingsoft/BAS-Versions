package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class BasProgressBar extends JFrame implements Observer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JProgressBar bar;
	private JLabel label;

	public BasProgressBar(String title, int nbOp) {

		this.setSize(500, 100);
		this.setTitle(title);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);

		bar = new JProgressBar();
		label = new JLabel();
		bar.setMaximum(nbOp);
		bar.setMinimum(0);
		bar.setStringPainted(true);

		this.getContentPane().add(bar, BorderLayout.CENTER);
		this.getContentPane().add(label, BorderLayout.NORTH);

	}

	@Override
	public void update(Observable arg0, final Object arg1) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String str = ((BasPgBarUpdtePair) (arg1)).getStr();
				int i = ((BasPgBarUpdtePair) (arg1)).getI();
				bar.setValue(i);
				label.setText(str);
			}
		});
	}

}
