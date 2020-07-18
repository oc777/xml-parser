public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("--- XML parser ---");

        String inputFile = "input.txt";
        XMLParser p = new XMLParser();
        p.parse(inputFile);
    }
}
