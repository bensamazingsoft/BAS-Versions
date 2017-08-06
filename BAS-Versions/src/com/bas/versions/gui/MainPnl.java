package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MainPnl extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3479038272175062068L;
	private JTextField textField;
	private JTextField textField_1;
	JTextPane textPane = new JTextPane();
	JButton loadBut = new JButton("Load");
	JButton newBut = new JButton("New");
	JButton createChkptBut = new JButton("Commit new CheckPoint");
	JLabel projectDirLbl = new JLabel("New label");

	/**
	 * Create the panel.
	 */
	public MainPnl() {
		setPreferredSize(new Dimension(900, 600));
		setLayout(new BorderLayout(0, 0));
		
		JPanel north = new JPanel();
		north.setBorder(new TitledBorder(null, "Project", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(north, BorderLayout.NORTH);
		north.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_1.getLayout();
		flowLayout_2.setVgap(0);
		north.add(panel_1, BorderLayout.WEST);
		
		JLabel Project = new JLabel("Project :");
		panel_1.add(Project);
		
		JLabel projectLbl = new JLabel("New label");
		panel_1.add(projectLbl);
		projectLbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		north.add(panel, BorderLayout.EAST);
		
		
		panel.add(loadBut);
		
		
		panel.add(newBut);
		
		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		north.add(panel_2, BorderLayout.SOUTH);
		
		
		panel_2.add(projectDirLbl);
		
		JPanel center = new JPanel();
		center.setBorder(new TitledBorder(null, "Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		center.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Filter IN : ");
		panel_3.add(lblNewLabel);
		
		textField_1 = new JTextField();
		panel_3.add(textField_1);
		textField_1.setColumns(15);
		
		JLabel lblFilterOut = new JLabel("Filter OUT : ");
		panel_3.add(lblFilterOut);
		
		textField = new JTextField();
		panel_3.add(textField);
		textField.setColumns(15);
		
		JPanel panel_4 = new JPanel();
		center.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_5.setPreferredSize(new Dimension(300, 300));
		panel_5.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Files in project dir.", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.add(panel_5, BorderLayout.WEST);
		
		
		JPanel panel_6 = new JPanel();
		panel_6.setPreferredSize(new Dimension(300, 300));
		panel_6.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.add(panel_6, BorderLayout.CENTER);
		
		JPanel panel_7 = new JPanel();
		panel_7.setPreferredSize(new Dimension(300, 300));
		panel_7.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Modified & Filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.add(panel_7, BorderLayout.EAST);
		
		JPanel panel_8 = new JPanel();
		add(panel_8, BorderLayout.SOUTH);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.EAST);
		
		
		panel_9.add(createChkptBut);
		
		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		
		JLabel lblNewLabel_1 = new JLabel("CheckPoint Message :");
		panel_10.add(lblNewLabel_1);
		
		
		textPane.setText("");
		panel_10.add(textPane);

	}

}
