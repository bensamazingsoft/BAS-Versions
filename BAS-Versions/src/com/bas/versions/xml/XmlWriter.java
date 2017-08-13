package com.bas.versions.xml;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.bas.versions.utils.CheckPoint;

public class XmlWriter {

	public XmlWriter() {

	}

	/**
	 * writes Checkpoint to disk in an xml file
	 * 
	 * @param chkPt
	 */
	public void writeChckPtXml(CheckPoint chkPt) {

		try {
			Path xportPath = Paths.get(chkPt.getChckPtPath() + "\\Save\\");
			File xportFile = new File(xportPath.toFile().getAbsolutePath() + chkPt.toString().trim() + ".xml");

			// xportFile.mkdirs();

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element rootElt = doc.createElement("checkpoint");
			// rootElt.setAttributeNS("http://www.w3.org/2001/XMLSchema",
			// "xs:noNamespaceSchemaLocation-instance",
			// "com/bas/versions/xml/CheckPoint.xsd");
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

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer trans = transFactory.newTransformer();
			DOMSource domSrc = new DOMSource(doc);
			StreamResult result = new StreamResult(xportFile);
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			trans.transform(domSrc, result);

		} catch (DOMException | ParserConfigurationException | TransformerFactoryConfigurationError
				| TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
