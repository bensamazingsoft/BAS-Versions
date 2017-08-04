package com.bas.versions.utils;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import static com.bas.versions.utils.FileManager.*;

public class Project extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6070843143570960833L;
	private static int projectId = 0;
	private int id;
	private Date dateCreated;
	private String filterIn;
	private String filterOut;
	private String versMsg;
	private Path projectPath;
	private Path workPath;
	private Queue<Version> versionStack;
	private Set<File> fileSet;
	private Set<File> modFileSet;
	private Set<File> filteredFileSet;

	public Project() {

		projectId++;
		this.id = projectId;
		this.dateCreated = new Date();
		this.filterIn = "";
		this.filterOut = "";
		this.versMsg = "No message";
		this.projectPath = null;
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-Versions");
		this.versionStack = new PriorityQueue<>();
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
		this.filterOut = "rush, rush, source, sources, BAS-Versions";
		this.versMsg = "New project : " + projectPath.toFile().getName();
		this.projectPath = projectPath;
		this.workPath = Paths.get(this.projectPath.toFile().getAbsolutePath() + "\\BAS-Versions");
		this.versionStack = new PriorityQueue<>();
		this.fileSet = new FileList(this.projectPath).getResult();
		this.filteredFileSet = new FileList(this.fileSet, this.filterIn, this.filterOut, this.projectPath).getResult();
		this.modFileSet = new FileList(this.filteredFileSet, this.dateCreated).getResult();
		setChanged();
		notifyObservers();

	}

	public void commitVersion() {
		File dateSave = new File(this.workPath + "\\data.basv");
		Version newVers = new Version(new Date(), this.projectPath, this.modFileSet, this.versMsg);
		newVers.writeFiles();
		saveFile(newVers.getDateCreated(), dateSave);
		versionStack.add(newVers);
		setChanged();
		notifyObservers();
	}
	

	/**
	 * updates sets of files
	 */
	public void updateSets(){	
		
		this.fileSet = new FileList(this.projectPath).getResult();
		this.filteredFileSet = new FileList(this.fileSet, this.filterIn, this.filterOut, this.projectPath).getResult();
		if (this.getVersionStack().peek() == null) {
			this.modFileSet = new FileList(this.filteredFileSet, this.dateCreated).getResult();
		} else {
			this.modFileSet = new FileList(this.filteredFileSet, this.getVersionStack().peek().getDateCreated()).getResult();
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
	public Queue<Version> getVersionStack() {
		return versionStack;
	}

	/**
	 * @param versionStack
	 *            the versionStack to set
	 */
	public void setVersionStack(Queue<Version> versionStack) {
		this.versionStack = versionStack;
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
		return versMsg;
	}

	/**
	 * @param versMsg the versMsg to set
	 */
	public void setVersMsg(String versMsg) {
		this.versMsg = versMsg;
	}

	// }}}

}
