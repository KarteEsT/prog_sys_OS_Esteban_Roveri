import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;

public class Image {
    private int width;
    private int height;
    // pixels[y][x][0=R,1=G,2=B]
    private int[][][] pixels; // pixels[y][x][0=R,1=G,2=B]

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    /**
     * Constructeur : initialise une image vide.
     */
    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[height][width][3];
    }

    /**
     * Définit la couleur d'un pixel à la position (x, y)
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[y][x][0] = r;
            pixels[y][x][1] = g;
            pixels[y][x][2] = b;
        }
    }

    /**
     * Sauvegarde l'image au format texte PPM (P3)
     */
    /* public void save_txt(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
	
            writer.write("P3\n");
            // Écriture des dimensions
            writer.write(width + " " + height + "\n");
            // Écriture de la valeur maximal
            writer.write("255\n");

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int r = pixels[y][x][0];
					int g = pixels[y][x][1];
					int b = pixels[y][x][2];
					writer.write(r + " " + g + " " + b + " ");
				}
				writer.write("\n");
			}
					
            writer.close(); // Fermeture du fichier
    } */
	
	/* static public read_txt (String filename) throws IOException {
		
		byte[] data = fs.readAllBytes();
		String txt = new String (data, StandarCharset.UTF-8);
		int nbToken = 0;
		// TODO
		
	}	*/
  
	public void save_bin(String filename) throws IOException {
		  
		FileOutputStream writer = new FileOutputStream(filename);
		  
		writer.write("P6\n".getBytes());
		writer.write((width + " " + height + "\n").getBytes());
		writer.write("255\n".getBytes());
		  
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				writer.write((byte)pixels[y][x][0]);
				writer.write((byte)pixels[y][x][1]);
				writer.write((byte)pixels[y][x][2]);
			}
		}
		writer.close();
	}
}