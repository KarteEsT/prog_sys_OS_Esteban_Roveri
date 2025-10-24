import java.net.*;
import java.io.*;

public class ServeurMulti {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur multi-clients démarré");

        // Boucle infinie pour accepter les connexions clientes
        while (true) {
            Socket client = serverSocket.accept();
            System.out.println("Nouveau client : " + client.getInetAddress());

            // Délégation à un thread dédié
            Thread t = new Thread(new ClientHandler(client));
            t.start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket client;

    public ClientHandler(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
         try {
            // Création des canaux input/output
            BufferedReader in = new BufferedReader(
                new InputStreamReader(client.getInputStream())
            );
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            // Boucle d'écho (lecture/écriture)
            String ligne;
            while ((ligne = in.readLine()) != null) {
                System.out.println(Thread.currentThread().getName() + " ] Reçu : " + client.getInetAddress() + ": " +
                    ligne);
                out.println("ECHO: " + ligne);
            }

         } catch (IOException erreur) {
             erreur.printStackTrace();
         } finally {
             try {
                // Fermeture de la connexion client
                System.out.println("Client déconnecté");
                client.close();
             } catch (IOException ignored) {}
         }
    }
}