import java.io.FileWriter;
import java.io.IOException;

public class FirstPPM {
    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("FirstPPM.ppm");

            writer.write("P3\n");
            // Écriture des dimensions
            writer.write("3 2\n");
            // Écriture de la valeur maximal
            writer.write("255\n");
            // Écriture des pixels
            writer.write("255 0 0 0 255 0 0 0 255\n");
            // Première ligne : rouge, vert, bleu
             writer.write("255 255 0 255 255 255 0 0 0\n");
            // Deuxième ligne : jaune, blanc, noir

            writer.close(); // Fermeture du fichier

            System.out.println("Image PPM créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}