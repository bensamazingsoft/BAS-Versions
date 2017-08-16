package com.bas.versions.xml;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class ProjectParser {

	private Document doc;

	public ProjectParser(File xmlFile) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sFactory
					.newSchema(getClass().getClassLoader().getResource("com/bas/versions/xml/Project.xsd"));
			factory.setSchema(schema);

			DocumentBuilder builder = factory.newDocumentBuilder();
			ErrorHandler errHandler = new ParseErrorHandler();
			builder.setErrorHandler(errHandler);

			this.doc = builder.parse(xmlFile);
		} catch (final SAXException | ParserConfigurationException | IOException e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null, e.getMessage(), "error in ProjectParser class",
							JOptionPane.ERROR_MESSAGE);
				}
			});
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return Document
	 */
	public Document getDoc() {
		return this.doc;
	}
}
