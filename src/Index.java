import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTree;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.util.InvalidBTreeStateException;

import java.io.*;
import java.util.*;

public class Index extends BPlusTree {

    private final static String mappingSP_OFilePath = "indexMappings\\SP_O_mapping";
    private final static String mappingOP_SFilePath = "indexMappings\\OP_S_mapping";
    private final static String mappingSO_PFilePath = "indexMappings\\SO_P_mapping";
    private int genericCounter = 0; //Used as a counter for inserted pairs in the pairToInt map

    /**
     * pairToInt map contains keys in the form of "int.int" and values int
     * Eg "1.2" -> 3
     * This is because the insertKey method must have int as ids
     * The problem of non-allowed duplicate pairs is solved with the genericArrayList
     */
    private static HashMap<String, Integer> pairToInt = new HashMap<>();
    private static HashMap<Integer, String> intToPair = new HashMap<>();

    /**
     * Stores (String,Integer) pairs
     * genericArrayList stores every pair ("int.int" -> int)
     * Then it's used to create a file containing every mapping
     * thus solving the duplicate key problem arising from the HashMaps
     */
    private ArrayList<String> genericArrayList = new ArrayList<>();

    public Index() throws IOException, InvalidBTreeStateException {
        super();
    }

    /**
     * Overloaded constructor for maximum customization
     *
     * @param conf         conf B+ Tree configuration instance
     * @param mode         I/O mode
     * @param treeFilePath file path for the file
     * @param bPerf        performance counter class
     * @throws IOException                is thrown when we fail to open/create the binary tree file
     * @throws InvalidBTreeStateException
     */
    public Index(BPlusConfiguration conf,
                 String mode, String treeFilePath,
                 BPlusTreePerformanceCounter bPerf) throws IOException, InvalidBTreeStateException {
        super(conf, mode, treeFilePath, bPerf);
    }

    /**
     * Creates an index based on the name
     *
     * @param name index to be created
     */
    public void createIndex(String name) throws UnknownIndexException {
        switch (name) {
            case "SPO":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "SOP":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "POS":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "PSO":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "OSP":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "OPS":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "SP_O":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "OP_S":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            case "SO_P":
                System.out.println("Valid index name: " + name);
                readEncodedFileToGenerateIndex(name);
                break;
            default:
                throw new UnknownIndexException();
        }
    }

    private void readEncodedFileToGenerateIndex(String name) {
        String path_encoded_uri = "ENCODED_URI";
        BufferedReader reader;
        String[] tokens;//Stores lines from a file , line contains integers e.g 1 2 3
        int[] integerTokens = new int[3];//stores integers from each line
        try {
            String line;
            reader = new BufferedReader(new FileReader(path_encoded_uri));
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                tokens = line.split("\\s");
                for (int i = 0; i < tokens.length; i++) {
                    integerTokens[i] = Integer.parseInt(tokens[i]);
                }
                switch (name) {
                    case "SPO":
                        populateSPO(integerTokens);
                        break;
                    case "SOP":
                        populateSOP(integerTokens);
                        break;
                    case "POS":
                        populatePOS(integerTokens);
                        break;
                    case "PSO":
                        populatePSO(integerTokens);
                        break;
                    case "OSP":
                        populateOSP(integerTokens);
                        break;
                    case "OPS":
                        populateOPS(integerTokens);
                        break;
                    case "SP_O":
                        populateSP_O(integerTokens);
                        break;
                    case "OP_S":
                        populateOP_S(integerTokens);
                        break;
                    case "SO_P":
                        populateSO_P(integerTokens);
                        break;
                    default:
                        System.out.print("Unknown index name.");
                        break;
                }
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * Populates the index that has as keys "S"
     * and as values "PO"
     *
     * @param arr an array that contains
     *            subject as 1st arg
     *            predicate as 2nd arg
     *            object as 3rd arg
     */
    private void populateSPO(int[] arr) {
        String predicate = String.valueOf(arr[1]);
        String object = String.valueOf(arr[2]);
        String po = predicate.concat(" ").concat(object);
        po = po.trim();
        try {
            this.insertKey(arr[0], po, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO Documentation
     *
     * @param arr
     */
    private void populateSOP(int[] arr) {
        String predicate = String.valueOf(arr[1]);
        String object = String.valueOf(arr[2]);
        String op = object.concat(" ").concat(predicate);
        op = op.trim();
        try {
            this.insertKey(arr[0], op, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate the index that has as keys "P"
     * and as values "OS"
     *
     * @param arr an array that contains
     *            subject as 1st arg
     *            predicate as 2nd arg
     *            object as 3rd arg
     */
    private void populatePOS(int[] arr) {
        String object = String.valueOf(arr[2]);
        String subject = String.valueOf(arr[0]);
        String os = object.concat(" ").concat(subject);
        os = os.trim();
        try {
            this.insertKey(arr[1], os, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO Documentation
     *
     * @param arr
     */
    private void populatePSO(int[] arr) {
        String object = String.valueOf(arr[2]);
        String subject = String.valueOf(arr[0]);
        String so = subject.concat(" ").concat(object);
        so = so.trim();
        try {
            this.insertKey(arr[1], so, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * Populate the index that has as keys "O"
     * and as values "SP"
     *
     * @param arr an array that contains
     *            subject as 1st arg
     *            predicate as 2nd arg
     *            object as 3rd arg
     */
    private void populateOSP(int[] arr) {
        String subject = String.valueOf(arr[0]);
        String predicate = String.valueOf(arr[1]);
        String sp = subject.concat(" ").concat(predicate);
        sp = sp.trim();
        try {
            this.insertKey(arr[2], sp, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO Documentation
     *
     * @param arr
     */
    private void populateOPS(int[] arr) {
        String subject = String.valueOf(arr[0]);
        String predicate = String.valueOf(arr[1]);
        String ps = predicate.concat(" ").concat(subject);
        ps = ps.trim();
        try {
            this.insertKey(arr[2], ps, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate the index that has as keys "SP"
     * and as values "O"
     * Also store all the mapped (Key,Value) pairs into an ArrayList
     * so we can support duplicate keys
     *
     * @param arr an array that contains
     *            subject as 1st arg
     *            predicate as 2nd arg
     *            object as 3rd arg
     */
    @SuppressWarnings("Duplicates")
    private void populateSP_O(int[] arr) {
        String subject = String.valueOf(arr[0]);
        String predicate = String.valueOf(arr[1]);
        String object = String.valueOf(arr[2]);
        String sp = subject.concat(".").concat(predicate);

        pairToInt.put(sp, genericCounter);
        intToPair.put(genericCounter, sp);
        genericArrayList.add(intToPair.put(genericCounter, sp).concat("%").concat(String.valueOf(genericCounter).concat("\n")));
        genericCounter++;
        try {
            this.insertKey(pairToInt.get(sp), object, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate the index that has as keys "OP"
     * and as values "S"
     * Also store all the mapped (Key,Value) pairs into an ArrayList
     * so we can support duplicate keys
     *
     * @param arr an array that contains
     *            subject as 1st arg
     *            predicate as 2nd arg
     *            object as 3rd arg
     */
    @SuppressWarnings("Duplicates")
    private void populateOP_S(int[] arr) {
        String subject = String.valueOf(arr[0]);
        String predicate = String.valueOf(arr[1]);
        String object = String.valueOf(arr[2]);
        String op = object.concat(".").concat(predicate);

        pairToInt.put(op, genericCounter);
        intToPair.put(genericCounter, op);
        genericArrayList.add(intToPair.put(genericCounter, op).concat("%").concat(String.valueOf(genericCounter).concat("\n")));
        genericCounter++;
        try {
            this.insertKey(pairToInt.get(op), subject, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populate the index that has as keys "SO"
     * and as values "P"
     * Also store all the mapped (Key,Value) pairs into an ArrayList
     * so we can support duplicate keys
     *
     * @param arr an array that contains
     *            subject as 1st arg
     *            predicate as 2nd arg
     *            object as 3rd arg
     */
    @SuppressWarnings("Duplicates")
    private void populateSO_P(int[] arr) {
        String subject = String.valueOf(arr[0]);
        String predicate = String.valueOf(arr[1]);
        String object = String.valueOf(arr[2]);
        String so = subject.concat(".").concat(object);

        pairToInt.put(so, genericCounter);
        intToPair.put(genericCounter, so);
        //populateHashMap(so);
        genericArrayList.add(intToPair.put(genericCounter, so).concat("%").concat(String.valueOf(genericCounter).concat("\n")));
        genericCounter++;
        try {
            this.insertKey(pairToInt.get(so), predicate, false);
        } catch (IOException | InvalidBTreeStateException | IllegalStateException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used only for searching in one of the permutation trees
     * Converts the user input into the actual stored way
     * Extracts the value from the (Key,Value) mappings
     * Returns all the occurrences of the searchParameter1+searchParameter2
     * (the mapping happens in the corresponding populateXX_X method)
     *
     * @param searchParameter1 ,encoded URI #1 user wants to search for
     * @param searchParameter2 ,encoded URI #2 user wants to search for
     * @param mappingPath      ,the path of the file that the mapping pairs are stored
     * @return LinkedList<Integer> containing all the places that the searchParameter1 + searchParameter2 are found together
     * so we can iterate the list and return the actual values (before the mapping) that the user searches for
     */
    @SuppressWarnings("Duplicates")
    public LinkedList<Integer> searchInPermutationTree(String searchParameter1, String searchParameter2, String mappingPath) throws UnknownIndexException {
        LinkedList<Integer> valueList = new LinkedList<>();
        searchParameter1 = searchParameter1.trim();
        searchParameter2 = searchParameter2.trim();
        String modifiedParameter1 = searchParameter1.concat(".").concat(searchParameter2).concat("%").concat("\\d+");
        String modifiedParameter2 = searchParameter2.concat(".").concat(searchParameter1).concat("%").concat("\\d+");
        switch (mappingPath) {
            case "SP_O":
                mappingPath = mappingSP_OFilePath;
                break;
            case "OP_S":
                mappingPath = mappingOP_SFilePath;
                break;
            case "SO_P":
                mappingPath = mappingSO_PFilePath;
                break;
            default:
                throw new UnknownIndexException();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(mappingPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.matches(modifiedParameter1) || line.matches(modifiedParameter2)) {
                    int indexOfDelimiter = line.indexOf("%");
                    int value = Integer.parseInt(line.substring(indexOfDelimiter + 1, line.length()));
                    valueList.add(value);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueList;
    }

    /**
     * Create the files that are needed
     * for the mapping for each permutation
     * Closes the connection to the buff.reader
     * clears hash maps
     * clears array list
     */
    @SuppressWarnings("Duplicates")
    public void createMappingFiles(String path) throws UnknownIndexException {
        File file;
        switch (path) {
            case "SP_O":
                path = mappingSP_OFilePath;
                break;
            case "OP_S":
                path = mappingOP_SFilePath;
                break;
            case "SO_P":
                path = mappingSO_PFilePath;
                break;
            default:
                throw new UnknownIndexException();
        }
        try {
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (String s : genericArrayList) {
                bw.write(s);
            }
            bw.close();
            genericCounter = 0;
            clearHashMaps();
            clearArrayList();
            System.out.println("--------------\n");
        } catch (IOException x) {
            System.out.println(x);
        }
    }

    /**
     * Input: [1 3       ,5 12    ,22 99     ,...]
     * Store each returned value as a string in a string array
     * remove whitespaces from every string
     * split the string at the whitespace and create two integer tokens e.g "35 47" ---> "35" "47"
     *
     * @return integerTokenList, a list containing all the values as integers
     * Output:[1,3,5,12,22,99,....]
     * Used on SPO,POS,OSP indexes
     */
    public LinkedList<Integer> preprocessBasicIndex(LinkedList<String> keyValues) {
        LinkedList<Integer> integerTokenList = new LinkedList<>();
        if (keyValues.isEmpty()) {
            System.out.print("Returned 0 values.\n");
            return integerTokenList;
        } else {
            String[] strArr = new String[keyValues.size()];
            String[] strTokens;
            for (int i = 0; i < keyValues.size(); i++) {
                strArr[i] = keyValues.get(i);
                String token = strArr[i].trim();
                strTokens = token.split("\\s");
                integerTokenList.add(Integer.valueOf(strTokens[0].trim()));
                integerTokenList.add(Integer.valueOf(strTokens[1].trim()));
            }
            return integerTokenList;
        }
    }

    /**
     * Input: [1       ,5    ,22     ,...]
     * Store each returned value as a string
     * remove whitespaces from every string
     * get the integer value of the string
     * add it to a list
     *
     * @return integerTokenList, a list containing all the values as integers
     * Output:[1,3,5,12,22,....]
     */
    public LinkedList<Integer> preprocessPermIndex(List<Integer> searchValues) throws InvalidBTreeStateException, IOException {
        LinkedList<Integer> integerTokenList = new LinkedList<>();
        if (searchValues.isEmpty()) {
            System.out.println("Returned 0 values.\n");
            return integerTokenList;
        } else {
            System.out.println("Processing input...\n");
            for (int i : searchValues) {
                String token = this.searchKey(i, false).getValues().toString().replace("[", "").replace("]", "").trim();
                int intToken = Integer.valueOf(token);
                integerTokenList.add(intToken);
            }
            return integerTokenList;
        }
    }

    /**
     * Input: [1       ],[2      ]...
     * Store each returned value as an integer
     * remove whitespaces from every string
     * get the integer value of the string
     *
     * @return an integer
     * Output: 1,2....
     */
    public static int myPreprop(LinkedList<String> val) {
        int intToken = -999;
        if (val.isEmpty()) {
            System.out.println("Returned 0 values.\n");
            return intToken;
        } else {
            //System.out.println("Processing input...\n");
            for (String token : val) {
                token = val.toString().replace("[", "").replace("]", "").trim();
                intToken = Integer.valueOf(token);
            }
            return intToken;
        }
    }

    /**
     * Join algorithm
     * For each element in the first list
     * check if it is contained in the second list
     * if yes add the element to the join array,it means both triples exist
     *
     * @param list1 ,contains the values from the first pair to be joined
     * @param list2 ,contains the values from the second pair to be joined
     * @return a linked list containing the joined integer elements
     */
    static LinkedList<Integer> generalJoin(LinkedList<Integer> list1, LinkedList<Integer> list2) {
        LinkedList<Integer> joinList = new LinkedList<>();
        if (!list1.isEmpty() && !list2.isEmpty()) {
            for (int i : list1) {
                if (list2.contains(i)) {
                    joinList.add(i);
                }
            }
            return joinList;
        } else {
            System.out.println("0 shared elements found.");
            return joinList;
        }
    }

    /**
     * Join algorithm that uses the Indexes to create the joined data structure
     * For each value in the treeValues list
     * search with that value as id in the "correct" (this) tree
     * if the returned values contain both the first and the second join param.
     * both triples exist,
     * add the search value to the list
     *
     * @param joinParam1 ,input parameter1 of the triple to be joined
     * @param joinParam2 ,input parameter2 of the triple to be joined
     * @param treeValues ,a list containing the values of the variable we are joining
     * @return a linked list containing the joined integer elements
     * @throws IOException                ''
     * @throws InvalidBTreeStateException ''
     */
    public LinkedList<Integer> starJoin(String joinParam1, String joinParam2,
                                        LinkedList<Integer> treeValues) throws IOException, InvalidBTreeStateException {
        LinkedList<Integer> joinList = new LinkedList<>();
        LinkedList<String> keyValues = null;
        LinkedList<Integer> basicTreesValues = null;
        for (int value : treeValues) {
            keyValues = this.searchKey(value, false).getValues();
            basicTreesValues = this.preprocessBasicIndex(keyValues);
            if (basicTreesValues.contains(Integer.valueOf(joinParam1)) && basicTreesValues.contains(Integer.valueOf(joinParam2))) {
                joinList.add(value);
            }
        }
        keyValues.clear();
        basicTreesValues.clear();
        return joinList;
    }

    static LinkedList<Integer> queryOP_S(LinkedList<Integer> placesValuesFound, Index indexOP_S) throws IOException, InvalidBTreeStateException {
        LinkedList<Integer> subjectList = new LinkedList<>();
        if (!placesValuesFound.isEmpty()) {
            for (int token : placesValuesFound) {
                subjectList.add(Index.myPreprop(indexOP_S.searchKey(token, false).getValues()));
            }
            return subjectList;
        } else {
            return subjectList;
        }
    }

    static LinkedList<Integer> querySO_P(LinkedList<Integer> placesValuesFound, Index indexSO_P) throws IOException, InvalidBTreeStateException {
        LinkedList<Integer> predicateList = new LinkedList<>();
        if (!placesValuesFound.isEmpty()) {
            for (int token : placesValuesFound) {
                predicateList.add(Index.myPreprop(indexSO_P.searchKey(token, false).getValues()));
            }
            return predicateList;
        } else {
            return predicateList;
        }
    }

    static LinkedList<Integer> querySP_O(LinkedList<Integer> placesValuesFound, Index indexSP_O) throws IOException, InvalidBTreeStateException {
        LinkedList<Integer> objectList = new LinkedList<>();
        if (!placesValuesFound.isEmpty()) {
            for (int token : placesValuesFound) {
                objectList.add(Index.myPreprop(indexSP_O.searchKey(token, false).getValues()));
            }
            return objectList;
        } else {
            return objectList;
        }
    }

    static ArrayList<String> reconstructArrayList(String path) throws UnknownIndexException {
        switch (path) {
            case "SP_O":
                path = mappingSP_OFilePath;
                break;
            case "OP_S":
                path = mappingOP_SFilePath;
                break;
            case "SO_P":
                path = mappingSO_PFilePath;
                break;
            default:
                throw new UnknownIndexException();
        }
        System.out.println("Reconstructing ArrayList...\n");
        BufferedReader reader;
        String line;
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            String values;
            int index;
            reader = new BufferedReader(new FileReader(path));
            while ((line = reader.readLine()) != null && line.contains("%")) {
                int indexOfDelimiter = line.indexOf("%");
                index = Integer.parseInt(line.substring(indexOfDelimiter + 1, line.length()));
                values = line.substring(0, indexOfDelimiter);
                arrayList.add(index, values);
            }
            reader.close();
            System.out.println("ArrayList reconstructing finished...\n");
            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    private void clearHashMaps() {
        pairToInt.clear();
        intToPair.clear();
    }

    private void clearArrayList() {
        genericArrayList.clear();
    }
}