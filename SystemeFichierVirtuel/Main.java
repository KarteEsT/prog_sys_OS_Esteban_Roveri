public class Main {
    public static void main(String[] args) {
        System.out.println("=== Initialisation du système de fichiers virtuel ===");
        VirtualFileSystem vfs = new VirtualFileSystem();

        try {
            // Afficher le layout initial
            FileSystemUtils.dumpMemoryLayout(vfs);
            FileSystemUtils.displayStats(vfs);

            // Test 1 : Création de fichiers
            System.out.println("\n--- Test 1 : Création de fichiers ---");
            vfs.createFile("/", "readme.txt");
            vfs.createFile("/", "test.dat");
            vfs.createFile("/", "config.conf");

            // Test 2 : Écriture de données
            System.out.println("\n--- Test 2 : Écriture de données ---");
            String contenu1 = "Ceci est un fichier de test pour notre système de fichiers virtuel.\n";
            contenu1 += "Toutes les données sont stockées dans 1Mo de mémoire.\n";
            vfs.writeFile("/", "readme.txt", contenu1.getBytes());

            // Créer un fichier plus volumineux
            StringBuilder bigContent = new StringBuilder();
            for (int i = 0; i < 50; i++)
                bigContent.append("Ligne ").append(i).append(" - Test de données répétées.\n");
            vfs.writeFile("/", "test.dat", bigContent.toString().getBytes());

            // Test 3 : Lecture de fichiers
            System.out.println("\n--- Test 3 : Lecture de fichiers ---");
            byte[] data = vfs.readFile("/", "readme.txt");

            if (data != null) {
                System.out.println("Contenu lu (" + data.length + " octets) :");
                System.out.println(new String(data));
            }

            // Test 4 : Statistiques finales
            System.out.println("\n--- Test 4 : Statistiques finales ---");
            FileSystemUtils.displayStats(vfs);

            // Test 5 : Stress test - créer beaucoup de petits fichiers
            System.out.println("\n--- Test 5 : Stress test ---");
            int successCount = 0;

            for (int i = 0; i < 100; i++) {
                String filename = "file" + i + ".tmp";
                String content = "Contenu du fichier " + i;

                if (vfs.createFile("/", filename) && 
                    vfs.writeFile("/", filename, content.getBytes())) {
                    successCount++;
                } else {
                    System.out.println("Échec création fichier " + i + " (limite atteinte)");
                    break;
                }
            }
            System.out.println("Fichiers créés avec succès : " + successCount);

            // Statistiques finales
            FileSystemUtils.displayStats(vfs);

        } catch (Exception e) {
            System.err.println("Erreur pendant les tests : " + e.getMessage());
            e.printStackTrace();
        }
    }
}