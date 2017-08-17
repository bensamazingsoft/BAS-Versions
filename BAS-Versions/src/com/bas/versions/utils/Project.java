package com.bas.versions.utils;

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
	private Boolean filter = true;

	public Project() {

		projectId++;
		this.id = projectId;
		this.dateCreated = new Date();
		this.filterIn = "";
		this.filterOut = "";
		this.chkptMsg = "No message";
		this.projectPath = null;
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		this.checkpointStack = new LinkedList<>();
		this.fileSet = new HashSet<File>();
		this.modFileSet = new HashSet<File>();
		this.filteredFileSet = new HashSet<File>();
		setChanged();
		notifyObservers();
	}

	public Project(Path projectPath) {

		projectId++;
		this.id = projectId;
		this.dateCreated = new Date();
		this.filterIn = ".aep,.prpj,.psd,.ai,.txt,.srt";
		this.filterOut = "";
		this.chkptMsg = "New project : " + projectPath.toFile().getName();
		this.projectPath = projectPath;
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		this.workPath.toFile().mkdirs();
		this.checkpointStack = new LinkedList<>();
		this.fileSet = new FileList(this.projectPath).getResult();
		this.filteredFileSet = new FileList(this.fileSet, this.filterIn + ",", "BAS-CheckPoints," + this.filterOut,
				this.projectPath).getResult();
		this.modFileSet = this.filteredFileSet;
		setChanged();
		notifyObservers();

	}

	public Project(Document doc) {
		
		this.id = projectId;
		this.dateCreated = new Date();
		this.filterIn = "";
		this.filterOut = "";
		this.chkptMsg = "No message";
		this.projectPath = null;
		this.checkpointStack = new LinkedList<>();
		this.fileSet = new HashSet<File>();
		this.modFileSet = new HashSet<File>();
		this.filteredFileSet = new HashSet<File>();

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
			this.id = Integer.valueOf(rootElt.getAttribute("pr_id"));
			projectId = this.id;
			try {
				this.dateCreated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00'")
						.parse(rootElt.getAttribute("pr_date"));
			} catch (ParseException e) {
				SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(),
						"error in Project constructor", JOptionPane.ERROR_MESSAGE));
				e.printStackTrace();
			}
			this.projectPath = Paths.get(rootElt.getAttribute("projectPath"));

			this.filterIn = rootElt.getElementsByTagName("filterIn").item(0).getTextContent();
			this.filterOut = rootElt.getElementsByTagName("filterOut").item(0).getTextContent();

			NodeList stack = rootElt.getElementsByTagName("checkpoint");
			int nbChkpt = stack.getLength();
			
			for (int i = 0; i < nbChkpt; i++) {

				Element cpElt = (Element) stack.item(i);
				Document tempDoc = builder.newDocument();		
				tempDoc.appendChild(tempDoc.importNode(cpElt, true));
				CheckPoint cp = new CheckPoint(tempDoc);
				this.getCheckPointStack().add(cp);
			}
		} catch (NumberFormatException | DOMException | ParserConfigurationException | SAXException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(),
					"error in Project constructor", JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		}
		CheckPoint.setVersionId(this.getCheckPointStack().peekLast().getId()+1);
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		this.updateSets();
		this.setChanged();
		this.notifyObservers();
	}

	public void commitCheckPoint() {

		CheckPoint newVers = new CheckPoint(new Date(), this.projectPath, this.modFileSet, this.chkptMsg);
		newVers.writeFiles();
		this.checkpointStack.add(newVers);
		XmlWriter xw = new XmlWriter();
		xw.WriteProjectXml(this);
		setChanged();
		notifyObservers();
	}

	/**
	 * updates sets of files
	 */
	public void updateSets() {

		this.fileSet = new FileList(this.projectPath).getResult();
		if (this.filter){
		this.filteredFileSet = new FileList(this.fileSet, this.filterIn + ",", "BAS-CheckPoints," + this.filterOut,
				this.projectPath).getResult();
		} else {
			this.filteredFileSet = new FileList(this.fileSet, "*", "BAS-CheckPoints," + this.filterOut,
				this.projectPath).getResult();
		}
		if (this.getCheckPointStack().peekLast() == null) {
			this.modFileSet = this.filteredFileSet;
		} else {
			this.modFileSet = new FileList(this.filteredFileSet, this.getCheckPointStack().peekLast().getDateCreated())
					.getResult();
		}
	}

	// {{{ Getters/Setters

	/**
	 * @return the projectId
	 */
	public static int getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId
	 *            the projectId to set
	 */
	public static void setProjectId(int projectId) {
		Project.projectId = projectId;
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
		this.id = id;
	}

	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}

	/**
	 * @param dateCreated
	 *            the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	/**
	 * @return the filterIn
	 */
	public String getFilterIn() {
		return filterIn;
	}

	/**
	 * @param filterIn
	 *            the filterIn to set
	 */
	public void setFilterIn(String filterIn) {
		this.filterIn = filterIn;
	}

	/**
	 * @return the filterOut
	 */
	public String getFilterOut() {
		return filterOut;
	}

	/**
	 * @param filterOut
	 *            the filterOut to set
	 */
	public void setFilterOut(String filterOut) {
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
		this.projectPath = projectPath;
	}

	/**
	 * @return the versionPath
	 */
	public Path getVersionPath() {
		return workPath;
	}

	/**
	 * @param versionPath
	 *            the versionPath to set
	 */
	public void setVersionPath(Path versionPath) {
		this.workPath = versionPath;
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
		this.fileSet = listFile;
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
		this.modFileSet = listModFile;
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
		this.filteredFileSet = listFilteredFile;
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
		this.chkptMsg = versMsg;
	}

	/**
	 * @return the filter
	 */
	public Boolean getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Boolean filter) {
		this.filter = filter;
	}

	// }}}

}
