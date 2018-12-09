import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.engine.QueryIterator;
import org.apache.jena.sparql.engine.binding.Binding;

public class SparqlEx {
    public void kappa() {
      /*  String queryStr =
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +
                        "SELECT DISTINCT ?name ?nick" +
                        "{?x foaf:mbox <mailt:person@server> ." +
                        "?x foaf:name ?name" +
                        "OPTIONAL { ?x foaf:nick ?nick }}";

        Query query = QueryFactory.create(queryStr);
        Op op = Algebra.compile(query);
        System.out.println(op);
        */

        String s = "SELECT DISTINCT ?s { ?s ?p ?o }";

        // Parse
        Query query = QueryFactory.create(s) ;
        System.out.println(query) ;

        // Generate algebra
        Op op = Algebra.compile(query) ;
        op = Algebra.optimize(op) ;
        System.out.println(op) ;

        // Execute it.
        QueryIterator qIter = Algebra.exec(op, ModelFactory.createDefaultModel()) ;

        // Results
        for ( ; qIter.hasNext() ; )
        {
            Binding b = qIter.nextBinding() ;
            System.out.println(b) ;
        }
        qIter.close() ;
    }
}
