package com.bas.versions.gui;

import static com.bas.versions.utils.LogGenerator.log;

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
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bas.versions.utils.BasProperties;
import com.bas.versions.utils.NightsWatcher;
import com.bas.versions.utils.Project;
import com.bas.versions.utils.ProjectRestorer;
import com.bas.versions.xml.ProjectParser;

public class MainPnl extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3479038272175062068L;

	Project project;

	private JTextField filterOutTf;
	private JTextField filterInTf;
	private JTextPane msgTp = new JTextPane();
	private JButton loadBut = new JButton("Load");
	private JButton newBut = new JButton("New");
	private JButton createChkptBut = new JButton("Commit new CheckPoint");
	private JButton updateBut = new JButton("update");
	private JLabel projectDirLbl = new JLabel("");
	private JLabel projectLbl = new JLabel("");
	private JPanel panel4Files = new JPanel();
	private JPanel panel4FilteredFiles = new JPanel();
	private JPanel panel4ModFiles = new JPanel();
	private JLabel lastChkptLbl = new JLabel("");
	private JFileChooser newJfc = new JFileChooser();
	private JFileChooser loadJfc = new JFileChooser();
	private JFileChooser archiveJFC = new JFileChooser();
	private JLabel chkptDateLbl = new JLabel("");
	private JLabel lastChkptIndicLbl = new JLabel("");
	private JLabel chkptDateIndicLbl = new JLabel("");
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JPanel panel_5 = new JPanel();
	private final JPanel panel_6 = new JPanel();
	private final JPanel panel_7 = new JPanel();
	private final JCheckBox noFilterCb = new JCheckBox("No filters");
	BasProperties config;
	NightsWatcher jon;
	private final JButton watchPauseBut = new JButton("Pause live update");
	private final JPanel panel_8 = new JPanel();
	private final JButton restoreBut = new JButton("Restore Files");
	private final JButton archiveBut = new JButton("Archive project");

	/**
	 * Create the panel.
	 */
	public MainPnl() {

		log("\n*********************************************************************" + "\nStarted " + new Date()
				+ "\n");

		config = new BasProperties();

		File confFile = new File("config.properties");
		if (confFile.exists()) {
			config.loadProperties(confFile);
			loadJfc.setCurrentDirectory(new File(config.getProp().getProperty("lastPath") + "\\Bas-CheckPoints"));
			newJfc.setCurrentDirectory(new File(config.getProp().getProperty("lastPath") + "\\Bas-CheckPoints"));
		}

		setPreferredSize(new Dimension(900, 500));
		setLayout(new BorderLayout(0, 0));
		restoreBut.setEnabled(false);

		JPanel north = new JPanel();
		north.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"<html><Strong> <font size=\"4\">project</font> </Strong></html>", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(0, 0, 0)));
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
		panel_1.add(lastChkptIndicLbl);
		lastChkptLbl.setHorizontalAlignment(SwingConstants.LEFT);
		panel_1.add(lastChkptLbl);
		lastChkptLbl.setFont(new Font("Tahoma", Font.BOLD, 11));
		FlowLayout flowLayout_2 = (FlowLayout) panel_2.getLayout();
		flowLayout_2.setHgap(10);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		flowLayout_2.setVgap(0);
		panel_6.add(panel_2);
		panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel_2.add(chkptDateIndicLbl);
		chkptDateLbl.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(chkptDateLbl);

		JPanel panel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		north.add(panel, BorderLayout.EAST);

		newJfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		archiveJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		loadJfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		loadJfc.setFileFilter(new FileNameExtensionFilter("*.basv filter", "basv"));

		loadBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadProject();
			}
		});
		loadBut.setToolTipText("load project file");
		loadBut.setMnemonic('o');

		panel.add(loadBut);
		newBut.setToolTipText("create new project");
		newBut.setMnemonic('n');
		newBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseNew();
			}
		});

		panel.add(newBut);

		JPanel center = new JPanel();
		center.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"<html><Strong> <font size=\"4\">files</font> </Strong></html>", TitledBorder.LEADING, TitledBorder.TOP,
				null, new Color(0, 0, 0)));
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
		updateBut.setToolTipText(
				"<html><p>Update file lists according to filters</p>\r\n</br>\r\n<p> Filter IN sets wich files to include in checkpoint</p>\r\n<p>Filter Out sets wich files to exclude of checkpoint</p>\r\n</br>\r\n<p>Filters are Case Insensitive</p>\r\n");
		updateBut.setMnemonic('u');
		updateBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateButAction();
			}
		});

		updateBut.setEnabled(false);
		panel_3.add(updateBut);
		noFilterCb.setSelected(true);
		noFilterCb.setEnabled(false);
		noFilterCb.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {

				if (noFilterCb.isSelected()) {
					MainPnl.this.project.setFilterIn("*");
					project.setFilterOut("");
					filterInTf.setEnabled(false);
					filterOutTf.setEnabled(false);
				}

				if (!noFilterCb.isSelected()) {
					MainPnl.this.project.setFilterIn(filterInTf.getText());
					filterInTf.setEnabled(true);
					filterOutTf.setEnabled(true);
					MainPnl.this.project.setFilterOut(filterOutTf.getText());
					MainPnl.this.project.reScan();
				}
			}

		});
		noFilterCb.setToolTipText("Disable filtering");

		panel_3.add(noFilterCb);

		watchPauseBut.setEnabled(false);
		watchPauseBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!NightsWatcher.isPause()) {
					NightsWatcher.setPause(true);
					watchPauseBut.setText("resume live update");
					return;
				}

				if (NightsWatcher.isPause()) {
					NightsWatcher.setPause(false);
					watchPauseBut.setText("Pause live update");
					MainPnl.this.project.reScan();
					return;
				}

			}
		});

		panel_3.add(watchPauseBut);

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

		JPanel south = new JPanel();
		add(south, BorderLayout.SOUTH);
		south.setLayout(new BorderLayout(0, 0));

		JPanel commitButtPnl = new JPanel();
		south.add(commitButtPnl, BorderLayout.EAST);
		createChkptBut.setToolTipText("create a new checkpoint in the project");
		createChkptBut.setMnemonic('c');
		createChkptBut.setEnabled(false);
		createChkptBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				commit();
			}
		});

		commitButtPnl.add(createChkptBut);

		JPanel msgPnl = new JPanel();
		FlowLayout fl_msgPnl = (FlowLayout) msgPnl.getLayout();
		fl_msgPnl.setAlignment(FlowLayout.RIGHT);
		south.add(msgPnl, BorderLayout.CENTER);

		JLabel msgPnlLbl = new JLabel("CheckPoint Message :");
		msgPnlLbl.setVerticalAlignment(SwingConstants.TOP);
		msgPnl.add(msgPnlLbl);
		msgTp.setToolTipText("Set message of the next checkpoint.");
		msgTp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				msgTp.setText(msgTp.getText().replaceAll("\"", "'"));
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				if (msgTp.getText().equals("Please insert message")) {
					msgTp.setText("");
				}
				if (msgTp.getText().length() == 0) {
					msgTp.setCaretPosition(0);
				}
			}
		});
		msgTp.setBorder(null);
		msgTp.setPreferredSize(new Dimension(300, 45));
		JScrollPane scrollPane = new JScrollPane(msgTp);
		msgPnl.add(scrollPane);

		south.add(panel_8, BorderLayout.SOUTH);
		restoreBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				RestoreFrame rf = new RestoreFrame(MainPnl.this.project);
			}
		});

		panel_8.add(restoreBut);
		archiveBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				archiveAction();
			}
		});

		archiveBut.setEnabled(false);
		panel_8.add(archiveBut);

	}

	protected void archiveAction() {

		final int result = archiveJFC.showOpenDialog(getParent());

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (result == JFileChooser.APPROVE_OPTION) {

					log("Started archiving project in folder : " + archiveJFC.getSelectedFile().getAbsolutePath()
							+ new Date());

					ProjectRestorer pr = new ProjectRestorer(project, archiveJFC.getSelectedFile().toPath(),
							MainPnl.this.project.getCheckPointStack().peekLast().getCheckPointId());
					pr.restoreFiles();
					

				}
			}
		}).start();

	}

	protected void updateButAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!noFilterCb.isSelected()) {
					MainPnl.this.project.setFilters(filterInTf.getText(), filterOutTf.getText());
				}
				MainPnl.this.project.reScan();
			}
		}).start();
	}

	protected void commit() {

		log("Committed checkPoint" + new Date());

		new Thread(new Runnable() {

			@Override
			public void run() {
				int info = JOptionPane.OK_OPTION;
				if (msgTp.getText().length() == 0 || msgTp.getText().equals("Please insert message")) {
					new JOptionPane();
					info = JOptionPane.showConfirmDialog(null, "Warning", "Checkpoint message is empty",
							JOptionPane.OK_CANCEL_OPTION);
				}

				if (info == JOptionPane.OK_OPTION) {
					MainPnl.this.project.setVersMsg(msgTp.getText());
					MainPnl.this.project.commitCheckPoint();
					lastChkptIndicLbl.setText("Last checkpoint :");
					chkptDateIndicLbl.setText("created : ");
					msgTp.setText("");
					restoreBut.setEnabled(true);
					archiveBut.setEnabled(true);
				} else {
					msgTp.setText("Please insert message");
				}
			}
		}).start();

	}

	protected void chooseNew() {

		log("New Project created" + new Date());

		final int result = newJfc.showOpenDialog(getParent());

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (result == JFileChooser.APPROVE_OPTION) {

					initPanel(new Project(Paths.get(newJfc.getSelectedFile().getAbsolutePath())));
					MainPnl.this.project.addObserver(MainPnl.this);
					updateBut.setEnabled(true);
					config.getProp().setProperty("lastPath",
							MainPnl.this.project.getProjectPath().toFile().getAbsolutePath());
					config.updateConf();

				}
			}
		}).start();

	}

	public void loadProject() {

		log("Project Loaded" + new Date());

		final int result = loadJfc.showOpenDialog(getParent());

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (result == JFileChooser.APPROVE_OPTION) {

					initPanel(new Project(new ProjectParser(loadJfc.getSelectedFile()).getDoc()));
					MainPnl.this.project.addObserver(MainPnl.this);
					updateBut.setEnabled(true);
					config.getProp().setProperty("lastPath",
							MainPnl.this.project.getProjectPath().toFile().getAbsolutePath());
					config.updateConf();
					restoreBut.setEnabled(true);
					archiveBut.setEnabled(true);
					update(null, null);
				}
			}
		}).start();

	}

	public void initPanel(Project project) {

		this.project = project;
		new Thread(new Runnable() {
			@Override
			public void run() {
				jon = new NightsWatcher(MainPnl.this.project);
			}
		}).start();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				watchPauseBut.setEnabled(true);
				projectLbl.setText("/" + MainPnl.this.project.getProjectPath().toFile().getName() + "/");
				projectDirLbl.setText("Created : " + MainPnl.this.project.getDateCreated());
				if (project.getCheckPointStack().peekLast() != null) {
					lastChkptIndicLbl.setText("Last checkpoint :");
					chkptDateIndicLbl.setText("created : ");
				}
				filterInTf.setText(MainPnl.this.project.getFilterIn());
				filterOutTf.setText(MainPnl.this.project.getFilterOut());
				panel4Files
						.add(new BasJTree(MainPnl.this.project.getProjectPath(), MainPnl.this.project.getListFile()));
				panel4FilteredFiles.add(new BasJTree(MainPnl.this.project.getProjectPath(),
						MainPnl.this.project.getListFilteredFile()));
				panel4ModFiles.add(
						new BasJTree(MainPnl.this.project.getProjectPath(), MainPnl.this.project.getListModFile()));
				if (MainPnl.this.project.getCheckPointStack().peekLast() != null) {
					lastChkptLbl.setText(MainPnl.this.project.getCheckPointStack().peekLast().toString().trim());
					chkptDateLbl.setText(
							MainPnl.this.project.getCheckPointStack().peekLast().getDateCreated().toString().trim());
				}

				createChkptBut.setEnabled(true);
				noFilterCb.setEnabled(true);
				revalidate();
				repaint();
			}
		});

	}

	@Override
	public void update(Observable arg0, Object arg1) {

		System.err.println("GUIupdate");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel4Files.removeAll();
				panel4FilteredFiles.removeAll();
				panel4ModFiles.removeAll();
				panel4Files
						.add(new BasJTree(MainPnl.this.project.getProjectPath(), MainPnl.this.project.getListFile()));
				panel4FilteredFiles.add(new BasJTree(MainPnl.this.project.getProjectPath(),
						MainPnl.this.project.getListFilteredFile()));
				panel4ModFiles.add(
						new BasJTree(MainPnl.this.project.getProjectPath(), MainPnl.this.project.getListModFile()));
				if (MainPnl.this.project.getCheckPointStack().peekLast() != null) {
					lastChkptLbl.setText(MainPnl.this.project.getCheckPointStack().peekLast().toString().trim());
					chkptDateLbl.setText(
							MainPnl.this.project.getCheckPointStack().peekLast().getDateCreated().toString().trim());
				}
				revalidate();
				repaint();
			}
		});

	}
}
