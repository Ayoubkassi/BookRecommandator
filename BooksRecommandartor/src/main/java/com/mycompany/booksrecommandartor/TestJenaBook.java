/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.booksrecommandartor;

import java.util.Scanner;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author kassi
 */
public class TestJenaBook {
        private Model model;
	private String file;
	private String namespace;

	TestJenaBook(String path) {
		this.namespace = "";
		this.file = path;
		this.model = JenaEngine.readModel(path);
		if (model != null) {
			namespace = model.getNsPrefixURI("");
		}
	}

	public String getInput() {
		System.out.println("Please enter a name of a book: ");
		Scanner sc = new Scanner(System.in);
		return sc.nextLine();
	}

	public void searchBook(String title) {
		if (hasBook(title)) {
			Resource rs = model.getResource(namespace + title);
			System.out.println(rs.getLocalName());
			JenaEngine.readRsDataType(model, namespace, rs, "year");
			JenaEngine.readRsDataType(model, namespace, rs, "country");
			JenaEngine.readObjectType(model, namespace, title, "hasGenres");
			JenaEngine.readObjectType(model, namespace, title, "hasActor");
		} else {
			System.out.println("Error: Wrong title!");
		}
	}

	public boolean hasBook(String title) {
		Resource rs = model.getResource(namespace + title);
		Property ptitle = model.getProperty(namespace + "title");
		if (rs != null && ptitle != null) {
			if(rs.getProperty(ptitle) != null )
				return true;
			else
				return false;
		}
		return false;
	}
}
