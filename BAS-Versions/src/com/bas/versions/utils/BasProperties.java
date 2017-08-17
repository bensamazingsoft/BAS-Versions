package com.bas.versions.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class BasProperties {

	private Properties defaultProp = new Properties();
	private Properties prop = new Properties();
	private InputStream input = null;
	private OutputStream output = null;
	private Path projectPath;

	
	
	public BasProperties(){
		
		initDefaultProperties();
		
	}
	
	public BasProperties(Path path) {

		initDefaultProperties();
		this.projectPath = path;
		this.prop.setProperty("projectPath", path.toFile().getAbsolutePath());
		
	}

	/**
	 * 
	 */
	private void initDefaultProperties() {
		this.input = getClass().getClassLoader().getResourceAsStream("com/bas/versions/utils/Default.properties");

		try {
			defaultProp.load(input);
		} catch (final IOException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error in properties",
					JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		}finally {
			if (input != null) {
				try {
					this.input.close();
				} catch (IOException e) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE));
					e.printStackTrace();
				}
			}

		}

		prop = defaultProp;
	}

	public void updateProperties() {

		try {
			output = new FileOutputStream(new File(this.projectPath.toFile() + "\\BAS-CheckPoints\\config.properties"));
		} catch (FileNotFoundException e) {
			SwingUtilities.invokeLater(
					() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		}

		try {
			prop.store(output, null);
		} catch (IOException e) {
			SwingUtilities.invokeLater(
					() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE));
					e.printStackTrace();
				}
			}

		}
	}
	
	public void loadProperties(File file){
		
		try {
			this.input = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e1.getMessage(), "error in properties",
					JOptionPane.ERROR_MESSAGE));
			e1.printStackTrace();
		}
		
		try {
			this.prop.load(input);
		} catch (final IOException e) {
			SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error in properties",
					JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		}finally {
			if (input != null) {
				try {
					this.input.close();
				} catch (IOException e) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE));
					e.printStackTrace();
				}
			}

		}
	}
	
	public void updateConf() {

		try {
			output = new FileOutputStream(new File("config.properties"));
		} catch (FileNotFoundException e) {
			SwingUtilities.invokeLater(
					() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		}

		try {
			this.prop.store(output, null);
		} catch (IOException e) {
			SwingUtilities.invokeLater(
					() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE));
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, e.getMessage(), "error",
							JOptionPane.ERROR_MESSAGE));
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * @return the prop
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * @param prop the prop to set
	 */
	public void setProp(Properties prop) {
		this.prop = prop;
	}
}
