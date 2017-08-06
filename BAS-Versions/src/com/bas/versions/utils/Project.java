package com.bas.versions.utils;

import static com.bas.versions.utils.FileManager.saveFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Project extends Observable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6070843143570960833L;
	private static int projectId = 0;
	private int id;
	private Date dateCreated;
	private String filterIn;
	private String filterOut;
	private String chkptMsg;
	private Path projectPath;
	private Path workPath;
	private Queue<CheckPoint> checkpointStack;
	private Set<File> fileSet;
	private Set<File> modFileSet;
	private Set<File> filteredFileSet;

	public Project() {

		projectId++;
		this.id = projectId;
		this.dateCreated = new Date();
		this.filterIn = "";
		this.filterOut = "";
		this.chkptMsg = "No message";
		this.projectPath = null;
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-Versions");
		this.checkpointStack = new PriorityQueue<>();
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
		this.filterOut = "rush,rush,source,sources,BAS-Checkpoints";
		this.chkptMsg = "New project : " + projectPath.toFile().getName();
		this.projectPath = projectPath;
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints");
		this.checkpointStack = new LinkedList<>();
		this.fileSet = new FileList(this.projectPath).getResult();
		this.filteredFileSet = new FileList(this.fileSet, this.filterIn, this.filterOut, this.projectPath).getResult();
		this.modFileSet = new FileList(this.filteredFileSet, this.dateCreated).getResult();
		setChanged();
		notifyObservers();

	}

	public void commitCheckPoint() {
		File dateSave = new File(this.workPath + "\\data.basv");
		CheckPoint newVers = new CheckPoint(new Date(), this.projectPath, this.modFileSet, this.chkptMsg);
		newVers.writeFiles();
		saveFile(newVers.getDateCreated(), dateSave);
		checkpointStack.add(newVers);
		setChanged();
		notifyObservers();
	}
	

	/**
	 * updates sets of files
	 */
	public void updateSets(){	
		
		this.fileSet = new FileList(this.projectPath).getResult();
		this.filteredFileSet = new FileList(this.fileSet, this.filterIn, this.filterOut, this.projectPath).getResult();
		if (this.getCheckPointStack().peek() == null) {
			this.modFileSet = new FileList(this.filteredFileSet, this.dateCreated).getResult();
		} else {
			this.modFileSet = new FileList(this.filteredFileSet, this.getCheckPointStack().peek().getDateCreated()).getResult();
		}
		setChanged();
		notifyObservers();
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
	public Queue<CheckPoint> getCheckPointStack() {
		return checkpointStack;
	}

	/**
	 * @param versionStack
	 *            the versionStack to set
	 */
	public void setVersionStack(Queue<CheckPoint> versionStack) {
		this.checkpointStack = versionStack;
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
	 * @param versMsg the versMsg to set
	 */
	public void setVersMsg(String versMsg) {
		this.chkptMsg = versMsg;
	}

	// }}}

}
