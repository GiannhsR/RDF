import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.util.InvalidBTreeStateException;
import org.apache.jena.query.QueryException;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;

import java.io.IOException;
import java.util.*;

class Run {
    static void initialize(boolean recreateIndex) throws IOException, UnknownIndexException {
        long startAllTime;
        long finishAllTime;
        long startTime;
        long stopTime;
        try {
            //Basic indexes paths
            String treeFilePathSPO = "watdiv10M\\17MBFOLDER\\SPO_index_17MB";
            String treeFilePathSOP = "watdiv10M\\17MBFOLDER\\SOP_index_17MB";
            String treeFilePathPOS = "watdiv10M\\17MBFOLDER\\POS_index_17MB";
            String treeFilePathPSO = "watdiv10M\\17MBFOLDER\\PSO_index_17MB";
            String treeFilePathOSP = "watdiv10M\\17MBFOLDER\\OSP_index_17MB";
            String treeFilePathOPS = "watdiv10M\\17MBFOLDER\\OPS_index_17MB";
            //Permutations indexes paths
            String treeFilePathSP_O = "watdiv10M\\17MBFOLDER\\SP_O_index_17MB";
            String treeFilePathOP_S = "watdiv10M\\17MBFOLDER\\OP_S_index_17MB";
            String treeFilePathSO_P = "watdiv10M\\17MBFOLDER\\SO_P_index_17MB";

            //Basic indexes
            Index indexSPO = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathSPO, new BPlusTreePerformanceCounter(true));
            Index indexSOP = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathSOP, new BPlusTreePerformanceCounter(true));
            Index indexPOS = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathPOS, new BPlusTreePerformanceCounter(true));
            Index indexPSO = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathPSO, new BPlusTreePerformanceCounter(true));
            Index indexOSP = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathOSP, new BPlusTreePerformanceCounter(true));
            Index indexOPS = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathOPS, new BPlusTreePerformanceCounter(true));
            //Permutations
            Index indexSP_O = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathSP_O, new BPlusTreePerformanceCounter(true));
            Index indexOP_S = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathOP_S, new BPlusTreePerformanceCounter(true));
            Index indexSO_P = new Index(new BPlusConfiguration(), recreateIndex ? "rw+" : "rw", treeFilePathSO_P, new BPlusTreePerformanceCounter(true));

            if (recreateIndex) {
                System.out.println("Creating indexes...");
                startTime = System.nanoTime();
                indexSPO.createIndex("SPO");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until SPO index created : " + (stopTime - startTime));
                indexSPO.commitTree();

                System.out.println("Creating indexes...");
                startTime = System.nanoTime();
                indexSOP.createIndex("SOP");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until SOP index created : " + (stopTime - startTime));
                indexSOP.commitTree();

                startTime = System.nanoTime();
                indexPOS.createIndex("POS");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until POS index created : " + (stopTime - startTime));
                indexPOS.commitTree();

                startTime = System.nanoTime();
                indexPSO.createIndex("PSO");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until PSO index created : " + (stopTime - startTime));
                indexPSO.commitTree();

                startTime = System.nanoTime();
                indexOSP.createIndex("OSP");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until OSP index created : " + (stopTime - startTime));
                indexOSP.commitTree();

                startTime = System.nanoTime();
                indexOPS.createIndex("OPS");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until OPS index created : " + (stopTime - startTime));
                indexOPS.commitTree();

                startTime = System.nanoTime();
                indexSP_O.createIndex("SP_O");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until SP_O index created : " + (stopTime - startTime));
                indexSP_O.commitTree();
                indexSP_O.createMappingFiles("SP_O");

                startTime = System.nanoTime();
                indexOP_S.createIndex("OP_S");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until OP_S index created : " + (stopTime - startTime));
                indexOP_S.commitTree();
                indexOP_S.createMappingFiles("OP_S");

                startTime = System.nanoTime();
                indexSO_P.createIndex("SO_P");
                stopTime = System.nanoTime();
                System.out.println("Time elapsed until SO_P index created : " + (stopTime - startTime));
                indexSO_P.commitTree();
                indexSO_P.createMappingFiles("SO_P");

                System.out.println("Indexes created successfully.\n");
            } else {
                startAllTime = System.nanoTime();
                ReadFile.reconstructHashMapFromDictionary();
                HashMap<String, Integer> encodeMap = Run.getEncodeHashMap();
                HashMap<Integer, String> decodeMap = Run.getDecodeHashMap();

                String sparqlStr =
                        "PREFIX wsdbm: <http://db.uwaterloo.ca/~galuc/wsdbm/>\n " +
                                "PREFIX sch: <http://schema.org/>\n" +
                                "SELECT ?x\n" +
                                "WHERE\n" +
                                " { ?x  sch:eligibleRegion wsdbm:Country6 .\n" + " }";

                HashMap<String, LinkedList<String>> triple = Run.extractTriple(sparqlStr);

                String subject = "<".concat(triple.get("subjects").get(0)).concat(">");
                String predicate = "<".concat(triple.get("preds").get(0)).concat(">");
                String object = "<".concat(triple.get("objs").get(0)).concat(">");
                String[] tripleResources = Run.provideResources(subject, predicate, object);
                LinkedList<Integer> placesValuesFound = indexOP_S.searchInPermutationTree(encodeMap.get(tripleResources[2]).toString(), encodeMap.get(tripleResources[1]).toString(), "OP_S");
                LinkedList<Integer> subjectList = Index.queryOP_S(placesValuesFound, indexOP_S);
                placesValuesFound.clear();

                sparqlStr = "PREFIX wsdbm: <http://db.uwaterloo.ca/~galuc/wsdbm/>\n " +
                        "PREFIX sch: <http://schema.org/>\n" +
                        "SELECT ?x\n" +
                        "WHERE\n" +
                        " { ?x  sch:eligibleRegion 288 .\n" + " }";

                triple = Run.extractTriple(sparqlStr);

                subject = "<".concat(triple.get("subjects").get(0)).concat(">");
                predicate = "<".concat(triple.get("preds").get(0)).concat(">");
                if (!triple.get("objs").get(0).startsWith("http")) {
                    object = triple.get("objs").get(0);
                } else {
                    object = "<".concat(triple.get("objs").get(0)).concat(">");
                }

                String[] tripleResources2 = Run.provideResources(subject, predicate, object);
                placesValuesFound = indexOP_S.searchInPermutationTree(encodeMap.get(tripleResources2[2]).toString(), encodeMap.get(tripleResources2[1]).toString(), "OP_S");
                LinkedList<Integer> subjectList2 = Index.queryOP_S(placesValuesFound, indexOP_S);
                placesValuesFound.clear();

                startTime = System.nanoTime();
                LinkedList<Integer> joinedList = Index.join(subjectList, subjectList2);
                stopTime = System.nanoTime();
                System.out.println("Join list created: " + (stopTime - startTime) / Math.pow(10, 9));

                Run.printTriples(decodeMap,joinedList, decodeMap.get(encodeMap.get(tripleResources[1])), decodeMap.get(encodeMap.get(tripleResources[2])),
                       decodeMap.get(encodeMap.get(tripleResources2[1])), decodeMap.get(encodeMap.get(tripleResources2[2])));
                finishAllTime = System.nanoTime();
                System.out.println("Time elapsed:" + (finishAllTime - startAllTime) / Math.pow(10, 9));

                System.out.println(subjectList.size());
                System.out.println(subjectList2.size());
                System.out.println(joinedList.size());
                System.out.println("Joined list: " + joinedList);

            }
        } catch (InvalidBTreeStateException e) {
            e.printStackTrace();
        }
    }

    private static String[] provideResources(String sparqlSub, String sparqlPred, String sparqlObj) {
        String[] arr = new String[3];
        arr[0] = sparqlSub.trim();
        arr[1] = sparqlPred.trim();
        arr[2] = sparqlObj.trim();
        return arr;
    }

    private static void printTriples(HashMap<Integer, String> decodeMap, LinkedList<Integer> joinedList, String predicate1, String object1, String predicate2, String object2) {
        if (!joinedList.isEmpty()) {
            for (Integer subject : joinedList) {
                System.out.println("Triples found : " + decodeMap.get(subject) + " " + predicate1 + " " + object1 + " and " + decodeMap.get(subject) + " " + predicate2 + " " + object2);
            }
        }
    }

    private static void printTriples(HashMap<Integer, String> decodeMap, LinkedList<Integer> tokens, int constant) {
        if (!tokens.isEmpty()) {
            for (int i = 0; i < tokens.size(); i++) {
                System.out.println("Triples found: " + decodeMap.get(tokens.get(i)) + " " + decodeMap.get(tokens.get(++i)) + " " + decodeMap.get(constant));
            }
        }
    }

    private static HashMap<String, LinkedList<String>> extractTriple(String sparqlStr) {
        HashMap<String, LinkedList<String>> triple = new HashMap<>();
        try {
            org.apache.jena.query.Query query = QueryFactory.create(sparqlStr);

            final LinkedList<String> subjects = new LinkedList<>();
            final LinkedList<String> preds = new LinkedList<>();
            final LinkedList<String> objs = new LinkedList<>();
            List<TriplePath> l = new ArrayList<>();

            ElementWalker.walk(query.getQueryPattern(),
                    new ElementVisitorBase() {
                        public void visit(ElementPathBlock el) {
                            Iterator<TriplePath> triples = el.patternElts();
                            TriplePath triple;
                            l.addAll(el.getPattern().getList());
                            while (triples.hasNext()) {
                                triple = triples.next();
                                subjects.add(triple.getSubject().toString());
                                preds.add(triple.getPredicate().toString());
                                if (triple.getObject().isLiteral()) {
                                    objs.add(triple.getObject().getLiteralValue().toString());
                                } else {
                                    objs.add(triple.getObject().toString());
                                }
                            }
                        }
                    }
            );
            System.out.println("Triplets " + l);
            System.out.println("S " + subjects.toString());
            System.out.println("P " + preds.toString());
            System.out.println("O " + objs.toString());
            if (!subjects.isEmpty())
                triple.put("subjects", subjects);
            if (!preds.isEmpty())
                triple.put("preds", preds);
            if (!objs.isEmpty())
                triple.put("objs", objs);
        } catch (QueryException er) {
            System.out.println("---INVALID QUERY SYNTAX---");
        }
        return triple;
    }

    private static HashMap<Integer, String> getDecodeHashMap() {
        return ReadFile.getDictionaryDecodeMap();
    }

    private static HashMap<String, Integer> getEncodeHashMap() {
        return ReadFile.getDictionaryEncodeMap();
    }
}

/* SO_P Example
                LinkedList<Integer> placesValuesFound = indexSO_P.searchInPermutationTree("1", "3", "SO_P");
                LinkedList<Integer> predicateList = Index.querySO_P(placesValuesFound, indexSO_P);
                System.out.println(predicateList);
                placesValuesFound.clear();

                placesValuesFound = indexSO_P.searchInPermutationTree("125", "12", "SO_P");
                LinkedList<Integer> predicateList2 = Index.querySO_P(placesValuesFound, indexSO_P);
                System.out.println(predicateList2);
                placesValuesFound.clear();

                LinkedList<Integer> joinedList = Index.join(predicateList,predicateList2);
                System.out.println(joinedList);
*/


 /* SP_O Example

                LinkedList<Integer> placesValuesFound = indexSP_O.searchInPermutationTree("26403", "273", "SP_O");
                LinkedList<Integer> predicateList = Index.querySP_O(placesValuesFound, indexSP_O);
                System.out.println(predicateList);
                placesValuesFound.clear();

                placesValuesFound = indexSO_P.searchInPermutationTree("26406", "273", "SP_O");
                LinkedList<Integer> predicateList2 = Index.querySP_O(placesValuesFound, indexSP_O);
                System.out.println(predicateList2);
                placesValuesFound.clear();

                LinkedList<Integer> joinedList = Index.join(predicateList,predicateList2);
                System.out.println(joinedList);

                System.out.println("--------");
                System.out.println(encodeMap.get("288"));
                System.out.println(decodeMap.get(2166));
                System.out.println(encodeMap.get("2166"));
                System.out.println(decodeMap.get(14));
                System.out.println(encodeMap.get("14"));
                System.out.println("--------");
*/

 /*
                System.out.println(tripleResources[0] + " " + tripleResources[1] + " " + tripleResources[2]);
                System.out.println(encodeMap.get(tripleResources[0]) + " " + encodeMap.get(tripleResources[1]) + " " + encodeMap.get(tripleResources[2]));//Input encoding
                System.out.println(decodeMap.get(encodeMap.get(tripleResources[0])) + " " + decodeMap.get(encodeMap.get(tripleResources[1])) + " " + decodeMap.get(encodeMap.get(tripleResources[2])));
  */