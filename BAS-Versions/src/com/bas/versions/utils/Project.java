package com.bas.versions.utils;

import static com.bas.versions.utils.LogGenerator.log;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.bas.versions.xml.ParseErrorHandler;
import com.bas.versions.xml.XmlWriter;

public class Project extends Observable {

	private static int projectId = 0;
	private int id;
	private Date dateCreated;
	private String filterIn;
	private String filterOut;
	private String chkptMsg;
	private Path projectPath;
	private Path workPath;
	private Deque<CheckPoint> checkpointStack;
	private Set<File> fileSet;
	private Set<File> modFileSet;
	private Set<File> filteredFileSet;
	private Set<File> filesInProjectFolder;
	private Set<File> nonCommittedFiles;
	Set<File> nonCommittedAndFilteredFiles;
	Set<File> filesFromXml = new HashSet<File>();
	private volatile boolean autoCommit;
	protected volatile long waitTime;
	Thread autoCommitThread;

	public Project() {

		projectId++;
		id = projectId;
		dateCreated = new Date();
		filterIn = "";
		filterOut = "";
		chkptMsg = "No message";
		projectPath = null;
		workPath = Paths.get(projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		checkpointStack = new LinkedList<>();
		fileSet = new HashSet<File>();
		modFileSet = new HashSet<File>();
		filteredFileSet = new HashSet<File>();
		setChanged();
		notifyObservers();
	}

	public Project(Path projectPath) {

		nonCommittedFiles = new HashSet<File>();
		nonCommittedAndFilteredFiles = new HashSet<File>();

		projectId++;
		id = projectId;
		dateCreated = new Date();
		filterIn = "*";
		filterOut = "";
		chkptMsg = "New project : " + projectPath.toFile().getName();
		this.projectPath = projectPath;
		workPath = Paths.get(projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		workPath.toFile().mkdirs();
		checkpointStack = new LinkedList<>();
		fileSet = new FileList(projectPath).getResult();
		filteredFileSet = new FileList(fileSet, filterIn + ",", "BAS-CheckPoints," + filterOut, projectPath)
				.getResult();
		modFileSet = filteredFileSet;
		filesInProjectFolder = getListFile();
		startAutoCommit();
		setChanged();
		notifyObservers();

	}

	/**
	 * @param doc
	 *            Document created with
	 *            {@link com.bas.versions.xml.ProjectParser}
	 */
	public Project(Document doc) {

		nonCommittedFiles = new HashSet<File>();
		nonCommittedAndFilteredFiles = new HashSet<File>();

		id = projectId;
		dateCreated = new Date();
		filterIn = "";
		filterOut = "";
		chkptMsg = "No message";
		projectPath = null;
		checkpointStack = new LinkedList<>();
		fileSet = new HashSet<File>();
		modFileSet = new HashSet<File>();
		filteredFileSet = new HashSet<File>();

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sFactory
					.newSchema(getClass().getClassLoader().getResource("com/bas/versions/xml/CheckPoint.xsd"));
			factory.setSchema(schema);
			ErrorHandler errHandler = new ParseErrorHandler();
			builder.setErrorHandler(errHandler);

			Element rootElt = doc.getDocumentElement();
			id = Integer.valueOf(rootElt.getAttribute("pr_id"));
			projectId = id;
			try {
				dateCreated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00'").parse(rootElt.getAttribute("pr_date"));
			} catch (ParseException e) {
				SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(),
						"error in Project constructor", JOptionPane.ERROR_MESSAGE));
				e.printStackTrace();
			}
			projectPath = Paths.get(rootElt.getAttribute("projectPath"));

			filterIn = rootElt.getElementsByTagName("filterIn").item(0).getTextContent();
			filterOut = rootElt.getElementsByTagName("filterOut").item(0).getTextContent();

			NodeList stack = rootElt.getElementsByTagName("checkpoint");
			int nbChkpt = stack.getLength();

			for (int i = 0; i < nbChkpt; i++) {

				Element cpElt = (Element) stack.item(i);
				Document tempDoc = builder.newDocument();
				tempDoc.appendChild(tempDoc.importNode(cpElt, true));
				CheckPoint cp = new CheckPoint(tempDoc);
				getCheckPointStack().add(cp);
			}

			NodeList state = rootElt.getElementsByTagName("stateFile");
			int nbStateFile = state.getLength();
			for (int j = 0; j < nbStateFile; j++) {
				filesFromXml.add(new File(state.item(j).getTextContent()));
			}

		} catch (NumberFormatException | DOMException | ParserConfigurationException | SAXException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(),
					"error in Project constructor", JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		}
		CheckPoint.setCheckPointId(getCheckPointStack().peekLast().getId() + 1);
		workPath = Paths.get(projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		filesInProjectFolder = new FileList(projectPath).getResult();
		nonCommittedFiles = filesInProjectFolder;
		nonCommittedFiles.removeAll(filesFromXml);
		updateSets();
		startAutoCommit();

		setChanged();
		notifyObservers();
	}

	/**
	 * Commits new CheckPoint to the workPath
	 * 
	 */
	public void commitCheckPoint(boolean auto) {
		CheckPoint newCheckPoint;

		newCheckPoint = auto ? new CheckPoint(new Date(), projectPath, modFileSet, "Automatic commit")
				: new CheckPoint(new Date(), projectPath, modFileSet, chkptMsg);
		newCheckPoint.writeFiles();
		checkpointStack.add(newCheckPoint);
		filesInProjectFolder = new FileList(projectPath).getResult();
		filesFromXml = filesInProjectFolder;
		filesFromXml.removeIf(file -> nonCommittedFiles.contains(file) && !modFileSet.contains(file));
		nonCommittedFiles.clear();
		nonCommittedAndFilteredFiles.clear();
		XmlWriter xw = new XmlWriter();
		xw.WriteProjectXml(this);
		updateSets();
		setChanged();
		notifyObservers();
	}

	/**
	 * updates sets of files
	 */
	public void updateSets() {

		System.err.println(SwingUtilities.isEventDispatchThread());
		fileSet = new FileList(projectPath).getResult();
		filteredFileSet = new FileList(fileSet, filterIn + ",", "BAS-CheckPoints," + filterOut, projectPath)
				.getResult();
		if (getCheckPointStack().peekLast() == null) {
			modFileSet = filteredFileSet;
		} else {
			modFileSet = new FileList(filteredFileSet, getCheckPointStack().peekLast().getDateCreated()).getResult();
			if (!nonCommittedFiles.isEmpty()) {
				nonCommittedAndFilteredFiles = new FileList(nonCommittedFiles, filterIn + ",",
						"BAS-CheckPoints," + filterOut, projectPath).getResult();
			}
			if (!nonCommittedAndFilteredFiles.isEmpty()) {
				nonCommittedAndFilteredFiles.forEach(modFileSet::add);
			}
		}
	}

	/**
	 * Rescans the project folder to acknowledge changes
	 */
	public void reScan() {
		filesInProjectFolder = new FileList(projectPath).getResult();
		nonCommittedFiles = filesInProjectFolder;
		nonCommittedFiles.removeAll(filesFromXml);
		updateSets();
		setChanged();
		notifyObservers();
	}

	public void startAutoCommit() {

		autoCommitThread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.err.println("starting auto commit");
				while (true) {

					try {
						Thread.sleep(waitTime);
					} catch (InterruptedException e) {
						log(e.getMessage());
						e.printStackTrace();
						return;
					}
					if (isAutoCommit() && !modFileSet.isEmpty()) {
						commitCheckPoint(true);
						log("Performing AutoCommit " + new Date());
					}
					autoCommit = isAutoCommit();
				}

			}
		});

		autoCommitThread.start();

	}

	// {{{ Getters/Setters

	/**
	 * @return the projectId
	 */
	public static int getProjectId() {
		return projectId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		id = this.id;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return this.dateCreated;
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		dateCreated = this.dateCreated;
	}

	/**
	 * @return the filterIn
	 */
	public String getFilterIn() {
		return this.filterIn;
	}

	/**
	 * @param filterIn
	 *            the filterIn to set
	 */
	public void setFilterIn(String filterIn) {
		filterIn = this.filterIn;
		updateSets();

	}

	/**
	 * @return the filterOut
	 */
	public String getFilterOut() {
		return this.filterOut;
	}

	/**
	 * @param filterOut
	 *            the filterOut to set
	 */
	public void setFilterOut(String filterOut) {
		filterOut = this.filterOut;
		updateSets();

	}

	public void setFilters(String filterIn, String filterOut) {
		this.filterIn = filterIn;
		this.filterOut = filterOut;
	}

	/**
	 * @return the projectPath
	 */
	public Path getProjectPath() {
		return projectPath;
	}

	/**
	 * @param projectPath
	 *            the projectPath to set
	 */
	public void setProjectPath(Path projectPath) {
		projectPath = this.projectPath;
	}

	/**
	 * @return the versionPath
	 */
	public Path getWorkPath() {
		return workPath;
	}

	/**
	 * @param versionPath
	 *            the versionPath to set
	 */
	public void setWorkPath(Path versionPath) {
		workPath = versionPath;
	}

	/**
	 * @return the versionStack
	 */
	public Deque<CheckPoint> getCheckPointStack() {
		return checkpointStack;
	}

	/**
	 * @return the listFile
	 */
	public Set<File> getListFile() {
		return fileSet;
	}

	/**
	 * @param listFile
	 *            the listFile to set
	 */
	public void setListFile(Set<File> listFile) {
		fileSet = listFile;
	}

	/**
	 * @return the listModFile
	 */
	public Set<File> getListModFile() {
		return modFileSet;
	}

	/**
	 * @param listModFile
	 *            the listModFile to set
	 */
	public void setListModFile(Set<File> listModFile) {
		modFileSet = listModFile;
	}

	/**
	 * @return the listFilteredFile
	 */
	public Set<File> getListFilteredFile() {
		return filteredFileSet;
	}

	/**
	 * @param listFilteredFile
	 *            the listFilteredFile to set
	 */
	public void setListFilteredFile(Set<File> listFilteredFile) {
		filteredFileSet = listFilteredFile;
	}

	/**
	 * @return the versMsg
	 */
	public String getVersMsg() {
		return chkptMsg;
	}

	/**
	 * @param versMsg
	 *            the versMsg to set
	 */
	public void setVersMsg(String versMsg) {
		chkptMsg = versMsg;
	}

	/**
	 * @return the filesInProjectFolder
	 */
	public Set<File> getFilesInProjectFolder() {
		return filesInProjectFolder;
	}

	/**
	 * @param filesInProjectFolder
	 *            the filesInProjectFolder to set
	 */
	public void setFilesInProjectFolder(Set<File> state) {
		state = this.filesInProjectFolder;
	}

	/**
	 * @return the nonCommittedFiles
	 */
	public Set<File> getNonCommittedFiles() {
		return this.nonCommittedFiles;
	}

	/**
	 * @param nonCommittedFiles
	 *            the nonCommittedFiles to set
	 */
	public void setNonCommittedFiles(Set<File> newFiles) {
		newFiles = this.nonCommittedFiles;
	}

	/**
	 * @return the filesFromXml
	 */
	public Set<File> getFilesFromXml() {
		return this.filesFromXml;
	}

	/**
	 * @param filesFromXml
	 *            the filesFromXml to set
	 */
	public void setFilesFromXml(Set<File> tempSet) {
		this.filesFromXml = tempSet;
	}

	/**
	 * @return the autoCommit
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * @param autoCommit
	 *            the autoCommit to set
	 */
	public void setAutoCommit(boolean autoCommit) {
		System.err.println("autocommit set to " + autoCommit);
		this.autoCommit = autoCommit;
	}

	/**
	 * @return the waitTime
	 */
	public long getWaitTime() {
		return waitTime;
	}

	/**
	 * @param waitTime
	 *            the waitTime to set
	 */
	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	public void stopAutoCommit() {
		autoCommitThread.interrupt();

	}

	// }}}

}
