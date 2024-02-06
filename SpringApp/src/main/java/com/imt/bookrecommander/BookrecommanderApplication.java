package com.imt.bookrecommander;

import com.github.jsonldjava.core.*;
import com.github.jsonldjava.utils.JsonUtils;
import org.apache.commons.io.Charsets;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.vocabulary.RDF;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.util.*;
import java.util.regex.Pattern;


@SpringBootApplication
@RestController
@RequestMapping("/api/v1")
public class BookrecommanderApplication {

	// final version
	// define model attribute
	Model model;

	public static Model loadModelFromFile(String filePath) {
		Model model = ModelFactory.createDefaultModel();

		try (InputStream inputStream = new FileInputStream(filePath)) {
			RDFDataMgr.read(model, inputStream, Lang.RDFXML);
			System.out.println("RDF data loaded from " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return model;
	}

	// Helper method to create a book resource with specified properties
	private static void createBook(Model model, Resource bookClass, Property hasTitleProperty, Property hasGenreProperty, Property authoredByProperty, String title, String genre, String author) {
		Resource book = model.createResource("http://example.org#" + title)
				.addProperty(RDF.type, bookClass)
				.addProperty(hasTitleProperty, title)
				.addProperty(hasGenreProperty, "http://example.org#" + genre)
				.addProperty(authoredByProperty, "http://example.org#" + author);
	}

	private void initializeLoadFile(){
		// initialize the file and read our file
		// Specify the output file
		String outputFile = "TriplebookRecomander.rdf";

		// Define namespaces
		String ex = "http://example.org#";
		String rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
		String xsd = "http://www.w3.org/2001/XMLSchema#";
		String vcard = "http://www.w3.org/2001/vcard-rdf/3.0#"; // Added VCARD namespace

		// Create an empty Model
		Model model = ModelFactory.createDefaultModel();

		// Define classes
		Resource bookClass = model.createResource(ex + "Book");

		// Define properties
		Property hasTitleProperty = model.createProperty(ex + "hasTitle");
		Property hasGenreProperty = model.createProperty(ex + "hasGenre");
		Property authoredByProperty = model.createProperty(ex + "authoredBy");

		// Create individuals (resources) for 10 books
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "CleanCode", "ComputerScience", "Robert C Martin");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ThePragmaticProgrammer", "Programming", "Dave Thomas and Andy Hunt");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "DesignPatterns", "SoftwareEngineering", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "Refactoring", "SoftwareEngineering", "Martin Fowler");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "CodeComplete", "SoftwareEngineering", "Steve McConnell");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TheArtOfComputerProgramming", "ComputerScience", "Donald Knuth");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "CleanArchitecture", "SoftwareEngineering", "Robert C Martin");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "PatternsOfEnterpriseApplicationArchitecture", "SoftwareEngineering", "Martin Fowler");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "IntroductionToAlgorithms", "ComputerScience", "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TheLeanStartup", "Business", "Eric Ries");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ExtremeProgrammingExplained", "Programming", "Kent Beck");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ThePhoenixProject", "Business", "Gene Kim, Kevin Behr, George Spafford");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "DomainDrivenDesign", "SoftwareEngineering", "Eric Evans");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ContinuousDelivery", "SoftwareEngineering", "Jez Humble, David Farley");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "AgileEstimatingAndPlanning", "Agile", "Mike Cohn");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TestDrivenDevelopment", "Programming", "Kent Beck");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "HeadFirstDesignPatterns", "Programming", "Eric Freeman, Elisabeth Robson, Bert Bates, Kathy Sierra");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TheCProgrammingLanguage", "Programming", "Brian W. Kernighan, Dennis M. Ritchie");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ArtificialIntelligence", "ArtificialIntelligence", "Stuart Russell, Peter Norvig");
		// Add more books as needed

		// Write the model to the file
		try (OutputStream outputStream = new FileOutputStream(outputFile)) {
			RDFDataMgr.write(outputStream, model, Lang.RDFXML);
			System.out.println("RDF data written to " + outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BookrecommanderApplication(){

		//initializeLoadFile();
		// and after load the file
		this.model = loadModelFromFile("TriplebookRecomander.rdf");
	}

	// Add this function to query the provided Model with the SPARQL query
	// model i will use it as defined static attribute
//	@PostMapping("/query")
//	public ResponseEntity<List<Map<String, String>>> queryModelWithSPARQL(@RequestBody String sparqlQuery) {
//
//		Query query = QueryFactory.create(sparqlQuery);
//
//		List<Map<String, String>> queryResults = new ArrayList<>();
//
//		try (QueryExecution qexec = QueryExecutionFactory.create(query, this.model)) {
//			ResultSet results = qexec.execSelect();
//
//			while (results.hasNext()) {
//				QuerySolution soln = results.nextSolution();
//
//				// Process each solution and add it to the list
//				Map<String, String> resultMap = new HashMap<>();
//				soln.varNames().forEachRemaining(var -> {
//					RDFNode value = soln.get(var);
//					resultMap.put(var, value.toString());
//				});
//
//				queryResults.add(resultMap);
//			}
//		}
//
//		// Return the list of query results as JSON
//		return ResponseEntity.ok(queryResults);
//	}

	@PostMapping("/query")
	public ResponseEntity<List<Map<String, String>>> queryModelWithParameters(@RequestBody Map<String, String> requestBody) {
		String title = requestBody.get("title");
		String genre = requestBody.get("genre"); // Updated 'type' to 'genre'
		String author = requestBody.get("author");

		String queryString = buildQuery(title, genre, author); // Updated method parameters
		System.out.println("Query string: " + queryString);

		Query query = QueryFactory.create(queryString);

		List<Map<String, String>> queryResults = new ArrayList<>();

		try (QueryExecution qexec = QueryExecutionFactory.create(query, this.model)) {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();

				// Process each solution and add it to the list
				Map<String, String> resultMap = new HashMap<>();
				soln.varNames().forEachRemaining(var -> {
					RDFNode value = soln.get(var);
					resultMap.put(var, value.toString());
				});

				queryResults.add(resultMap);
			}
		}

		System.out.println("Query results: " + queryResults);

		// Return the list of query results as JSON
		return ResponseEntity.ok(queryResults);
	}

	// Helper method to build the query based on parameters
	// Helper method to build the query based on parameters
	private String buildQuery(String title, String genre, String author) {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("PREFIX ex: <http://example.org#>\n")
				.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n")
				.append("PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n")
				.append("SELECT ?book ?title ?genre ?author\n")
				.append("WHERE {\n")
				.append("  ?book rdf:type ex:Book .\n")
				.append("  ?book ex:hasGenre ?genre .\n")
				.append("  ?book ex:authoredBy ?author .\n")
				.append("  ?book ex:hasTitle ?title .\n");

		// Filter based on provided parameters
		if (title != null && !title.isEmpty()) {
			queryBuilder.append("  FILTER (regex(?title, \"").append(title).append("\", \"i\"))\n");
		}

		if (genre != null && !genre.isEmpty()) {
			// Adjusted property for the book genre
			queryBuilder.append("  FILTER (regex(?genre, \"").append(genre).append("\", \"i\"))\n");
		}

		if (author != null && !author.isEmpty()) {
			// Adjusted property for the book author
			queryBuilder.append("  FILTER (regex(?author, \"").append(author).append("\", \"i\"))\n");
		}

		queryBuilder.append("}");

		return queryBuilder.toString();
	}




	@GetMapping("/books")
	public ResponseEntity<List<Map<String, String>>> getAllBooks() {
		String queryString = "PREFIX ex: <http://example.org#>\n" +
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + // Define the rdf prefix
				"SELECT ?book ?title ?genre ?author\n" +
				"WHERE {\n" +
				"  ?book rdf:type ex:Book .\n" + // Use the rdf prefix
				"  ?book ex:hasGenre ?genre .\n" +
				"  ?book ex:authoredBy ?author .\n" +
				"  ?book ex:hasTitle ?title .\n" +
				"}";


		Query query = QueryFactory.create(queryString);

		List<Map<String, String>> queryResults = new ArrayList<>();

		try (QueryExecution qexec = QueryExecutionFactory.create(query, this.model)) {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();

				// Process each solution and add it to the list
				Map<String, String> resultMap = new HashMap<>();
				soln.varNames().forEachRemaining(var -> {
					RDFNode value = soln.get(var);
					resultMap.put(var, value.toString());
				});

				queryResults.add(resultMap);
			}
		}

		// Return the list of query results as JSON
		return ResponseEntity.ok(queryResults);
	}


	// Helper method to escape special characters in the regex


	@GetMapping("/view")
	public ResponseEntity<String> viewOntology() {
		StringWriter ontologyStringWriter = new StringWriter();

		// Write the ontology model to a string
		RDFDataMgr.write(ontologyStringWriter, this.model, Lang.RDFXML);

		// Return the ontology as a response
		return ResponseEntity.ok(ontologyStringWriter.toString());
	}


	public static void main(String[] args) {
		SpringApplication.run(BookrecommanderApplication.class, args);
	}

}
