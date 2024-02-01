/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.booksrecommandartor;

/**
 *
 * @author kassi
 */
public interface Queries {
    
    String BASE_PREFIXES =
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                    "PREFIX boo: <file://C:/Users/olega/Desktop/company-data-loader/database.owl/Orgs#>";

    String CHECK_INN_EXISTENCE = BASE_PREFIXES  +
            "SELECT ?INST WHERE { ?INST a boo:INN . FILTER (?INST = boo:%s) }";

    String SELECT_COMPANY_NAME_BY_INN = BASE_PREFIXES +
            "SELECT ?INST WHERE { ?INST a boo:CompanyName . boo:%s boo:hasCompanyName ?INST}";

    String SELECT_COMPANY_ADDRESS_BY_INN = BASE_PREFIXES +
            "SELECT ?INST WHERE { ?INST a boo:CompanyAddress . boo:%s boo:hasCompanyAddress ?INST}";
    
}
