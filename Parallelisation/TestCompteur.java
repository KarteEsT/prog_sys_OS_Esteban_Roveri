// /workspaces/prog_sys_Windows_Esteban_Roveri/Parallelisation/TestCompteur.java
public class TestCompteur {
    private static final int NOMBRE_REPETITIONS = 50;
    private static Thread[] tableauThreads = new Thread[NOMBRE_REPETITIONS];


    public static void main(String[] args) {
        for (int i=0; i<=9; i++) {
            CompteurDangereux.resetCompteur();
            tableauThreads[i] = new CompteurDangereux.Incrementeur("t" + 1, 1111111);
        }

        for (Thread thread : tableauThreads) {
            thread.start();
            thread.join();
        }

        System.out.println("Compteur final : " + CompteurDangereux.getCompteur());
    }
}