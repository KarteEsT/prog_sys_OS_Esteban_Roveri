public class Calcule extends Thread {
    // TODO: Déclarer les attributs nécessaires
    private int valeur;
    private int multiplicateur;
    private int index;
    private long resultat;

    public Calcule(int valeur, int multiplicateur, int index) {
        this.valeur = valeur;
        this.multiplicateur = multiplicateur;
        this.index = index;
        this.resultat = 0;
    }

    @Override
    public void run() {
        long temp = 0;
        for (int j = 0; j < multiplicateur; j++) {
            temp += valeur * valeur + valeur;
        }
        resultat = temp;
    }

    public long getResultat() {
        return resultat;
    }
}