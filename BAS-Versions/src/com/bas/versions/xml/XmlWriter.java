package com.bas.versions.xml;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.bas.versions.utils.CheckPoint;
import com.bas.versions.utils.Project;

public class XmlWriter {

	public XmlWriter() {

	}

	/**
	 * writes Checkpoint to disk in an xml file with .basv extension (for
	 * cosmetic reasons)
	 * 
	 * @param chkPt
	 */
	public void writeChckPtXml(CheckPoint chkPt) {

		try {
			Path xportPath = Paths.get(chkPt.getChckPtPath() + "\\Save\\");
			File xportFile = new File(xportPath.toFile().getAbsolutePath() + chkPt.toString().trim() + ".basv");

			Document doc = mkChkPtDoc(chkPt);

			writeXmlFile(xportFile, doc);

		} catch (DOMException | ParserConfigurationException | TransformerFactoryConfigurationError
				| TransformerException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * writes Project to disk in an xml file with .basv extension (for cosmetic
	 * reasons
	 * 
	 * @param proj
	 */
	public void WriteProjectXml(Project proj) {

		final File xmlFile = new File(
				proj.getProjectPath() + "\\BAS-CheckPoints\\" + proj.getProjectPath().toFile().getName() + ".basv");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();

			Document docPro = docBuilder.newDocument();
			Element rootProElt = docPro.createElement("project");
			Attr idAttr = docPro.createAttribute("pr_id");
			idAttr.setValue(String.valueOf(proj.getId()));
			rootProElt.setAttributeNode(idAttr);
			Attr dateAttr = docPro.createAttribute("pr_date");
			dateAttr.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00'").format(proj.getDateCreated()));
			rootProElt.setAttributeNode(dateAttr);
			Attr projectPathAttr = docPro.createAttribute("projectPath");
			projectPathAttr.setValue(proj.getProjectPath().toFile().getAbsolutePath());
			rootProElt.setAttributeNode(projectPathAttr);

			Element filterInElt = docPro.createElement("filterIn");
			filterInElt.setTextContent(proj.getFilterIn());
			rootProElt.appendChild(filterInElt);

			Element filterOutElt = docPro.createElement("filterOut");
			filterOutElt.setTextContent(proj.getFilterOut());
			rootProElt.appendChild(filterOutElt);

			Element stack = docPro.createElement("stack");
			rootProElt.appendChild(stack);
			Iterator<CheckPoint> it = proj.getCheckPointStack().iterator();
			while (it.hasNext()) {
				Document cpDoc = mkChkPtDoc(it.next());
				Node cpNode = cpDoc.getDocumentElement();
				docPro.adoptNode(cpNode);
				stack.appendChild(cpNode);
			}

			Element state = docPro.createElement("state");
			rootProElt.appendChild(state);
			Iterator<File> it2 = proj.getState().iterator();
			while (it2.hasNext()) {
				Element stateFile = docPro.createElement("stateFile");
				stateFile.setTextContent(it2.next().getAbsolutePath());
				state.appendChild(stateFile);
			}

			docPro.appendChild(rootProElt);

			writeXmlFile(xmlFile, docPro);
		} catch (DOMException | ParserConfigurationException | TransformerFactoryConfigurationError
				| TransformerException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (xmlFile.exists()) {
					// JOptionPane.showMessageDialog(null, "project saved",
					// "info", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "project save failed", "error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * @param xportFile
	 * @param doc
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerConfigurationException
	 * @throws TransformerException
	 */
	private void writeXmlFile(File xportFile, Document doc)
			throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
		TransformerFactory transFactory = TransformerFactory.newInstance();
		Transformer trans = transFactory.newTransformer();
		DOMSource domSrc = new DOMSource(doc);
		StreamResult result = new StreamResult(xportFile);
		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		trans.transform(domSrc, result);

	}

	/**
	 * @param chkPt
	 * @return
	 * @throws ParserConfigurationException
	 */
	private Document mkChkPtDoc(CheckPoint chkPt) throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();

		Element rootElt = doc.createElement("checkpoint");
		Attr idAttr = doc.createAttribute("id");
		idAttr.setValue(String.valueOf(chkPt.getId()));
		rootElt.setAttributeNode(idAttr);
		Attr dateAttr = doc.createAttribute("date");
		dateAttr.setValue(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:'00'").format(chkPt.getDateCreated()));
		rootElt.setAttributeNode(dateAttr);
		Attr projectPathAttr = doc.createAttribute("projectPath");
		projectPathAttr.setValue(chkPt.getProjectPath().toFile().getAbsolutePath());
		rootElt.setAttributeNode(projectPathAttr);
		Attr chkptPath = doc.createAttribute("checkpointPath");
		chkptPath.setValue(chkPt.getChckPtPath().toFile().getAbsolutePath());
		rootElt.setAttributeNode(chkptPath);

		doc.appendChild(rootElt);

		Element msgElt = doc.createElement("message");
		msgElt.appendChild(doc.createTextNode(chkPt.getChkPtMsg()));
		rootElt.appendChild(msgElt);

		Element filesElt = doc.createElement("files");
		rootElt.appendChild(filesElt);

		for (File file : chkPt.getChckPtFileList()) {
			Element fileElt = doc.createElement("file");
			fileElt.appendChild(doc.createTextNode(file.getAbsolutePath()));
			filesElt.appendChild(fileElt);
		}
		return doc;
	}

}
