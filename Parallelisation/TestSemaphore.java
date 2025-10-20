public class TestSemaphore {

    public static void main(String[] args) {
        ProducteurConsommateur pc = new ProducteurConsommateur();

        Thread producteur1 = new Thread(new Producteur(pc));
        Thread producteur2 = new Thread(new Producteur(pc));
        Thread consommateur1 = new Thread(new Consommateur(pc));
        Thread consommateur2 = new Thread(new Consommateur(pc));

        System.out.println("Scénario équilibré");
        producteur1.start();
        consommateur1.start();
        
    }
}