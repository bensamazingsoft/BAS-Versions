package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.bas.versions.utils.CheckPoint;
import com.bas.versions.utils.Project;
import com.bas.versions.utils.ProjectRestorer;

@SuppressWarnings("serial")
public class RestoreFrame extends JFrame {

	Project project;
	CheckPoint selectedCp;
	ProjectRestorer restorer;
	JPanel selCpPnl = new JPanel();
	JLabel selCpLbl = new JLabel("Select CheckPoint");
	JPanel panel = new JPanel();
	@SuppressWarnings("rawtypes")
	JComboBox comboBox;
	private final JPanel msgPnl = new JPanel();
	private final JTextPane msgTp = new JTextPane();
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JButton btnNewButton = new JButton("Restore files");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RestoreFrame(Project project) {

		this.project = project;

		setTitle("File Restoration");
		setSize(new Dimension(600, 300));
		setLocationRelativeTo(null);
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "files",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		selCpPnl.setLayout(new BorderLayout(0, 0));
		getContentPane().add(selCpPnl, BorderLayout.NORTH);

		selCpPnl.add(panel_1, BorderLayout.WEST);
		panel_1.add(selCpLbl);
		comboBox = new JComboBox(project.getCheckPointStack().toArray());
		if (comboBox.getSelectedItem() != null) {
			panel.add(new BasJTree(project.getProjectPath(),
					((CheckPoint) comboBox.getSelectedItem()).getChckPtFileList()), BorderLayout.CENTER);
			selectedCp = (CheckPoint) comboBox.getSelectedItem();
		}
		panel_1.add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					selectedCp = (CheckPoint) event.getItem();
					update();
				}
			}
		});

		selCpPnl.add(panel_2, BorderLayout.EAST);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				restoreButAction();
			}
		});

		panel_2.add(btnNewButton);
		FlowLayout flowLayout = (FlowLayout) msgPnl.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		msgPnl.setBackground(Color.WHITE);
		msgPnl.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "message",
				TitledBorder.LEADING, TitledBorder.BELOW_TOP, null, new Color(0, 0, 0)));

		msgPnl.add(msgTp);
		getContentPane().add(msgPnl, BorderLayout.SOUTH);

		if (comboBox.getSelectedItem() != null) {
			msgTp.setText(selectedCp.getChkPtMsg());
		}

		setVisible(true);

	}

	public void restoreButAction() {
		int action = JOptionPane.showConfirmDialog(this,
				"This will replace actual project files with checkpoint older version files. Are you sure, super-duper-sure ? ",
				"proceed ?", JOptionPane.OK_CANCEL_OPTION);

		if (action == JOptionPane.OK_OPTION) {
			restoreAction();
			dispose();
		}
	}

	protected void restoreAction() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				restorer = new ProjectRestorer(project, project.getProjectPath(), selectedCp.getId());
				restorer.restoreFiles();
			}
		}).start();
	}

	private void update() {
		panel.removeAll();
		if (comboBox.getSelectedItem() != null) {
			panel.add(new BasJTree(project.getProjectPath(), selectedCp.getChckPtFileList()));
			msgTp.setText(selectedCp.getChkPtMsg());
		}

		revalidate();
		repaint();
	}

}
