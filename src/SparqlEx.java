import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;

public class SparqlEx {
    public void kappa() {
        String queryStr =
                "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>" +
                        "SELECT DISTINCT ?name ?nick" +
                        "{?x foaf:mbox <mailt:person@server> ." +
                        "?x foaf:name ?name" +
                        "OPTIONAL { ?x foaf:nick ?nick }}";

        Query query = QueryFactory.create(queryStr);
        Op op = Algebra.compile(query);
        System.out.println(op);
    }
}
