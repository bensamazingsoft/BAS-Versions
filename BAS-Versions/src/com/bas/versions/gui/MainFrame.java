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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bas.versions.utils.BasProperties;
import com.bas.versions.utils.CheckPoint;
import com.bas.versions.utils.MessageIncrementer;
import com.bas.versions.utils.NightsWatcher;
import com.bas.versions.utils.Project;
import com.bas.versions.utils.ProjectRestorer;
import com.bas.versions.xml.ProjectParser;

public class MainFrame extends JFrame implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3479038272175062068L;

	Project project;

	private JTextField filterOutTf;
	private JTextField filterInTf;
	private JTextPane msgTp = new JTextPane();
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
	private final JLabel autoCommiWaitTimeLbl = new JLabel("Auto commit every (min)");
	private final JCheckBox autoCommiWaitTimeCb = new JCheckBox("");
	private final JTextField autoCommiWaitTimeTf = new JTextField();
	private final JPanel panel_9 = new JPanel();
	private Thread watchThread;
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu mnNewMenu = new JMenu("Project");
	private final JMenuItem mntmNewMenuItem = new JMenuItem("New");
	private final JMenuItem mntmNewMenuItem_1 = new JMenuItem("Load");
	private final JMenu mnTools = new JMenu("Tools");
	private final JMenuItem mntmCommit = new JMenuItem("Commit");
	private final JMenuItem mntmRestore = new JMenuItem("Restore");
	private final JMenuItem mntmArchive = new JMenuItem("Archive");
	private final JSeparator separator = new JSeparator();
	private final JMenuItem switchLiveUpdateMit = new JMenuItem("Disable live update");
	private final JLabel liveUpdateLbl = new JLabel("Live update");
	ImageIcon iconOn = new ImageIcon(MainFrame.class.getResource("/com/bas/versions/gui/img/on.png"));
	ImageIcon iconOff = new ImageIcon(MainFrame.class.getResource("/com/bas/versions/gui/img/off.png"));
	private final JPanel panel = new JPanel();
	private final JMenuItem toggleAutoCommitMit = new JMenuItem("Toggle Auto-commit");
	private final JMenu mnNewMenu_1 = new JMenu("?");
	private final JMenuItem mntmNewMenuItem_2 = new JMenuItem("Help");
	private final JMenuItem BasvWidgetMit = new JMenuItem("BASV Widget");

	/**
	 * Create the panel.
	 */
	public MainFrame() {

		mnTools.setEnabled(false);

		log("\n*******************************************************" + "\nStarted " + new Date() + "\n");

		config = new BasProperties();

		File confFile = new File("config.properties");
		if (confFile.exists()) {
			config.loadProperties(confFile);
			loadJfc.setCurrentDirectory(new File(config.getProp().getProperty("lastPath") + "\\Bas-CheckPoints"));
			newJfc.setCurrentDirectory(new File(config.getProp().getProperty("lastPath") + "\\Bas-CheckPoints"));
		}
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel north = new JPanel();
		getContentPane().add(north, BorderLayout.NORTH);
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

		newJfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		archiveJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		loadJfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		loadJfc.setFileFilter(new FileNameExtensionFilter("*.basv filter", "basv"));

		JPanel center = new JPanel();
		center.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
				"<html><Strong> <font size=\"4\">files</font> </Strong></html>", TitledBorder.LEADING, TitledBorder.TOP,
				null, new Color(0, 0, 0)));
		getContentPane().add(center, BorderLayout.CENTER);
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
					filterInTf.setText(filterInTf.getText().replaceAll("[/\\\\]", ""));
					if (filterInTf.getText().length() == 0) {
						filterInTf.setText(project.getFilterIn());
					}
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
					filterOutTf.setText(filterOutTf.getText().replaceAll("[/\\\\]", ""));
					JOptionPane.showMessageDialog(null, "Can't have \"/\" or \"\\\" in filters", "error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel_3.add(filterOutTf);
		filterOutTf.setColumns(25);

		filterOutTf.setEnabled(false);
		filterInTf.setEnabled(false);
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

		noFilterCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				noFilterCbAction();
			}

		});
		noFilterCb.setToolTipText("Disable filtering");

		panel_3.add(noFilterCb);
		liveUpdateLbl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		liveUpdateLbl.setHorizontalAlignment(SwingConstants.CENTER);
		liveUpdateLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				pauseLiveUpdateAction();
				switchLiveUpdateMit.setText(NightsWatcher.isPause() ? "Enable live update" : "Disable live update");
				liveUpdateLbl.setIcon(NightsWatcher.isPause() ? iconOff : iconOn);
			}
		});
		panel_3.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(liveUpdateLbl);
		liveUpdateLbl.setIcon(iconOn);
		liveUpdateLbl.setEnabled(false);

		panel4Files.setPreferredSize(new Dimension(300, 300));
		panel4Files.setBorder(new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
				"Files in project dir.", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel4Files.setLayout(new BorderLayout(0, 0));
		panel4FilteredFiles.setPreferredSize(new Dimension(300, 300));
		panel4FilteredFiles
				.setBorder(new TitledBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
						"Filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel4FilteredFiles.setLayout(new BorderLayout(0, 0));
		panel4ModFiles.setPreferredSize(new Dimension(300, 300));
		panel4ModFiles.setBorder(new TitledBorder(
				new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")),
				"New or modified filtered files", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
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
		getContentPane().add(south, BorderLayout.SOUTH);
		south.setLayout(new BorderLayout(0, 0));

		JPanel commitButtPnl = new JPanel();
		south.add(commitButtPnl, BorderLayout.CENTER);
		createChkptBut.setToolTipText("create a new checkpoint in the project");
		createChkptBut.setMnemonic('c');
		createChkptBut.setEnabled(false);
		createChkptBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				commitButAction();
			}
		});
		commitButtPnl.setLayout(new BoxLayout(commitButtPnl, BoxLayout.Y_AXIS));

		commitButtPnl.add(createChkptBut);
		FlowLayout flowLayout_4 = (FlowLayout) panel_9.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_9.setAlignmentX(Component.LEFT_ALIGNMENT);

		commitButtPnl.add(panel_9);
		autoCommiWaitTimeCb.setEnabled(false);
		autoCommiWaitTimeCb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				autoCommiWaitTimeCbAction();
			}
		});
		panel_9.add(autoCommiWaitTimeCb);
		autoCommiWaitTimeLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				autoCommiWaitTimeCb.setSelected(!autoCommiWaitTimeCb.isSelected());
				autoCommiWaitTimeCbAction();
			}
		});
		panel_9.add(autoCommiWaitTimeLbl);
		autoCommiWaitTimeTf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				checkForLong();
			}
		});

		autoCommiWaitTimeTf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkForLong();
			}
		});
		autoCommiWaitTimeTf.setText("20");
		panel_9.add(autoCommiWaitTimeTf);
		autoCommiWaitTimeTf.setColumns(4);

		JPanel msgPnl = new JPanel();
		FlowLayout fl_msgPnl = (FlowLayout) msgPnl.getLayout();
		fl_msgPnl.setAlignment(FlowLayout.RIGHT);
		south.add(msgPnl, BorderLayout.WEST);

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

		setJMenuBar(menuBar);
		mnNewMenu.setMnemonic('p');
		mnNewMenu.setHorizontalAlignment(SwingConstants.LEFT);

		menuBar.add(mnNewMenu);
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseNew();
			}
		});
		mntmNewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNewMenuItem.setHorizontalAlignment(SwingConstants.LEFT);

		mnNewMenu.add(mntmNewMenuItem);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadProject();
			}
		});
		mntmNewMenuItem_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mntmNewMenuItem_1.setHorizontalAlignment(SwingConstants.LEFT);

		mnNewMenu.add(mntmNewMenuItem_1);
		mnTools.setMnemonic('t');

		menuBar.add(mnTools);
		mntmCommit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmCommit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commitButAction();
			}
		});
		mntmCommit.setHorizontalAlignment(SwingConstants.LEFT);

		mnTools.add(mntmCommit);
		mntmRestore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				RestoreFrame rf = new RestoreFrame(project);
			}
		});
		mntmRestore.setHorizontalAlignment(SwingConstants.LEFT);

		mnTools.add(mntmRestore);
		mntmArchive.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mntmArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				archiveAction();
			}
		});
		mntmArchive.setHorizontalAlignment(SwingConstants.LEFT);

		mnTools.add(mntmArchive);

		mnTools.add(separator);
		switchLiveUpdateMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pauseLiveUpdateAction();
				switchLiveUpdateMit.setText(NightsWatcher.isPause() ? "Enable live update" : "Disable live update");
				liveUpdateLbl.setIcon(NightsWatcher.isPause() ? iconOff : iconOn);
			}
		});

		mnTools.add(switchLiveUpdateMit);
		toggleAutoCommitMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				autoCommiWaitTimeCb.setSelected(!autoCommiWaitTimeCb.isSelected());
				autoCommiWaitTimeCbAction();
			}
		});

		mnTools.add(toggleAutoCommitMit);
		BasvWidgetMit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BasvWidgetMitAction();
			}
		});

		mnTools.add(BasvWidgetMit);

		menuBar.add(mnNewMenu_1);
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				helpMitAction();
			}
		});
		mntmNewMenuItem_2.setHorizontalAlignment(SwingConstants.LEFT);

		mnNewMenu_1.add(mntmNewMenuItem_2);

		pack();
	}

	protected void BasvWidgetMitAction() {
		@SuppressWarnings("unused")
		BasvWidget widget = new BasvWidget(project);
	}

	protected void helpMitAction() {
		@SuppressWarnings("unused")
		HelpFrame helpFrame = new HelpFrame();

	}

	private void pauseLiveUpdateAction() {
		if (!NightsWatcher.isPause()) {
			NightsWatcher.setPause(true);
			return;
		}

		if (NightsWatcher.isPause()) {
			NightsWatcher.setPause(false);
			project.reScan();
			return;
		}

	}

	protected void checkForLong() {
		if (!autoCommiWaitTimeTf.getText().isEmpty()) {
			String text = autoCommiWaitTimeTf.getText();

			try {
				Long.parseLong(text);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "Enter integer number for wait time (Ex '20')", "error",
						JOptionPane.ERROR_MESSAGE);
				autoCommiWaitTimeTf.setText("20");
			}
		}

	}

	protected void autoCommiWaitTimeCbAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				project.setAutoCommit(autoCommiWaitTimeCb.isSelected());
				project.setWaitTime((Long.valueOf(autoCommiWaitTimeTf.getText())) * 60 * 1000);
				config.getProp().setProperty("waitTime",
						String.valueOf(Long.valueOf(autoCommiWaitTimeTf.getText()) * 60 * 1000));
				config.updateConf();
			}
		}).start();

	}

	protected void archiveAction() {

		final int result = archiveJFC.showOpenDialog(getParent());

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (result == JFileChooser.APPROVE_OPTION) {

					log("Started archiving project in folder : " + archiveJFC.getSelectedFile().getAbsolutePath() + " "
							+ new Date());

					project.getCheckPointStack().peekLast();
					ProjectRestorer pr = new ProjectRestorer(project, archiveJFC.getSelectedFile().toPath(),
							CheckPoint.getCheckPointId());
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
					project.setFilters(filterInTf.getText(), filterOutTf.getText());
				}
				project.reScan();
			}
		}).start();
	}

	protected void commitButAction() {

		log("Committed checkPoint " + new Date());

		new Thread(new Runnable() {

			@Override
			public void run() {
				int info = JOptionPane.OK_OPTION;
				if (msgTp.getText().length() == 0 || msgTp.getText().equals("Please insert message")) {
					info = JOptionPane.showConfirmDialog(null, "Warning", "Checkpoint message is empty",
							JOptionPane.OK_CANCEL_OPTION);
				}

				if (info == JOptionPane.OK_OPTION) {
					project.setVersMsg(msgTp.getText());
					project.commitCheckPoint(false);
					lastChkptIndicLbl.setText("Last checkpoint :");
					chkptDateIndicLbl.setText("created : ");
					msgTp.setText(MessageIncrementer.increment(msgTp.getText()));
				} else {
					msgTp.setText("Please insert message");
				}
			}
		}).start();

	}

	protected void chooseNew() {

		log("New Project created " + new Date());
		log("\n********************************************");
		final int result = newJfc.showOpenDialog(getParent());

		new Thread(new Runnable() {
			@Override
			public void run() {

				if (result == JFileChooser.APPROVE_OPTION) {

					if (project != null) {
						project.stopAutoCommit();
						watchThread.interrupt();
						CheckPoint.setCheckPointId(0);
						lastChkptLbl.setText("");
						chkptDateLbl.setText("");

					}

					Project newProject = new Project(Paths.get(newJfc.getSelectedFile().getAbsolutePath()));
					project = newProject;

					initPanel(project);
					project.reScan();
					project.addObserver(MainFrame.this);
					updateBut.setEnabled(true);
					config.getProp().setProperty("lastPath", project.getProjectPath().toFile().getAbsolutePath());
					project.setWaitTime(Long.valueOf(config.getProp().getProperty("waitTime")));
					config.updateConf();

					update(null, null);

				}
			}
		}).start();

	}

	public void loadProject() {

		log("Project Loaded " + new Date());
		log("\n********************************************");
		final int result = loadJfc.showOpenDialog(getParent());

		new Thread(new Runnable() {
			@Override
			public void run() {
				if (result == JFileChooser.APPROVE_OPTION) {

					project = new Project(new ProjectParser(loadJfc.getSelectedFile()).getDoc());
					initPanel(project);
					project.addObserver(MainFrame.this);
					updateBut.setEnabled(true);
					config.getProp().setProperty("lastPath", project.getProjectPath().toFile().getAbsolutePath());
					project.setWaitTime(Long.valueOf(config.getProp().getProperty("waitTime")));
					config.updateConf();
					noFilterCb.setSelected(false);
					filterInTf.setEnabled(true);
					filterOutTf.setEnabled(true);
					update(null, null);
				}
			}
		}).start();

	}

	public void initPanel(Project project) {

		this.project = project;
		watchThread = new Thread(new Runnable() {
			@Override
			public void run() {

				if (Thread.interrupted()) {
					System.out.println("WatchService interruted");
					return;
				}

				jon = new NightsWatcher(project);

			}
		});
		watchThread.start();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				projectLbl.setText("/" + project.getProjectPath().toFile().getName() + "/");
				projectDirLbl.setText("Created : " + project.getDateCreated());
				if (project.getCheckPointStack().peekLast() != null) {
					lastChkptIndicLbl.setText("Last checkpoint :");
					chkptDateIndicLbl.setText("created : ");
				}
				filterInTf.setText(project.getFilterIn());
				filterOutTf.setText(project.getFilterOut());
				panel4Files.add(new BasJTree(project.getProjectPath(), project.getListFile()));
				panel4FilteredFiles.add(new BasJTree(project.getProjectPath(), project.getListFilteredFile()));
				panel4ModFiles.add(new BasJTree(project.getProjectPath(), project.getListModFile()));
				if (project.getCheckPointStack().peekLast() != null) {
					lastChkptLbl.setText(project.getCheckPointStack().peekLast().toString().trim());
					chkptDateLbl.setText(project.getCheckPointStack().peekLast().getDateCreated().toString().trim());
				}

				createChkptBut.setEnabled(true);
				noFilterCb.setEnabled(true);
				autoCommiWaitTimeTf.setText(String.valueOf(project.getWaitTime() / 60000));
				mnTools.setEnabled(project != null ? true : false);
				liveUpdateLbl.setEnabled(true);
				autoCommiWaitTimeCb.setEnabled(true);
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
				panel4Files.add(new BasJTree(project.getProjectPath(), project.getListFile()));
				panel4FilteredFiles.add(new BasJTree(project.getProjectPath(), project.getListFilteredFile()));
				panel4ModFiles.add(new BasJTree(project.getProjectPath(), project.getListModFile()));
				if (project.getCheckPointStack().peekLast() != null) {
					lastChkptLbl.setText(project.getCheckPointStack().peekLast().toString().trim());
					chkptDateLbl.setText(project.getCheckPointStack().peekLast().getDateCreated().toString().trim());
				}
				autoCommiWaitTimeTf.setText(String.valueOf(project.getWaitTime() / 60000));
				revalidate();
				repaint();
			}
		});

	}

	/**
	 * 
	 */
	public void noFilterCbAction() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (noFilterCb.isSelected()) {
					project.setFilters("*", "");
					filterInTf.setEnabled(false);
					filterOutTf.setEnabled(false);
					project.reScan();
				}
				if (!noFilterCb.isSelected()) {
					project.setFilters(filterInTf.getText(), filterOutTf.getText());
					filterInTf.setEnabled(true);
					filterOutTf.setEnabled(true);
					project.reScan();
				}
			}
		}).start();
	}
}
