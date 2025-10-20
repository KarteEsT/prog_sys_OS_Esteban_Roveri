import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

public class ProducteurConsommateur {

    private static final int TAILLE_BUFFER = 5;
    private final Integer[] buffer = new Integer[TAILLE_BUFFER];
    private int indexProduction;
    private int indexConsommation;

    private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
    private final Semaphore elementsDisponibles = new Semaphore(0);
    private final Semaphore mutexBuffer = new Semaphore(1);

}

class Producteur implements Runnable {
    public void run() {
        for (int i = 0; i < 10; i++) {
            int produit = genererProduit();

            // Attendre une place libre
            placesLibres.acquire();
            // Acquérir l'accès exclusif au buffer  
            mutexBuffer.acquire();
            // Ajouter l'élément au buffer
            buffer[indexProduction] = produit;
            indexProduction = (indexProduction+1) % TAILLE_BUFFER;
            // Libérer l'accès au buffer
            mutexBuffer.release();
            // Signaler qu'un élément est disponible
            elementsDisponibles.release();
        }
    }
}

class Consommateur implements Runnable {
    public void run() {
        for (int i = 0; i < 7; i++) {
            // Attendre qu'un élément soit disponible
            elementsDisponibles.acquire();
            // Acquérir l'accès exclusif au buffer  
            mutexBuffer.acquire();
            // Retirer un élément du buffer
            Integer produit = buffer.poll();
            // Libérer l'accès au buffer
            mutexBuffer.release();
            // Signaler qu'une place est libre
            placesLibres.realease();
        }
    }
}