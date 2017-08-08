package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.bas.versions.utils.Project;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;

public class MainPnl extends JPanel implements Observer{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3479038272175062068L;
	
	Project project;
	
	private JTextField filterOutTf;
	private JTextField filterInTf;
	JTextPane msgTp = new JTextPane();
	JButton loadBut = new JButton("Load");
	JButton newBut = new JButton("New");
	JButton createChkptBut = new JButton("Commit new CheckPoint");
	JButton btnNewButton = new JButton("update");
	JLabel projectDirLbl = new JLabel("");
	JLabel projectLbl = new JLabel("");
	JPanel panel4Files = new JPanel();
	JPanel panel4FilteredFiles = new JPanel();
	JPanel panel4ModFiles = new JPanel();
	JLabel lastChkptLbl = new JLabel("");
	JFileChooser load = new JFileChooser();
	JLabel chkptDateLbl = new JLabel("");

	/**
	 * Create the panel.
	 */
	public MainPnl() {
		
		
		setPreferredSize(new Dimension(900, 500));
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
		
		
		panel_1.add(projectLbl);
		projectLbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		north.add(panel, BorderLayout.EAST);
		
		
		
		load.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		panel.add(loadBut);
		newBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseload();
			}
		});
		
		
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
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		center.add(panel_3, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Filter IN : ");
		panel_3.add(lblNewLabel);
		
		filterInTf = new JTextField();
		panel_3.add(filterInTf);
		filterInTf.setColumns(25);
		
		JLabel lblFilterOut = new JLabel("Filter OUT : ");
		panel_3.add(lblFilterOut);
		
		filterOutTf = new JTextField();
		panel_3.add(filterOutTf);
		filterOutTf.setColumns(25);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateButAction();
			}
		});
		
		
		panel_3.add(btnNewButton);
		
		JPanel panel_4 = new JPanel();
		center.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		
		panel4Files.setPreferredSize(new Dimension(300, 300));
		panel4Files.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Files in project dir.", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.add(panel4Files, BorderLayout.WEST);
		panel4Files.setLayout(new BorderLayout(0, 0));
		
		
		
		panel4FilteredFiles.setPreferredSize(new Dimension(300, 300));
		panel4FilteredFiles.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.add(panel4FilteredFiles, BorderLayout.CENTER);
		panel4FilteredFiles.setLayout(new BorderLayout(0, 0));
		
		
		panel4ModFiles.setPreferredSize(new Dimension(300, 300));
		panel4ModFiles.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "New or modified filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.add(panel4ModFiles, BorderLayout.EAST);
		panel4ModFiles.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_8 = new JPanel();
		add(panel_8, BorderLayout.SOUTH);
		panel_8.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.EAST);
		createChkptBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				commit();
			}
		});
		
		
		panel_9.add(createChkptBut);
		
		JPanel panel_10 = new JPanel();
		panel_8.add(panel_10, BorderLayout.CENTER);
		
		JLabel lblNewLabel_1 = new JLabel("CheckPoint Message :");
		panel_10.add(lblNewLabel_1);
		msgTp.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		msgTp.setPreferredSize(new Dimension(300, 40));
		
		
		msgTp.setText("\r\n\r\n\r\n");
		panel_10.add(msgTp);
		
		JPanel panel_5 = new JPanel();
		panel_8.add(panel_5, BorderLayout.NORTH);
		
		JLabel lblNewLabel_2 = new JLabel("Last checkpoint : ");
		panel_5.add(lblNewLabel_2);
		
		
		panel_5.add(lastChkptLbl);
		
		JLabel lblNewLabel_4 = new JLabel("Created : ");
		panel_5.add(lblNewLabel_4);
		
		
		panel_5.add(chkptDateLbl);

	}

	protected void updateButAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainPnl.this.project.setFilterIn(filterInTf.getText());
				MainPnl.this.project.setFilterOut(filterOutTf.getText());
				MainPnl.this.update(null, null);
			}
		}).start();
	}

	protected void commit() {
		new Thread(new Runnable(){
			@Override
			public void run(){
				MainPnl.this.project.setVersMsg(MainPnl.this.msgTp.getText());
				MainPnl.this.project.commitCheckPoint();
			}

			
		}).start();
		
		
	}

	protected void chooseload() {
		final int result = load.showOpenDialog(getParent());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (result == JFileChooser.APPROVE_OPTION) {

					initPanel(new Project(Paths.get(load.getSelectedFile().getAbsolutePath())));
					MainPnl.this.project.addObserver(MainPnl.this);

				} 
			}
		}).start();
		
	}

	public void initPanel (Project project){
		
		this.project = project;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPnl.this.projectLbl.setText(MainPnl.this.project.getProjectPath().toFile().getName());
				MainPnl.this.projectDirLbl.setText("Created : " + MainPnl.this.project.getDateCreated());
				MainPnl.this.filterInTf.setText(MainPnl.this.project.getFilterIn());
				MainPnl.this.filterOutTf.setText(MainPnl.this.project.getFilterOut());
			}
		});
		this.project.updateSets();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPnl.this.panel4Files.add(new BasJTree(MainPnl.this.project.getProjectPath()));
				MainPnl.this.panel4FilteredFiles.add(new BasJTree(MainPnl.this.project.getProjectPath(),
						MainPnl.this.project.getListFilteredFile()));
				MainPnl.this.panel4ModFiles.add(
						new BasJTree(MainPnl.this.project.getProjectPath(), MainPnl.this.project.getListModFile()));
				if (MainPnl.this.project.getCheckPointStack().peek() != null) {
					MainPnl.this.lastChkptLbl.setText(MainPnl.this.project.getCheckPointStack().peek().toString());
				}
			}
		});
		revalidate();
		repaint();
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {

				MainPnl.this.filterInTf.setText(MainPnl.this.project.getFilterIn());
				MainPnl.this.filterOutTf.setText(MainPnl.this.project.getFilterOut());

		this.project.updateSets();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPnl.this.panel4Files.removeAll();
				MainPnl.this.panel4FilteredFiles.removeAll();
				MainPnl.this.panel4ModFiles.removeAll();
				MainPnl.this.panel4Files.add(new BasJTree(MainPnl.this.project.getProjectPath()));
				MainPnl.this.panel4FilteredFiles.add(new BasJTree(MainPnl.this.project.getProjectPath(),
						MainPnl.this.project.getListFilteredFile()));
				MainPnl.this.panel4ModFiles.add(
						new BasJTree(MainPnl.this.project.getProjectPath(), MainPnl.this.project.getListModFile()));
				if (MainPnl.this.project.getCheckPointStack().peekLast() != null) {
					MainPnl.this.lastChkptLbl.setText(MainPnl.this.project.getCheckPointStack().peekLast().toString());
					MainPnl.this.chkptDateLbl.setText(
							MainPnl.this.project.getCheckPointStack().peekLast().getDateCreated().toString());
				}
			}
		});
		revalidate();
		repaint();
	}
}
