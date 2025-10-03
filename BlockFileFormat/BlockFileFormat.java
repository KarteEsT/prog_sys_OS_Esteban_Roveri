import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Format de fichier avec structure par blocs de 1Ko
 * Header + Métadonnées + Données organisées en blocs avec pointeurs
 */
public class BlockFileFormat {

    // Constantes
    public static final int BLOCK_SIZE = 1024; // 1Ko par bloc
    public static final int MAGIC_NUMBER = 0x424C4B46;
    public static final int CURRENT_VERSION = 1;

    // En-tête: magic, version, sectionPointers
    public static class Header {
        public int magic;
        public int version;
        public int[] sectionPointers;

        public Header() {
            this.magic = MAGIC_NUMBER;
            this.version = CURRENT_VERSION;
            this.sectionPointers = new int[0];
        }

        /**
         * Désérialise l'en-tête (Header) d'un fichier BLKF à partir d'un ByteBuffer.
         *
         * Le buffer contient :
         * - un entier magique (magic) pour identifier le format
         * - un entier de version
         * - un entier indiquant le nombre de sections
         * - une liste de pointeurs (un par section)
         *
         * @param buffer tampon contenant les données brutes de l'en-tête
         */
        public void deserialize(ByteBuffer buffer) {
        	int magic = buffer.getInt(); 
        	int version = buffer.getInt();
        	int count = buffer.getInt();

        	sectionPointers = new int[count];

            // Parcourir le nombre de sections et lire leurs pointeurs
            for (int i = 0; i < count; i++) {
            	sectionPointers[i] = buffer.getInt();
            }
        }


        public boolean isValid() {
            return magic == MAGIC_NUMBER && version == CURRENT_VERSION;
        }
    }

    // Section: 4 bytes type, 1 byte padding, 250 bytes filename, 1 byte padding, remaining int pointers
    public static class Section {
        public int type;
        public String filename;
        public int[] dataPointers; // pointeurs de blocs de données
        public Block[] dataBlocks; // non sérialisé, contenu en mémoire

        public Section() {
            this.type = 0;
            this.filename = "";
            this.dataPointers = new int[0];
            this.dataBlocks = new Block[0];
        }

        public Section(int type, String filename) {
            this.type = type;
            this.filename = filename == null ? "" : filename;
            this.dataPointers = new int[0];
            this.dataBlocks = new Block[0];
        }

        /**
         * Désérialise une section à partir d'un tampon binaire (ByteBuffer).
         * 
         * Le buffer contient :
         * - un entier représentant le type de la section
         * - du padding (octet(s) de remplissage)
         * - un nom de fichier codé sur 250 octets (UTF-8, complété par des \0)
         * - encore du padding
         * - une liste de pointeurs vers les blocs de données (terminée par un 0 si non utilisé)
         *
         * @param buffer tampon contenant les données brutes de la section
         */
        public void deserialize(ByteBuffer buffer) {
            //! TODO Lire le type de la section (int)
            int type = buffer.getInt(); 
        	
            //! TODO Lire le padding (1 octet, à ignorer)
            int padding = buffer.getInt();

            //! TODO Lire les 250 octets du nom de la section
            int nomSection = buffer.getInt();

            //! TODO Convertir ces octets en String avec StandardCharsets.UTF_8
            String rawName = new String(nomSection, StandardCharsets.UTF_8);
            
            //! TODO Nettoyer la chaîne (enlever les caractères nuls et espaces inutiles)
            String cleanName = rawName.replace("\0", "").trim();

            //! TODO Lire encore 1 octet de padding

            // Lire la suite du buffer comme une liste d'entiers (pointeurs de blocs)
            int remainingInts = buffer.remaining() / 4;
            int[] tmp = new int[remainingInts];
            int count = 0;

            for (int i = 0; i < remainingInts; i++) {
                //! TODO Lire un entier (pointeur)
                remainingInts.getInt();

                //! TODO Si le pointeur vaut 0, arrêter la boucle (0 = emplacement non utilisé)
                //! TODO Sinon, stocker le pointeur dans le tableau temporaire
                
            }

            //! TODO Copier les pointeurs valides dans dataPointers (taille = count)
        }
    }

    // Bloc de données
    public static class Block {
        public byte[] data;

        public Block() {
            this.data = new byte[BLOCK_SIZE];
        }

        public Block(byte[] src) {
            this();
            int len = Math.min(src.length, BLOCK_SIZE);
            System.arraycopy(src, 0, data, 0, len);
        }

        public byte[] getData(int offset, int length) {
            int copyLength = Math.min(length, BLOCK_SIZE - offset);
            return Arrays.copyOfRange(data, offset, offset + copyLength);
        }
    }

    // Gestionnaire principal
    private Header header;
    private Section[] sections;

    public BlockFileFormat() {
        this.header = new Header();
        this.sections = new Section[0];
    }

    /**
     * Lit un fichier binaire au format BLKF (BlockFileFormat) depuis le disque et
     * reconstruit en mémoire son en-tête, ses sections et les blocs de données référencés.
     *
     * Le fichier est organisé en blocs fixes de taille BLOCK_SIZE. L'en-tête contient
     * des pointeurs vers les blocs décrivant les sections; chaque section référence
     * à son tour les blocs de données qui lui appartiennent.
     *
     * @param filename chemin du fichier .blk à lire
     * @return une instance de BlockFileFormat entièrement peuplée
     * @throws IOException si le fichier n'existe pas, est inaccessible ou n'est pas valide
     */
    public static BlockFileFormat readFromFile(String filename) throws IOException {
        RandomAccessFile file = null;
        file = new RandomAccessFile(filename, "r");
        Header header = null; // TODO lire l'en-tête du fichier
        file.read(header);
        header.deserialize(buffer);

        // Vérifier si le header est valide (appel à header.isValid())
        // -> si ce n'est pas le cas, lever une exception IOException avec un message explicite
        if (header.isValid()) {
        	BlockFileFormat format = new BlockFileFormat();
            format.header = header;
            format.sections = new Section[header.sectionPointers.length];

            // Parcourir tous les pointeurs de sections indiqués dans le header :
            for (int sectionPointer : header.sectionPointers) {
                byte[] sectionData = new byte[BLOCK_SIZE];

                //! TODO Positionner le curseur du fichier à la bonne position
                
                
                //! TODO Lire les données de la section dans le tableau
                sectionData[sectionPointer] = sectionData.read();

                ByteBuffer buffer = ByteBuffer.wrap(sectionData);
                Section section = new Section();
                section.deserialize(buffer);

                // Pour chaque pointeur de bloc de données dans la section :
                for (int i = 0; i < section.dataPointers.length; i++) {
                    byte[] blockData = new byte[BLOCK_SIZE];
                    //! TODO Positionner le curseur au bon endroit dans le fichier (pointeur * BLOCK_SIZE)
                    
                    //! TODO Lire le bloc de données
                    section.dataBlocks[i] = new Block(blockData);
                }
            }
        } else {
        	throw new IOException ("Format de fichier invalide");
        }
        return format;
    }

    /**
     * Lit et désérialise l'en-tête situé dans le premier bloc du fichier.
     *
     * @param file fichier ouvert en lecture
     * @return l'en-tête peuplé
     * @throws IOException en cas d'erreur d'E/S
     */
    private static Header readHeader(RandomAccessFile file) throws IOException {
        byte[] headerData = new byte[BLOCK_SIZE];
        //! TODO Lire l'en-tête du fichier dans le tableau
        ByteBuffer buffer = ByteBuffer.wrap(headerData);
        Header header = new Header();
        //! TODO Désérialiser l'en-tête
        header.deserialize(buffer);
        return header;
    }

    public Header getHeader() { return header; }
    public Section[] getSections() { return sections; }

    @Override
    public String toString() {
        int totalBlocks = 0;
        for (Section s : sections) totalBlocks += s.dataBlocks.length;
        return String.format("BlockFileFormat{sections=%d, dataBlocks=%d}", sections.length, totalBlocks);
    }
}