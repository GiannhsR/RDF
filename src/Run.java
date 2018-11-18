import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.util.InvalidBTreeStateException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Run {

    public static void initialize(boolean recreateIndex) throws IOException, UnknownIndexException {
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
                //Encode-Decode example
                ReadFile.reconstructHashMapFromDictionary();
                HashMap<String, Integer> encodeMap = Run.getEncodeHashMap();
                HashMap<Integer, String> decodeMap = Run.getDecodeHashMap();

                String[] arr = Run.sampleQuery();
                System.out.println(arr[0] + "" + arr[1] + "" + arr[2]);
                System.out.println(encodeMap.get(arr[0]) + "" + encodeMap.get(arr[1]) + "" + encodeMap.get(arr[2]));//Input encoding
                System.out.println(decodeMap.get(encodeMap.get(arr[0])) + "" + decodeMap.get(encodeMap.get(arr[1])) + "" + decodeMap.get(encodeMap.get(arr[2])));

                ArrayList<String> arrayList = Index.reconstructArrayList("OP_S");
                //Encode-Decode example

                //Join example
                LinkedList<Integer> placesValuesFound = indexOP_S.searchInPermutationTree(encodeMap.get(arr[2]).toString(), encodeMap.get(arr[1]).toString(), "OP_S");
                startTime = System.nanoTime();
                LinkedList<Integer> subjectList = Index.queryOP_S(placesValuesFound, indexOP_S);
                stopTime = System.nanoTime();
                System.out.println("First list created : " + (stopTime - startTime) / Math.pow(10, 9));
                placesValuesFound.clear();

                placesValuesFound = indexOP_S.searchInPermutationTree("14", "288", "OP_S");
                startTime = System.nanoTime();
                LinkedList<Integer> subjectList2 = Index.queryOP_S(placesValuesFound, indexOP_S);
                stopTime = System.nanoTime();
                System.out.println("Second list created : " + (stopTime - startTime) / Math.pow(10, 9));
                placesValuesFound.clear();

                startTime = System.nanoTime();
                LinkedList<Integer> joinedList = Index.generalJoin(subjectList, subjectList2);
                stopTime = System.nanoTime();
                System.out.println("Join list created : " + (stopTime - startTime) / Math.pow(10, 9));
                System.out.println(joinedList);
                //Join example

                Run.printTriples(joinedList,  encodeMap.get(arr[1]).toString(),encodeMap.get(arr[2]).toString(), "288", "14");

                placesValuesFound = indexOP_S.searchInPermutationTree("80", "288", "OP_S");
                LinkedList<Integer> subjectList3 = Index.queryOP_S(placesValuesFound, indexOP_S);
                placesValuesFound.clear();
                System.out.println(subjectList3);

                joinedList = Index.generalJoin(joinedList, subjectList3);
                System.out.println(joinedList);
            }
        } catch (InvalidBTreeStateException e) {
            e.printStackTrace();
        }
    }
    //288 -> <http://schema.org/eligibleRegion>
    //12 --> <http://db.uwaterloo.ca/~galuc/wsdbm/Country1>
    private static String[] sampleQuery() {
        System.out.println("Enter resources:");
        String userSubject = "";
        String userPredicate = "";
        String userObject = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Subject:" );
            userSubject = br.readLine();
            System.out.println("Predicate:" );
            userPredicate = br.readLine();
            System.out.println("Object:" );
            userObject = br.readLine();
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        String[] arr = new String[3];
        arr[0] = userSubject;
        arr[1] = userPredicate;
        arr[2] = userObject;
        //arr[0] = "<http://db.uwaterloo.ca/~galuc/wsdbm/City0>";
        //arr[1] = "<http://www.geonames.org/ontology#parentCountry>";
        //arr[2] = "<http://db.uwaterloo.ca/~galuc/wsdbm/Country6>";
        return arr;
    }

    private static void printTriples(LinkedList<Integer> joinedList, String predicate1, String object1, String predicate2, String object2) {
        for (Integer subject : joinedList) {
            System.out.println("Triples found : " + subject + " " + predicate1 + " " + object1 + " and " + subject + " " + predicate2 + " " + object2);
        }
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

                LinkedList<Integer> joinedList = Index.generalJoin(predicateList,predicateList2);
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

                LinkedList<Integer> joinedList = Index.generalJoin(predicateList,predicateList2);
                System.out.println(joinedList);
*/