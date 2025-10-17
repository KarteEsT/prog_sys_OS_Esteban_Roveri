public class Calcule extends Thread {
    private int valeur;
    private int multiplicateur;
    private int index;
    private long resultat;

    public Calcule(int valeur, int multiplicateur, int index) {
        this.valeur = valeur;
        this.multiplicateur = multiplicateur;
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println("Thread " + index + " démarré pour la valeur " + valeur);
        long temp = 0;
        for (int j = 0; j < multiplicateur; j++) {
            temp += valeur * valeur + valeur;
        }
        resultat = temp;
        System.out.println("Thread " + index + " terminé avec le résultat " + resultat + " ms");
    }

    public long getResultat() {
        return resultat;
    }
}