public class CalculParallele {
    private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;
    private static Calcule[] threads = new Calcule[DONNEES.length];

    public static void main(String[] args) {
        long debut = System.nanoTime();
        System.out.println("=== Calcul Parallèle ===");
        
        for (int i = 0; i < DONNEES.length; i++) {
            threads[i] = new Calcule(DONNEES[i], MULTIPLICATEUR, i);
            threads[i].start();
        }

        try {
            for (int i = 0; i < DONNEES.length; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            System.err.println("Thread principal interrompu");
            Thread.currentThread().interrupt();
        }

        // Collecte des résultats
        long sommeTotal = 0;
        for (int i = 0; i < DONNEES.length; i++) {
            sommeTotal += threads[i].getResultat();
        }

        long duree = (System.nanoTime() - debut) / 1_000_000;
        System.out.println("Résultat total : " + sommeTotal);
        System.out.println("Durée : " + duree + " ms");
    }
}