import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException, UnknownIndexException{
        final String path = "watdiv10M\\benchmark _17MB.nt";
        ReadFile rf = new ReadFile();
        //rf.read(path);
        Run.initialize(false);
    }
}