package com.bas.versions.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.bas.versions.utils.Project;
import javax.swing.border.CompoundBorder;
import javax.swing.UIManager;

public class MainPnl extends JPanel implements Observer {
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
	JLabel lblNewLabel_2 = new JLabel("");
	JLabel lblNewLabel_4 = new JLabel("");
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JPanel panel_5 = new JPanel();
	private final JPanel panel_6 = new JPanel();
	private final JPanel panel_7 = new JPanel();

	/**
	 * Create the panel.
	 */
	public MainPnl() {

		setPreferredSize(new Dimension(900, 500));
		setLayout(new BorderLayout(0, 0));

		JPanel north = new JPanel();
		north.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "<html><Strong> <font size=\"4\">project</font> </Strong></html>", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(north, BorderLayout.NORTH);
		north.setLayout(new BorderLayout(5, 5));

		JPanel panel_12 = new JPanel();
		north.add(panel_12, BorderLayout.WEST);
		panel_12.setLayout(new BoxLayout(panel_12, BoxLayout.Y_AXIS));
		panel_5.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel_12.add(panel_5);
		panel_5.setLayout(new BoxLayout(panel_5, BoxLayout.Y_AXIS));
		projectLbl.setBounds(new Rectangle(5, 0, 0, 0));
		panel_5.add(projectLbl);
		projectLbl.setHorizontalAlignment(SwingConstants.LEFT);
		projectLbl.setFont(new Font("Tahoma", Font.BOLD, 16));
		projectDirLbl.setBounds(new Rectangle(5, 0, 0, 0));
		panel_5.add(projectDirLbl);
		projectDirLbl.setHorizontalAlignment(SwingConstants.LEFT);

		panel_12.add(panel_7);
		panel_6.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel_12.add(panel_6);
		panel_6.setLayout(new BoxLayout(panel_6, BoxLayout.Y_AXIS));
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_6.add(panel_1);
		panel_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_1.add(lblNewLabel_2);
		panel_1.add(lastChkptLbl);
		lastChkptLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setHgap(10);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		flowLayout_2.setVgap(0);
		panel_6.add(panel_2);
		panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_2.add(lblNewLabel_4);
		panel_2.add(chkptDateLbl);

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		north.add(panel, BorderLayout.EAST);

		load.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		loadBut.setToolTipText("load project file");
		loadBut.setMnemonic('o');

		panel.add(loadBut);
		newBut.setToolTipText("create new project");
		newBut.setMnemonic('n');
		newBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseload();
			}
		});

		panel.add(newBut);

		JPanel center = new JPanel();
		center.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "<html><Strong> <font size=\"4\">files</font> </Strong></html>", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(center, BorderLayout.CENTER);
		center.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_3.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		center.add(panel_3, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Filter IN : ");
		panel_3.add(lblNewLabel);

		filterInTf = new JTextField();
		filterInTf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {

				if (filterInTf.getText().length() == 0) {
					JOptionPane.showMessageDialog(null, "filter IN can't be empty", "error", JOptionPane.ERROR_MESSAGE);
					filterInTf.setText("Type in filter keyword");
				}

				if (filterInTf.getText().contains("/") || filterInTf.getText().contains("\\")) {
					filterInTf.setText(filterInTf.getText().replaceAll("(\\\\?)|(/?)", ""));
					JOptionPane.showMessageDialog(null, "Can't have \"/\" or \"\\\" in filters", "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (filterInTf.getText().equals("Type in filter keyword")) {
					filterInTf.setText("");
				}
			}
		});
		panel_3.add(filterInTf);
		filterInTf.setColumns(25);

		JLabel lblFilterOut = new JLabel("Filter OUT : ");
		panel_3.add(lblFilterOut);

		filterOutTf = new JTextField();
		filterOutTf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (filterOutTf.getText().contains("/") || filterOutTf.getText().contains("\\")) {
					filterOutTf.setText(filterOutTf.getText().replaceAll("(\\\\?)|(/?)", ""));
					JOptionPane.showMessageDialog(null, "Can't have \"/\" or \"\\\" in filters", "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(filterOutTf);
		filterOutTf.setColumns(25);
		btnNewButton.setToolTipText("<html><p>Update file lists according to filters</p>\r\n</br>\r\n<p> Filter IN sets wich files to include in checkpoint</p>\r\n<p>Filter Out sets wich files to exclude of checkpoint</p>\r\n</br>\r\n<p>Filters are Case Insensitive</p>\r\n");
		btnNewButton.setMnemonic('u');
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateButAction();
			}
		});

		btnNewButton.setEnabled(false);
		panel_3.add(btnNewButton);

		panel4Files.setPreferredSize(new Dimension(300, 300));
		panel4Files.setBorder(new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
				"Files in project dir.", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		// panel_4.add(panel4Files, BorderLayout.WEST);
		panel4Files.setLayout(new BorderLayout(0, 0));

		panel4FilteredFiles.setPreferredSize(new Dimension(300, 300));
		panel4FilteredFiles
				.setBorder(new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
						"Filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		// panel_4.add(panel4FilteredFiles, BorderLayout.CENTER);
		panel4FilteredFiles.setLayout(new BorderLayout(0, 0));

		panel4ModFiles.setPreferredSize(new Dimension(300, 300));
		panel4ModFiles.setBorder(new TitledBorder(
				new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
				"New or modified filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		// panel_4.add(panel4ModFiles, BorderLayout.EAST);
		panel4ModFiles.setLayout(new BorderLayout(0, 0));

		JPanel panel_4 = new JPanel();
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel4Files, panel4FilteredFiles);
		split.setOneTouchExpandable(true);
		split.setDividerLocation(300);
		JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, split, panel4ModFiles);
		split2.setOneTouchExpandable(true);
		split2.setDividerLocation(600);

		panel_4.setLayout(new BorderLayout(0, 0));
		panel_4.add(split2);
		center.add(panel_4, BorderLayout.CENTER);

		JPanel panel_8 = new JPanel();
		add(panel_8, BorderLayout.SOUTH);
		panel_8.setLayout(new BorderLayout(0, 0));

		JPanel panel_9 = new JPanel();
		panel_8.add(panel_9, BorderLayout.EAST);
		createChkptBut.setToolTipText("create a new checkpoint in the project");
		createChkptBut.setMnemonic('c');
		createChkptBut.setEnabled(false);
		createChkptBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				commit();
			}
		});

		panel_9.add(createChkptBut);

		JPanel panel_10 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_10.getLayout();
		flowLayout_4.setAlignment(FlowLayout.RIGHT);
		panel_8.add(panel_10, BorderLayout.CENTER);

		JLabel lblNewLabel_1 = new JLabel("CheckPoint Message :");
		lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
		panel_10.add(lblNewLabel_1);
		msgTp.setToolTipText("Set message of the next checkpoint.");
		msgTp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				msgTp.setText(msgTp.getText().replaceAll("\"", "'"));
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				if (msgTp.getText().equals("Please insert message")){
					msgTp.setText("");
				}
				if (msgTp.getText().length() == 0){
					msgTp.setCaretPosition(0);
				}
			}
		});
		msgTp.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		msgTp.setPreferredSize(new Dimension(300, 45));
		panel_10.add(msgTp);

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
		new Thread(new Runnable() {

			@Override
			public void run() {
				int info = JOptionPane.OK_OPTION;
				if (MainPnl.this.msgTp.getText().length() == 0
						|| MainPnl.this.msgTp.getText().equals("Please insert message")) {
					new JOptionPane();
					info = JOptionPane.showConfirmDialog(null, "Warning", "Checkpoint message is empty",
							JOptionPane.OK_CANCEL_OPTION);
				}

				if (info == JOptionPane.OK_OPTION) {
					MainPnl.this.project.setVersMsg(MainPnl.this.msgTp.getText());
					MainPnl.this.project.commitCheckPoint();
					MainPnl.this.lblNewLabel_2.setText("Last checkpoint : ");
					MainPnl.this.lblNewLabel_4.setText("created : ");
				} else {
					MainPnl.this.msgTp.setText("Please insert message");
				}
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
					btnNewButton.setEnabled(true);

				}
			}
		}).start();

	}

	public void initPanel(Project project) {

		this.project = project;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainPnl.this.projectLbl.setText("/" + MainPnl.this.project.getProjectPath().toFile().getName() + "/");
				MainPnl.this.projectDirLbl.setText("Created : " + MainPnl.this.project.getDateCreated());
				if (MainPnl.this.project.getCheckPointStack().peekLast() != null) {
					MainPnl.this.lblNewLabel_2.setText("Last checkpoint : ");
					MainPnl.this.lblNewLabel_4.setText("created : ");
				}
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
				createChkptBut.setEnabled(true);
				revalidate();
				repaint();
			}
		});

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
					MainPnl.this.chkptDateLbl
							.setText(MainPnl.this.project.getCheckPointStack().peekLast().getDateCreated().toString());
				}
				revalidate();
				repaint();
			}
		});

	}
}
