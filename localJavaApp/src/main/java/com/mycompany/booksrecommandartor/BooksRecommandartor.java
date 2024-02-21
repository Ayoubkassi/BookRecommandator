/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.booksrecommandartor;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import static org.apache.jena.query.QueryExecution.model;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;

/**
 *
 * @author kassi
 */
public class BooksRecommandartor {
    
    public void RDFInitializer(){
        // some definitions
        String personURI    = "http://somewhere/AyoubKassi";
        String givenName    = "Ayoub";
        String familyName   = "Kassi";
        String fullName     = givenName + " " + familyName;

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        //   and add the properties cascading style
        Resource ayoubKassi
          = model.createResource(personURI)
                 .addProperty(VCARD.FN, fullName)
                 .addProperty(VCARD.N,
                              model.createResource()
                                   .addProperty(VCARD.Given, givenName)
                                   .addProperty(VCARD.Family, familyName));
        
        // now write the model in XML form to a file
        //System.out.println("Model Out here ****************");
        //model.write(System.out);
        
       //model.write(System.out);
       
        // list the statements in the graph
//        StmtIterator iter = model.listStatements();
//        
//        // print out the predicate, subject and object of each statement
//        while (iter.hasNext()) {
//            Statement stmt      = iter.nextStatement();         // get next statement
//            Resource  subject   = stmt.getSubject();   // get the subject
//            Property  predicate = stmt.getPredicate(); // get the predicate
//            RDFNode   object    = stmt.getObject();    // get the object
//            
//            System.out.print(subject.toString());
//            System.out.print(" " + predicate.toString() + " ");
//            if (object instanceof Resource) {
//                System.out.print(object.toString());
//            } else {
//                // object is a literal
//                System.out.print(" \"" + object.toString() + "\"");
//            }
//            System.out.println(" .");
//        }

    }
    
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
    
    public static void BookRDFInitializer(){
        
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
    
    public static final String inputFileName = "bookRecomander.rdf";
    
    public static void queryModel(){
            
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
       
        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
        if (in == null) {
            throw new IllegalArgumentException( "File: " + inputFileName + " not found");
        }
        
        // read the RDF/XML file
        model.read( in, "");
        
        // select all the resources with a VCARD.FN property
        ResIterator iter = model.listResourcesWithProperty(VCARD.FN);
        if (iter.hasNext()) {
            System.out.println("The database contains vcards for:");
            while (iter.hasNext()) {
                System.out.println("  " + iter.nextResource()
                                              .getRequiredProperty(VCARD.FN)
                                              .getString() );
            }
        } else {
            System.out.println("No vcards were found in the database");
        }    
        
    }
    
     // Add this function to query the provided Model with the SPARQL query
    public void queryModelWithSPARQL(Model model, String sparqlQuery) {
        // Create the SPARQL query
        System.out.println("start querying");
        Query query = QueryFactory.create(sparqlQuery);

        // Execute the query on the provided model
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            
            // Process and print the query results
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                // Process each solution as needed
                // Example: Print the values of variables in the solution
                soln.varNames().forEachRemaining(var -> {
                    RDFNode value = soln.get(var);
                    System.out.println(var + ": " + value);
                });
            }
        }
    }
    
    public static void main(String[] args) {
       
        // start with RDF Format
        BooksRecommandartor bookrecommander = new BooksRecommandartor();
        //bookrecommander.RDFInitializer();
        //bookrecommander.BookRDFInitializer();
        //bookrecommander.queryModel();
        
        //bookrecommander.RDFInitializer();
        bookrecommander.BookRDFInitializer();

        // Load the model from the file
        Model model = loadModelFromFile("TriplebookRecomander.rdf");

        // SPARQL query to retrieve information about the book with the title "CleanCode"
        String sparqlQuery = 
    "PREFIX ex: <http://example.org#>\n" +
    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
    "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
    "SELECT ?book ?title ?genre ?author\n" +
    "WHERE {\n" +
    "  ?book rdf:type ex:Book .\n" +
    "  ?book ex:hasGenre ?genre .\n" +
    "  ?book ex:authoredBy ?author .\n" +
    "  ?book ex:hasTitle ?title .\n" +  // Adjusted property for the book title
    "  FILTER (regex(?title, \"CleanCode\", \"i\"))\n" +
    "}";

        // Query the model with the modified SPARQL query
        bookrecommander.queryModelWithSPARQL(model, sparqlQuery);

        
        
    }
}
