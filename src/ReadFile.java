import java.io.*;
import java.util.*;

/**
 * Reads a file
 * Splits text into tokens
 * Removes dots
 * Encodes its triples and writes them in another file @param encodedFilePath
 * Decodes the file and writes in another file @param decodedFilePath
 *
 * @author Ioannis Rakitzopoulos 2017
 */
public class ReadFile {
    private final int TRIPLE_RECREATED = 3;
    private static final String dictionaryFilePath = "watdiv10M\\dictionary.txt";
    private final String path_encoded_uri = "ENCODED_URI";
    private BufferedWriter writerEncoder = new BufferedWriter(new FileWriter(path_encoded_uri, true));

    private int index = 1;//working as a starting point for the DICTIONARY
    private int counter = 0;//checks if a triple is recreated

    private String[] tokens;//stores the reference of the tokens created from the splitting

    private static HashMap<String, Integer> dictionaryEncodeMap = new HashMap<>();

    private static HashMap<Integer, String> dictionaryDecodeMap = new HashMap<>();

    public ReadFile() throws IOException {
    }

    /**
     * Reads the file from given path
     * Creates tokens for each line
     * Stores Subject,Predicate,Object into HashMap
     *
     * @param path path of the file to be read
     * @throws IOException file may be missing
     */
    public void read(String path) throws IOException {
        System.out.println("Reading file...\n");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                tokens = preprocess(line);
                for (int i = 0; i < tokens.length; i++) {
                    if (insertIntoEncodingMap(tokens[i], index)) {
                        index++;
                    }
                }
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        } finally {
            closeWriterEncoder(writerEncoder);
            System.out.println("\nClosing input stream...\n");
            reader.close();
            System.out.println("------------------\n");
            createFiles();
            clearHashMaps();
        }
    }

    /**
     * Replaces "." with ""
     * in a given string
     *
     * @param line is the line that was read last
     * @return String without dots
     */
    private String removeDotFromString(String line) {
        line = line.substring(0, line.length() - 1);
        return line;
    }

    /**
     * Break up a string into its basic tokens
     *
     * @param line is the line that was read last
     * @return the tokens stored in a String array
     */
    private String[] preprocess(String line) {
        String[] result = {""};
        try {
            line = removeDotFromString(line);
            result = line.split("\\s");
        } catch (IndexOutOfBoundsException e) {
            System.err.format("IOException: %s%n", e);
        } finally {
            return result;
        }
    }

    /**
     * Try to insert a (S key , I value ) pair
     * If the key doesn't exist, create the mapping,write in the file and return true, mapping succeeded
     * Else write in the file and return false, mapping already exists
     *
     * @param key   the token to be stored
     * @param value the index of the mapping
     * @return true on success, false on already mapped pair
     */
    private boolean insertIntoEncodingMap(String key, int value) throws IOException {
        if (dictionaryEncodeMap.get(key) == null) {
            dictionaryEncodeMap.put(key, value);
            dictionaryDecodeMap.put(value, key);
            createEncodedFile(dictionaryEncodeMap.get(key));
        } else {
            createEncodedFile(dictionaryEncodeMap.get(key));
            return false;
        }
        return true;
    }

    /**
     * Creates a string dictionary on specified path
     * by iterating over the dictionaryEncodeMap
     *
     * @param dictionaryFilePath path of the dictionary
     */
    private void createDictionary(String dictionaryFilePath) {
        File file;

        System.out.println("Creating dictionary...\n");
        try {
            file = new File(dictionaryFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            for (Object o : dictionaryEncodeMap.entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                String key = String.valueOf(pair.getKey());
                String value = String.valueOf(pair.getValue());
                bw.write(key.concat("=").concat(value).concat("\n"));
            }
            bw.close();
            System.out.println("--------------\n");
        } catch (IOException x) {
            System.out.println(x);
        }
    }

    /**
     * Gets the id from the dictionaryEncodeMap
     * and writes it in the file
     *
     * @param id to be written on file
     * @throws IOException
     */
    private void createEncodedFile(int id) throws IOException {
        writerEncoder.write(String.valueOf(id) + " ");
        counter++;
        if (counter == TRIPLE_RECREATED) {
            writerEncoder.write("\n");
            counter = 0;
        }
        writerEncoder.flush();
    }

    /**
     * Decode the original file using the encoded_uri file and the dictionary.
     * Read every line of the encoded file.
     * for each line split its content and store it into a String[]
     * for each token stored,search in the dictionary to find its corresponding mapping
     * Write the mapping to the decoded file path.
     */
    private void createDecodedFile() {
        System.out.println("Decoding file...\n");
        String path_decoded_uri = "DECODED_URI";
        int counter = 0;
        BufferedReader encodedReader;
        BufferedWriter decodedWriter;
        BufferedReader dictionaryReader;
        String[] encodedTokensString;
        try {
            encodedReader = new BufferedReader(new FileReader(path_encoded_uri));
            decodedWriter = new BufferedWriter(new FileWriter(path_decoded_uri));
            String encodedLine;
            String dictionaryLine;
            String encodedToken;
            String startingURI;
            while ((encodedLine = encodedReader.readLine()) != null) {
                encodedTokensString = encodedLine.split("\\s");
                for (int i = 0; i < encodedTokensString.length; i++) {
                    encodedToken = encodedTokensString[i];
                    dictionaryReader = new BufferedReader(new FileReader(dictionaryFilePath));
                    while ((dictionaryLine = dictionaryReader.readLine()) != null) {
                        if (dictionaryLine.contains(">=") || dictionaryLine.contains("\"=")) {
                            int indexOfDelimiter = dictionaryLine.indexOf("=");
                            int value = Integer.parseInt(dictionaryLine.substring(indexOfDelimiter + 1, dictionaryLine.length()));
                            if (Integer.valueOf(encodedToken) == value) {
                                startingURI = dictionaryLine.substring(0, indexOfDelimiter);
                                decodedWriter.write(startingURI + " ");
                                counter++;
                                if (counter == TRIPLE_RECREATED) {
                                    decodedWriter.write("." + "\n");
                                    counter = 0;
                                }
                            }
                        }
                        decodedWriter.flush();
                    }
                    dictionaryReader.close();
                }
            }
            encodedReader.close();
            decodedWriter.close();
            System.out.println("------------\n");
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * Reconstruct the HashMaps using the dictionary.
     * Read each line of the dictionary then create the mappings:
     * <http://..> --> index
     */
    static void reconstructHashMapFromDictionary() {
        System.out.println("Reconstructing HashMap...\n");
        BufferedReader reader;
        String line;
        try {
            String startingURI;
            int index;
            reader = new BufferedReader(new FileReader(dictionaryFilePath));
            while ((line = reader.readLine()) != null) {
                if (line.contains(">=") || line.contains("\"=")) {
                    int indexOfDelimiter = line.indexOf("=");
                    startingURI = line.substring(0, indexOfDelimiter);
                    index = Integer.parseInt(line.substring(indexOfDelimiter + 1, line.length()));
                    dictionaryEncodeMap.put(startingURI, index);
                    dictionaryDecodeMap.put(index, startingURI);
                }
            }
            reader.close();
            System.out.println("HashMaps reconstructing finished...\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFiles() {
        createDictionary(dictionaryFilePath);
        //createDecodedFile();
    }

    /**
     * Closes the global scope buffered writer
     */
    private void closeWriterEncoder(BufferedWriter bw) throws IOException {
        bw.close();
    }

    private void clearHashMaps() {
        dictionaryDecodeMap.clear();
        dictionaryEncodeMap.clear();
    }

    public static HashMap<String, Integer> getDictionaryEncodeMap() {
        return dictionaryEncodeMap;
    }

    public static HashMap<Integer, String> getDictionaryDecodeMap() {
        return dictionaryDecodeMap;
    }
}