public class Main {
    public static void main(String[] args) throws Exception {
        String input = null;
        SeamCarver carver = null;
        String output = null;
        int remove;
        input = args[0];
        output = args[1];
        remove = Integer.parseInt(args[2]);
        carver = new SeamCarver(input);
        carver.carveImage(remove);
        carver.saveImage(output, "png");
    }
}
// javac *.java
// java Main in.png out.png 200