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
	private static void createBook(Model model, Resource bookClass, Property hasTitleProperty,
								   Property hasGenreProperty, Property authoredByProperty, String title, String genre, String author) {
		Resource book = model.createResource("http://example.org#" + title)
				.addProperty(RDF.type, bookClass)
				.addProperty(hasTitleProperty, title)
				.addProperty(hasGenreProperty, "http://example.org#" + genre)
				.addProperty(authoredByProperty, "http://example.org#" + author);
	}

	private void initializeLoadFile() {
		// initialize the file and read our file
		// Specify the output file
		String outputFile = "project.rdf";

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
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "CleanCode",
				"ComputerScience", "Robert C Martin");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ThePragmaticProgrammer",
				"Programming", "Dave Thomas and Andy Hunt");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "DesignPatterns",
				"SoftwareEngineering", "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "Refactoring",
				"SoftwareEngineering", "Martin Fowler");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "CodeComplete",
				"SoftwareEngineering", "Steve McConnell");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty,
				"TheArtOfComputerProgramming", "ComputerScience", "Donald Knuth");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "CleanArchitecture",
				"SoftwareEngineering", "Robert C Martin");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty,
				"PatternsOfEnterpriseApplicationArchitecture", "SoftwareEngineering", "Martin Fowler");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "IntroductionToAlgorithms",
				"ComputerScience", "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, Clifford Stein");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TheLeanStartup",
				"Business", "Eric Ries");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty,
				"ExtremeProgrammingExplained", "Programming", "Kent Beck");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ThePhoenixProject",
				"Business", "Gene Kim, Kevin Behr, George Spafford");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "DomainDrivenDesign",
				"SoftwareEngineering", "Eric Evans");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ContinuousDelivery",
				"SoftwareEngineering", "Jez Humble, David Farley");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty,
				"AgileEstimatingAndPlanning", "Agile", "Mike Cohn");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TestDrivenDevelopment",
				"Programming", "Kent Beck");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "HeadFirstDesignPatterns",
				"Programming", "Eric Freeman, Elisabeth Robson, Bert Bates, Kathy Sierra");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "TheCProgrammingLanguage",
				"Programming", "Brian W. Kernighan, Dennis M. Ritchie");
		createBook(model, bookClass, hasTitleProperty, hasGenreProperty, authoredByProperty, "ArtificialIntelligence",
				"ArtificialIntelligence", "Stuart Russell, Peter Norvig");
		// Add more books as needed

		// Write the model to the file
		try (OutputStream outputStream = new FileOutputStream(outputFile)) {
			RDFDataMgr.write(outputStream, model, Lang.RDFXML);
			System.out.println("RDF data written to " + outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BookrecommanderApplication() {

		// initializeLoadFile();
		// and after load the file
		this.model = loadModelFromFile("project.rdf");
	}


	@PostMapping("/query")
	public ResponseEntity<List<Map<String, String>>> queryModelWithParameters(@RequestBody Map<String, String> requestBody) {
		String author = requestBody.get("author");

		String queryString = buildQuery(author);
		System.out.println("Query string: " + queryString);

		Query query = QueryFactory.create(queryString);

		List<Map<String, String>> queryResults = new ArrayList<>();

		try (QueryExecution qexec = QueryExecutionFactory.create(query, this.model)) {
			ResultSet results = qexec.execSelect();
			System.out.printf("data" , results);
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();

				Map<String, String> resultMap = new HashMap<>();
				soln.varNames().forEachRemaining(varName -> resultMap.put(varName, soln.get(varName).toString().split("#")[1]));

				queryResults.add(resultMap);
			}
		}

		System.out.println("Query results: " + queryResults);

		return ResponseEntity.ok(queryResults);
	}

	private String buildQuery(String author) {
		String prefixes = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX ex: <http://www.semanticweb.org/ahmed/ontologies/2024/0/book#>\n";

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(prefixes)
				.append("SELECT ?book ?title ?genre ?author ?ISBN ?Notation ?abstract ?année_de_publication ?langue ?mots_clés ?nombre_de_pages\n")
				.append("WHERE {\n")
				.append("  ?book rdf:type ex:Livre .\n")
				.append("  ?book ex:écritPar ?author .\n")
				.append("  OPTIONAL { ?book ex:hasTitle ?title }\n")
				.append("  OPTIONAL { ?book ex:hasGenre ?genre }\n")
				.append("  OPTIONAL { ?book ex:hasISBN ?ISBN }\n")
				.append("  OPTIONAL { ?book ex:hasNotation ?Notation }\n")
				.append("  OPTIONAL { ?book ex:hasAbstract ?abstract }\n")
				.append("  OPTIONAL { ?book ex:hasYearOfPublication ?année_de_publication }\n")
				.append("  OPTIONAL { ?book ex:hasLanguage ?langue }\n")
				.append("  OPTIONAL { ?book ex:hasKeywords ?mots_clés }\n")
				.append("  OPTIONAL { ?book ex:hasNumberOfPages ?nombre_de_pages }\n");

		if (author != null && !author.isEmpty()) {
			// Extract the author name from the URI after the last '#'
			String authorName = author.substring(author.lastIndexOf("#") + 1);
			queryBuilder.append("  FILTER (regex(str(?author), \"").append(authorName).append("\", \"i\"))\n");
		}

		queryBuilder.append("}");

		return queryBuilder.toString();
	}


	@GetMapping("/books")
	public ResponseEntity<List<Map<String, String>>> getAllBooks() {

		String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX book: <http://www.semanticweb.org/ahmed/ontologies/2024/0/book#>\n" +
				"SELECT ?book ?title ?genre ?author ?ISBN ?Notation ?abstract ?année_de_publication ?langue ?mots_clés ?nombre_de_pages\n" +
				"WHERE {\n" +
				"  ?book rdf:type book:Livre .\n" +
				"  ?book book:nom ?title .\n" +
				"  OPTIONAL { ?book book:genre ?genre }\n" +
				"  OPTIONAL { ?book book:écritPar ?author }\n" +
				"  OPTIONAL { ?book book:ISBN ?ISBN }\n" +
				"  OPTIONAL { ?book book:Notation ?Notation }\n" +
				"  OPTIONAL { ?book book:abstract ?abstract }\n" +
				"  OPTIONAL { ?book book:année_de_publication ?année_de_publication }\n" +
				"  OPTIONAL { ?book book:langue ?langue }\n" +
				"  OPTIONAL { ?book book:mots_clés ?mots_clés }\n" +
				"  OPTIONAL { ?book book:nombre_de_pages ?nombre_de_pages }\n" +
				"}";

		Query query = QueryFactory.create(queryString);

		List<Map<String, String>> queryResults = new ArrayList<>();

		try (QueryExecution qexec = QueryExecutionFactory.create(query, this.model)) {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();

				// Process each solution and add it to the list
				Map<String, String> resultMap = new HashMap<>();
				resultMap.put("book", soln.get("book").toString().split("#")[1]);
				resultMap.put("title", soln.get("title").toString());
				resultMap.put("genre", getStringOrNull(soln, "genre"));
				resultMap.put("author", getStringOrNull(soln, "author"));
				resultMap.put("ISBN", getStringOrNull(soln, "ISBN"));
				resultMap.put("Notation", getStringOrNull(soln, "Notation"));
				resultMap.put("abstract", getStringOrNull(soln, "abstract"));
				resultMap.put("année_de_publication", getStringOrNull(soln, "année_de_publication"));
				resultMap.put("langue", getStringOrNull(soln, "langue"));
				resultMap.put("mots_clés", getStringOrNull(soln, "mots_clés"));
				resultMap.put("nombre_de_pages", getStringOrNull(soln, "nombre_de_pages"));

				queryResults.add(resultMap);
			}
		}

		// Return the list of query results as JSON
		return ResponseEntity.ok(queryResults);
	}

	// Helper method to handle null values
	private String getStringOrNull(QuerySolution soln, String variable) {
		RDFNode node = soln.get(variable);
		if( node != null && variable == "author" )
			return  node.toString().split("#")[1];
		return node != null ? node.toString() : null;
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