package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextPane;

import com.bas.versions.utils.MessageIncrementer;
import com.bas.versions.utils.Project;

@SuppressWarnings("serial")
public class BasvWidget extends JFrame {

	private JTextPane textPane = new JTextPane();

	private Point pos;

	public BasvWidget(final Project project) {

		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setTitle("BASV Widget");


		JPanel panel = new JPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				pos = e.getPoint();
			}
		});
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent evt) {
				int xMove = evt.getX() + getLocation().x - pos.x;
				int yMove = evt.getY() + getLocation().y - pos.y;

				setLocation(xMove, yMove);

			}
		});
		getContentPane().add(panel, BorderLayout.CENTER);

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(panel, popupMenu);

		JMenuItem mntmNewMenuItem = new JMenuItem("Close");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		popupMenu.add(mntmNewMenuItem);

		JLabel lblNewLabel = new JLabel("BASV widget");
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));

		JSeparator separator = new JSeparator();
		panel.add(separator);
		textPane.setText("widget message");
		textPane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				textPane.setText(textPane.getText().replaceAll("\"", "'"));
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (textPane.getText().equals("widget message")) {
					textPane.setText("");
				}
				if (textPane.getText().length() == 0) {
					textPane.setCaretPosition(0);
				}
			}
		});
		textPane.setToolTipText("Set message of the next checkpoint.");
		textPane.setPreferredSize(new Dimension(150, 20));
		textPane.setBorder(null);
		panel.add(textPane);

		JButton btnNewButton = new JButton("Commit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				project.setVersMsg(textPane.getText());
				textPane.setText(MessageIncrementer.increment(textPane.getText()));
				new Thread(new Runnable() {
					@Override
					public void run() {
						project.commitCheckPoint(false);
					}
				}).start();

			}
		});
		panel.add(btnNewButton);

		pack();
		setVisible(true);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
