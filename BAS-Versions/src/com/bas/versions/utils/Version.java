package com.bas.versions.utils;

import static com.bas.versions.utils.FileManager.changeFilePath;
import static com.bas.versions.utils.FileManager.copyFile2VersionPath;
import static com.bas.versions.utils.FileManager.writeTxtFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.bas.versions.gui.BasPgBarUpdtePair;
import com.bas.versions.gui.BasProgressBar;

public class Version extends Observable implements Comparable<Version>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1509582280653749478L;
	private static int versionId = 1;
	private int id;

	private Date dateCreated = new Date();
	private Set<File> projectFileList = new HashSet<>();
	private Set<File> versionFileList = new HashSet<>();
	private File[][] fileTab;
	private Path versionPath;
	private Path projectPath;
	private String versionMsg = "";
	private String formatId;
	private BasProgressBar pgBar;

	public Version() {
		this.dateCreated = null;
		this.versionPath = null;
		this.id = versionId;
	}

	public Version(Date date, Path projectPath, Set<File> fileModList, String msg) {

		this.id = versionId;
		this.formatId = String.format("%04d", this.id);
		this.dateCreated = date;
		this.projectPath = projectPath;
		this.versionPath = Paths.get(projectPath.toFile().getAbsolutePath() + "\\BAS-Versions" + "\\" + "Vers" + this.formatId + "["
				+ new SimpleDateFormat("yyy-MM-dd_HH-mm").format(dateCreated) + "]");
		this.versionMsg = msg;
		this.projectFileList = fileModList;
		this.fileTab = new File[2][fileModList.size()];
		this.versionFileList = createVersFileList(fileModList);

	}

	/**
	 * @param projectFileList
	 * @return
	 */
	public HashSet<File> createVersFileList(Set<File> projectFileList) {
		HashSet<File> versFileList = new HashSet<>();

		BasPgBarUpdtePair pair;

		pgBar = new BasProgressBar("Indexing Version " + formatId + " files", projectFileList.size());
		this.addObserver(pgBar);
		pgBar.setVisible(true);

		Iterator<File> it = projectFileList.iterator();
		int i = 0;
		while (it.hasNext()) {

			File file = it.next();
			File newFile = changeFilePath(file, this.projectPath, this.versionPath);
			versFileList.add(newFile);
			this.fileTab[0][i] = file;
			this.fileTab[1][i] = newFile;
			pair = new BasPgBarUpdtePair("Indexed file : " + file.getName(), i + 1);
			i++;
			setChanged();
			notifyObservers(pair);
		}
		i = 0;
		pgBar.dispose();
		return versFileList;
	}

	/**
	 * writes Version files to disk
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("static-access")
	public void writeFiles() {

		BasPgBarUpdtePair pair;

		pgBar = new BasProgressBar("Copying Files toVersion" + formatId, this.fileTab.length + 3);
		this.addObserver(pgBar);
		pgBar.setVisible(true);

		for (int i = 0; i < this.fileTab[0].length; i++) {
			boolean success = false;
			try {
				success = copyFile2VersionPath(this.fileTab[0][i], this.fileTab[1][i]);
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
				pair = new BasPgBarUpdtePair("Copied file : " + this.fileTab[1][i].getName(), i + 1);
				setChanged();
				notifyObservers(pair);
			}
		}
		boolean success = false;
		String title = "Version " + formatId + " Message";
		try {
			success = writeTxtFile(versionMsg, title, versionPath);
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
			pair = new BasPgBarUpdtePair("Copied file : " + title + ".txt", this.fileTab.length + 3);
			setChanged();
			notifyObservers(pair);
		}
		pgBar.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		this.formatId = String.format("%04d", this.id);

		String msg = this.versionMsg;
		if (this.versionMsg.length() > 31) {
			msg = this.versionMsg.substring(0, 30) + "...";
		}
		String str = "Version " + formatId + "[" + msg + "]";
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
		result = prime * result + ((versionFileList == null) ? 0 : versionFileList.hashCode());
		result = prime * result + ((versionMsg == null) ? 0 : versionMsg.hashCode());
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
		Version other = (Version) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (id != other.id)
			return false;
		if (versionFileList == null) {
			if (other.versionFileList != null)
				return false;
		} else if (!versionFileList.equals(other.versionFileList))
			return false;
		if (versionMsg == null) {
			if (other.versionMsg != null)
				return false;
		} else if (!versionMsg.equals(other.versionMsg))
			return false;
		return true;
	}

	@Override
	public int compareTo(Version v) {
		return this.dateCreated.compareTo(v.dateCreated);
	}

	// {{{ getters and setters
	/**
	 * @return the versionId
	 */
	public static int getVersionId() {
		return versionId;
	}

	/**
	 * @param versionId
	 *            the versionId to set
	 */
	public static void setVersionId(int versionId) {
		Version.versionId = versionId;
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
	 * @return the versionFileList
	 */
	public Set<File> getVersionFileList() {
		return projectFileList;
	}

	/**
	 * @param versionFileList
	 *            the versionFileList to set
	 */
	public void setVersionFileList(Set<File> versionFileList) {
		this.projectFileList = versionFileList;
	}

	/**
	 * @return the versionFileModList
	 */
	public Set<File> getVersionFileModList() {
		return versionFileList;
	}

	/**
	 * @param versionFileModList
	 *            the versionFileModList to set
	 */
	public void setVersionFileModList(Set<File> versionFileModList) {
		this.versionFileList = versionFileModList;
	}

	/**
	 * @return the versionPath
	 */
	public Path getVersionPath() {
		return versionPath;
	}

	/**
	 * @param versionPath
	 *            the versionPath to set
	 */
	public void setVersionPath(Path versionPath) {
		this.versionPath = versionPath;
	}

	/**
	 * @return the versionMsg
	 */
	public String getVersionMsg() {
		return versionMsg;
	}

	/**
	 * @param versionMsg
	 *            the versionMsg to set
	 */
	public void setVersionMsg(String versionMsg) {
		this.versionMsg = versionMsg;
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
