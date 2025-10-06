import java.io.*;

public class Main {

    public static void main (String[] args) {
        System.out.println("Test étape N°1");
        ProcessController proc1 = new ProcessController();

        String[] param = {"/c", "echo", "Hello World"};

        try {
            proc1.executeSimple("cmd", param);
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Test étape N°2");
        ProcessController proc2 = new ProcessController();
        File outputFile = new File("outputFile.txt");
        File error = new File("error.txt");


        try {
            proc2.executeWithRedirection("cmd", outputFile, error, param);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'exécution avec redirection : " + e.getMessage());
            e.printStackTrace();
        }
    }
}