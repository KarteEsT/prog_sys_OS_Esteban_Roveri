public class TestMutex {
    private static final int NOMBRE_REPETITIONS = 50;
    private static final int NOMBRE_INCREMENTS = 1111111;
    private static Thread[] tableauThreads = new Thread[NOMBRE_REPETITIONS];

    public static void main(String[] args) throws InterruptedException {
        
        // Réinitialisation du compteur
        CompteurSecurise.resetCompteur();
        
        // Création des threads
        for (int i = 0; i < NOMBRE_REPETITIONS; i++) {
            tableauThreads[i] = new Thread(
                new CompteurSecurise.Incrementeur("Thread " + i, NOMBRE_INCREMENTS)
            );
        }

        // Démarrage des threads
        for (int i = 0; i < NOMBRE_REPETITIONS; i++) {
            tableauThreads[i].start();
        }

        // Attente de la fin de tous les threads
        for (int i = 0; i < NOMBRE_REPETITIONS; i++) {
            tableauThreads[i].join();

        }

        // Affichage des résultats
        long resultatAttendu = (long)NOMBRE_REPETITIONS * NOMBRE_INCREMENTS;
        int resultatObtenu = CompteurSecurise.getCompteur();
        
        System.out.println("Résultat attendu : " + resultatAttendu);
        System.out.println("Résultat obtenu  : " + resultatObtenu);
        System.out.println("Différence       : " + (resultatAttendu - resultatObtenu));
    }
}