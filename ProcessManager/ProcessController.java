import java.io.*;

/**
 * Gestionnaire principal pour le lancement et le contrôle de processus externes.
 * Cette classe encapsule les fonctionnalités de ProcessBuilder en offrant
 * une interface simplifiée pour la gestion des processus.
 */
public class ProcessController {

    public static final int DEFAULT_TIMEOUT_SECONDS = 30;

    // Variables d'instance
    private ProcessBuilder processBuilder;
    private Process currentProcess;

    public ProcessController() {
        this.processBuilder = new ProcessBuilder();
        this.currentProcess = null;
    }

    /**
     * Lance un processus simple avec une commande et ses arguments.
     * Cette méthode constitue le point d'entrée basique pour l'exécution
     * de programmes externes.
     *
     * @param command commande à exécuter (ex: "ls", "python3", "notepad.exe")
     * @param args arguments optionnels de la commande
     * @return le processus lancé
     * @throws IOException si le lancement échoue
     */
    public Process executeSimple(String command, String[] args) throws IOException {
        // Créer un tableau pour stocker la commande complète
        String[] fullCommand;

        // Si args est null, fullCommand = tableau avec juste command
        if (args == null) {
        	fullCommand = new String[1];
        	fullCommand[0] = command;
        // Sinon, fullCommand = tableau avec command + tous les args
        } else {
        	fullCommand = new String[args.length + 1];
            fullCommand[0] = command;
            System.arraycopy(args, 0, fullCommand, 1, args.length);
        }
        
        // Configurer le ProcessBuilder avec fullCommand
        processBuilder.command(fullCommand);

        // Lancer le processus avec processBuilder.start()
        currentProcess = processBuilder.start();

        System.out.println("Lancement de : " + command);
        return currentProcess;
    }

    /**
     * Lance un processus avec redirection des flux vers des fichiers.
     * Permet de capturer facilement les sorties standard et d'erreur.
     *
     * @param command commande à exécuter
     * @param outputFile fichier pour la sortie standard (null = pas de redirection)
     * @param errorFile fichier pour la sortie d'erreur (null = pas de redirection)  
     * @param args arguments de la commande
     * @return le processus configuré et lancé
     * @throws IOException si la configuration ou le lancement échoue
     */
    public Process executeWithRedirection(String command, File outputFile, 
                                        File errorFile, String[] args) throws IOException {

        // Utiliser executeSimple pour lancer le processus de base
        Process process = executeSimple(command, args);

        // Si outputFile n'est pas null, configurer la redirection
        if (outputFile != null) {
            processBuilder.redirectOutput(outputFile);
        }

        // Si errorFile n'est pas null, configurer la redirection d'erreur
        if (errorFile != null) {
            processBuilder.redirectError(errorFile);
        }

        System.out.println("Redirection configurée - Sortie: " + outputFile + ", Erreur: " + errorFile);

        // Relancer le processus avec les redirections
        currentProcess = processBuilder.start();
        return currentProcess;
    }

    /**
     * Lance un processus interactif permettant l'envoi de données via l'entrée standard
     * et la lecture temps réel des sorties.
     *
     * @param command commande à lancer
     * @param args arguments
     * @return le processus interactif
     * @throws IOException si le lancement échoue
     */
    public Process executeInteractive(String command, String[] args) throws IOException {
        // Utiliser executeSimple pour lancer le processus
        Process proc = executeSimple(command, args);
        // (Les flux restent accessibles par défaut)

        System.out.println("Mode interactif activé pour : " + command);

        return proc;
    }

    /**
     * Attend la fin d'exécution d'un processus avec un timeout optionnel.
     * Retourne le code de sortie.
     *
     * @param process processus à attendre
     * @param timeoutSeconds délai maximum d'attente (0 = pas de timeout)
     * @return code de sortie du processus (-1 si timeout)
     * @throws InterruptedException si l'attente est interrompue
     */
    public int waitForProcess(Process process, int timeoutSeconds) throws InterruptedException {

        if (timeoutSeconds <= 0) {
            // Attendre indéfiniment avec process.waitFor()
            process.waitFor();
            return process.exitValue();
        } else {
            // Utiliser process.waitFor(timeoutSeconds, java.util.concurrent.TimeUnit.SECONDS)
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            // Si le processus se termine dans les temps, retourner process.exitValue()
            if (finished) {
                return process.exitValue();
            } else {
                // Sinon, appeler process.destroyForcibly() et retourner -1
                process.destroyForcibly();
                return -1;
            }
        }
    }

    /**
     * Envoie des données à l'entrée standard d'un processus interactif.
     */
    public void sendInput(Process process, String input) throws IOException {
        // Obtenir l'OutputStream du processus
        OutputStream outputStream = process.getOutpuStream();

        if (outputStream != null) {
            // Écrire les données + retour à la ligne
            outputStream.write(input + "\n");
            // Appeler flush() pour forcer l'envoi
            outputStream.flush();
        }

        System.out.println("Envoi vers le processus : " + input);
    }

    /**
     * Lit la sortie standard d'un processus de manière non-bloquante.
     */
    public String readOutput(Process process) throws IOException {
        // Obtenir l'InputStream du processus
        InputStream inputStream = process.getInputStream();

        if (inputStream != null) {
            // Vérifier s'il y a des données avec inputStream.available()
            if (inputStream.available() > 0) {
                // Si oui, les lire et les retourner comme String
                byte[] buffer = new byte[inputStream.available()];
                int byteRead = inputStream.readBuffer();
                return new String(buffer, 0, bytesRead);
            }
            
        }
        return "";
    }

    // Getters
    public Process getCurrentProcess() { 
        return currentProcess; 
    }
}