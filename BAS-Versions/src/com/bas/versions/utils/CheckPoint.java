package com.bas.versions.utils;

import static com.bas.versions.utils.FileManager.changeFilePath;
import static com.bas.versions.utils.FileManager.copyFile2CheckPointPath;
import static com.bas.versions.utils.FileManager.writeTxtFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.bas.versions.gui.BasPgBarUpdtePair;
import com.bas.versions.gui.BasProgressBar;
import com.bas.versions.xml.XmlWriter;

public class CheckPoint extends Observable implements Comparable<CheckPoint> {

	private static int chkptId = 1;
	private int id;

	private Date dateCreated = new Date();
	private Set<File> chkptFileList = new HashSet<>();
	private File[][] fileTab;
	private Path chkptPath;
	private Path projectPath;
	private String chkptMsg = "";
	private String formatId;
	private BasProgressBar pgBar;

	public CheckPoint() {
		dateCreated = null;
		chkptPath = null;
		id = chkptId;
		chkptId++;
	}

	public CheckPoint(Date date, Path projectPath, Set<File> fileList, String msg) {

		id = chkptId;
		formatId = String.format("%04d", id);
		dateCreated = date;
		this.projectPath = projectPath;
		chkptPath = Paths.get(projectPath.toFile().getAbsolutePath() + "\\BAS-CheckPoints" + "\\" + "Checkpoint"
				+ formatId + "[" + new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(dateCreated) + "]");
		chkptMsg = msg;
		fileTab = new File[2][fileList.size()];
		chkptFileList = mkChkptFileList(fileList);
		chkptId++;

	}

	public CheckPoint(Document doc){
		
		Element rootElt = doc.getDocumentElement();
		id = Integer.valueOf(rootElt.getAttribute("id"));
		formatId = String.format("%04d", id);
		try {
			dateCreated = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00'").parse(rootElt.getAttribute("date"));
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		projectPath = Paths.get(rootElt.getAttribute("projectPath"));
		chkptPath = Paths.get(rootElt.getAttribute("checkpointPath"));
		
		NodeList msgList = doc.getElementsByTagName("message");
		chkptMsg = msgList.item(0).getTextContent();
		
		NodeList fileList = doc.getElementsByTagName("file");
		int nbFile = fileList.getLength();
		for (int i = 0; i<nbFile; i++){
			chkptFileList.add(new File(fileList.item(i).getTextContent()));
		}

	
	}

	/**
	 * @param projectFileList
	 * @return
	 */
	public HashSet<File> mkChkptFileList(Set<File> projectFileList) {
		HashSet<File> chkptFileList = new HashSet<>();

		BasPgBarUpdtePair pair;

		pgBar = new BasProgressBar("Indexing CheckPoint " + formatId + " files", projectFileList.size());
		addObserver(pgBar);
		pgBar.setVisible(true);

		Iterator<File> it = projectFileList.iterator();
		int i = 0;
		while (it.hasNext()) {

			File file = it.next();
			File newFile = changeFilePath(file, projectPath, chkptPath);
			chkptFileList.add(newFile);
			fileTab[0][i] = file;
			fileTab[1][i] = newFile;
			pair = new BasPgBarUpdtePair("Indexed file : " + file.getName(), i + 1);
			i++;
			setChanged();
			notifyObservers(pair);
		}
		i = 0;
		pgBar.dispose();
		return chkptFileList;
	}

	/**
	 * writes CheckPoint files to disk
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public void writeFiles() {

		File newFolder = chkptPath.toFile();
		newFolder.mkdirs();

		BasPgBarUpdtePair pair;

		pgBar = new BasProgressBar("Copying Files to Checkpoint" + formatId, fileTab[0].length + 3);
		addObserver(pgBar);
		pgBar.setVisible(true);

		for (int i = 0; i < fileTab[0].length; i++) {
			boolean success = false;
			try {
				success = copyFile2CheckPointPath(fileTab[0][i], fileTab[1][i]);
			} catch (IOException e) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane.showMessageDialog(null, "Error", "Can't read file", JOptionPane.ERROR_MESSAGE);
					}
				});
			}
			if (!success) {
				final String errorMsg = "File copy Failed on file: \n" + fileTab[0][i].getName();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						JOptionPane error = new JOptionPane();
						error.showMessageDialog(null, "Error", errorMsg, JOptionPane.ERROR_MESSAGE);
					}
				});
			} else {
				pair = new BasPgBarUpdtePair("Copied file : " + fileTab[1][i].getName(), i + 1);
				setChanged();
				notifyObservers(pair);
			}
		}
		boolean success = false;
		String title = "CheckPoint " + formatId + " Message";
		try {
			success = writeTxtFile(chkptMsg, title, chkptPath);
		} catch (IOException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, "Error", "Can't read file", JOptionPane.ERROR_MESSAGE);
				}
			});
		}
		if (!success) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane error = new JOptionPane();
					error.showMessageDialog(null, "Error", "File copy Failed on Message.txt file.",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		} else {
			pair = new BasPgBarUpdtePair("Copied file : " + title + ".txt", fileTab.length + 3);
			setChanged();
			notifyObservers(pair);
		}
		pgBar.dispose();
		
		XmlWriter xmlWrt = new XmlWriter();
		xmlWrt.writeChckPtXml(this);
	}

	/**
	 * checks if Checkpoint files are on disk
	 * @return true if they are
	 */
	public boolean checkFiles(){	
		boolean ok = true;
		for (File file : chkptFileList){		
			ok=(file.exists()) ? true : false;
		}
			return ok;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		formatId = String.format("%04d", id);

		String msg = chkptMsg;
		if (chkptMsg.length() > 31) {
			msg = chkptMsg.replace(System.getProperty("line.separator"), "").substring(0, 30) + "...";
		}
		String str = "CheckPoint " + formatId + "[" + msg.replace(System.getProperty("line.separator"), " ") + "]";
		return str;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + id;
		result = prime * result + ((chkptFileList == null) ? 0 : chkptFileList.hashCode());
		result = prime * result + ((chkptMsg == null) ? 0 : chkptMsg.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckPoint other = (CheckPoint) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (id != other.id)
			return false;
		if (chkptFileList == null) {
			if (other.chkptFileList != null)
				return false;
		} else if (!chkptFileList.equals(other.chkptFileList))
			return false;
		if (chkptMsg == null) {
			if (other.chkptMsg != null)
				return false;
		} else if (!chkptMsg.equals(other.chkptMsg))
			return false;
		return true;
	}

	@Override
	public int compareTo(CheckPoint v) {
		return dateCreated.compareTo(v.dateCreated);
	}

	// {{{ getters and setters
	/**
	 * @return the chkptId
	 */
	public static int getCheckPointId() {
		return chkptId;
	}

	/**
	 * @param chkptId
	 *            the chkptId to set
	 */
	public static void setCheckPointId(int versionId) {
		CheckPoint.chkptId = versionId;
	}

	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#clearChanged()
	 */
	@Override
	protected synchronized void clearChanged() {
		super.clearChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#countObservers()
	 */
	@Override
	public synchronized int countObservers() {
		return super.countObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	@Override
	public synchronized void deleteObserver(Observer o) {
		super.deleteObserver(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#deleteObservers()
	 */
	@Override
	public synchronized void deleteObservers() {
		super.deleteObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#hasChanged()
	 */
	@Override
	public synchronized boolean hasChanged() {
		return super.hasChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers() {
		super.notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	@Override
	public void notifyObservers(Object arg) {
		super.notifyObservers(arg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#setChanged()
	 */
	@Override
	protected synchronized void setChanged() {
		super.setChanged();
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
	 * @return the versionFileModList
	 */
	public Set<File> getChckPtFileList() {
		return chkptFileList;
	}

	/**
	 * @param versionFileModList
	 *            the versionFileModList to set
	 */
	public void setVersionFileModList(Set<File> versionFileModList) {
		chkptFileList = versionFileModList;
	}

	/**
	 * @return the chkptPath
	 */
	public Path getChckPtPath() {
		return chkptPath;
	}

	/**
	 * @param chkptPath
	 *            the chkptPath to set
	 */
	public void setVersionPath(Path versionPath) {
		chkptPath = versionPath;
	}

	/**
	 * @return the chkptMsg
	 */
	public String getChkPtMsg() {
		return chkptMsg;
	}

	/**
	 * @param chkptMsg
	 *            the chkptMsg to set
	 */
	public void setVersionMsg(String versionMsg) {
		chkptMsg = versionMsg;
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
	// }}}

}
