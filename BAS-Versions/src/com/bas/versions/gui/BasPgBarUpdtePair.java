package com.bas.versions.gui;

/**
 * Pair of arguments for the BasProgressBar updates
 * @see BasProgressBar#update(java.awt.Graphics)
 * @author ben
 *
 */
public class BasPgBarUpdtePair {
	
	private String str;
	private int i;
	
	public BasPgBarUpdtePair(String str, int i){
		this.str = str;
		this.i = i;
		
	}

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}

	/**
	 * @param str the str to set
	 */
	public void setStr(String str) {
		this.str = str;
	}

	/**
	 * @return the i
	 */
	public int getI() {
		return i;
	}

	/**
	 * @param i the i to set
	 */
	public void setI(int i) {
		this.i = i;
	}

}
