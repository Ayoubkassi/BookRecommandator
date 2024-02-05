/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.booksrecommandartor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import java.util.List;
import javax.management.Query;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

/**
 *
 * @author kassi
 */
public class JenaEngine {

	static public String RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	static public Model readModel(String inputDataFile) {
		// create an empty model
		Model model = ModelFactory.createDefaultModel();

		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(inputDataFile);
		if (in == null) {
			System.out
					.println("Ontology file: " + inputDataFile + " not found");
			return null;
		}

		// read the RDF/XML file
		model.read(in, "");
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return model;
	}

	static public Model readInferencedModelFromRuleFile(Model model,
			String inputRuleFile) {
		InputStream in = FileManager.get().open(inputRuleFile);
		if (in == null) {
			System.out.println("Rule File: " + inputRuleFile + " not found");
			return null;
		} else {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}

		List rules = Rule.rulesFromURL(inputRuleFile);
		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		reasoner.setDerivationLogging(true);
		reasoner.setOWLTranslation(true); // not needed in RDFS case
		reasoner.setTransitiveClosureCaching(true);
		InfModel inf = ModelFactory.createInfModel(reasoner, model);
		return inf;
	}

	static public String executeQuery(Model model, String queryString) {
		org.apache.jena.query.Query query = QueryFactory.create(queryString);
		// No reasoning
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = (ResultSet) qe.execSelect();

		OutputStream output = new OutputStream() {

			private StringBuilder string = new StringBuilder();

			public void write(int b) throws IOException {
				this.string.append((char) b);
			}

			// Netbeans IDE automatically overrides this toString()
			public String toString() {
				return this.string.toString();
			}
		};

		ResultSetFormatter.out(output, (org.apache.jena.query.ResultSet) results, query);
		return output.toString();
	}

	static public String executeQueryFile(Model model, String filepath) {
		File queryFile = new File(filepath);
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(filepath);
		if (in == null) {
			System.out.println("Query file: " + filepath + " not found");
			return null;
		} else {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		String queryString = FileTool.getContents(queryFile);
		return executeQuery(model, queryString);
	}

	static public String executeQueryFileWithParameter(Model model,
			String filepath, String parameter) {
		File queryFile = new File(filepath);
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(filepath);
		if (in == null) {
			System.out.println("Query file: " + filepath + " not found");
			return null;
		} else {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		String queryString = FileTool.getContents(queryFile);
		queryString = queryString.replace("%PARAMETER%", parameter);
		return executeQuery(model, queryString);
	}

	static public boolean createInstanceOfClass(Model model, String namespace,
			String className, String instanceName) {
		Resource rs = model.getResource(namespace + instanceName);
		if (rs == null)
			rs = model.createResource(namespace + instanceName);
		Property p = model.getProperty(RDF + "type");
		Resource rs2 = model.getResource(namespace + className);
		if ((rs2 != null) && (rs != null) && (p != null)) {
			// add new value
			rs.addProperty(p, rs2);
			return true;
		}
		return false;
	}

	static public boolean updateValueOfObjectProperty(Model model,
			String namespace, String object1Name, String propertyName,
			String object2Name) {
		Resource rs1 = model.getResource(namespace + object1Name);
		Resource rs2 = model.getResource(namespace + object2Name);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs1 != null) && (rs2 != null) && (p != null)) {
			// remove all old values of property p
			rs1.removeAll(p);
			// add new value
			rs1.addProperty(p, rs2);
			return true;
		}
		return false;
	}

	static public boolean addValueOfObjectProperty(Model model,
			String namespace, String instance1Name, String propertyName,
			String instance2Name) {
		Resource rs1 = model.getResource(namespace + instance1Name);
		Resource rs2 = model.getResource(namespace + instance2Name);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs1 != null) && (rs2 != null) && (p != null)) {
			// add new value
			rs1.addProperty(p, rs2);
			return true;
		}
		return false;
	}

	static public boolean updateValueOfDataTypeProperty(Model model,
			String namespace, String instanceName, String propertyName,
			Object value) {
		Resource rs = model.getResource(namespace + instanceName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			// remove all old values of property p
			rs.removeAll(p);
			// add new value
			rs.addLiteral(p, value);
			return true;
		}
		return false;
	}

	static public boolean addValueOfDataTypeProperty(Model model,
			String namespace, String instanceName, String propertyName,
			Object value) {
		Resource rs = model.getResource(namespace + instanceName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			// add new value
			rs.addLiteral(p, value);
			return true;
		}
		return false;
	}

	static public boolean removeAllValuesOfProperty(Model model,
			String namespace, String objectName, String propertyName) {
		Resource rs = model.getResource(namespace + objectName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			// remove all old values of property p
			rs.removeAll(p);
			// add new value
			return true;
		}
		return false;
	}

	// get OntModel
	static public OntModel readOntmodel(String inputfile) {
		OntModel model = ModelFactory.createOntologyModel();

		InputStream in = FileManager.get().open(inputfile);
		if (in == null) {
			System.out
					.println("Ontology file: " + inputfile + " not found");
			return null;
		}
		model.read(in, "");
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		return model;
	}

	// read the value of property of type ObjectType
	static public void readObjectType(Model model, String namespace,
			String objectName, String propertyName) {

		Resource rs = model.getResource(namespace + objectName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			StmtIterator it = rs.listProperties(p);
			if (!it.hasNext()) {
				System.out.println(objectName + " " + propertyName + ": "
						+ null);
			} else {
				System.out.println(objectName + " " + propertyName + ": ");
				while (it.hasNext()) {

					Statement s = it.next();
					Resource re = s.getResource();
					readRsDataType(model, namespace, re, "name");
					readRsDataType(model, namespace, re, "genrename");
				}
			}
		}
	}

	// read the value of property of type DataType
	static public void readDataType(Model model, String namespace,
			String objectName, String propertyName) {
		Resource rs = model.getResource(namespace + objectName);
		Property p = model.getProperty(namespace + propertyName);
		if ((rs != null) && (p != null)) {
			if (rs.getProperty(p) != null) {
				System.out.println(propertyName + " of " + objectName + " is: "
						+ rs.getProperty(p).getString());
			} else {
				System.out.println(propertyName + " of " + objectName + " is: "
						+ null);
			}
		}
	}

	// read the value of property of type DataType from a resource
	static public void readRsDataType(Model model, String namespace,
			Resource rs, String propertyName) {
		Property p = model.getProperty(namespace + propertyName);
		if (p != null) {
			if (rs.getProperty(p) != null) {
				System.out.println(propertyName + ": "
						+ rs.getProperty(p).getString());
			}
		}
	}

}
