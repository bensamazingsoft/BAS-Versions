package com.bas.versions.xml;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public class CheckPointParser {

	private Document doc;

	public CheckPointParser(File xmlFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = sFactory.newSchema(new File ("src/com/bas/versions/xml/CheckPoint.xsd"));
			
			factory.setSchema(schema);
			
			DocumentBuilder builder = factory.newDocumentBuilder();

			ErrorHandler errHandler = new ParseErrorHandler();

			builder.setErrorHandler(errHandler);

			this.doc = builder.parse(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * @return the doc
	 */
	public Document getDoc() {
		return this.doc;
	}
}
