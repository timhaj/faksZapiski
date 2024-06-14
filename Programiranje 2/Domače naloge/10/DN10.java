import java.io.FileInputStream;

public class DN10 {
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream(args[0]);
            byte[] header = new byte[8];
            fis.read(header);
            while (true) {
                byte[] lengthBytes = new byte[4];
                fis.read(lengthBytes);
                int length = Byte.toUnsignedInt(lengthBytes[0]) * (int) Math.pow(256,3) + Byte.toUnsignedInt(lengthBytes[1]) * (int) Math.pow(256,2) + Byte.toUnsignedInt(lengthBytes[2]) * 256 + Byte.toUnsignedInt(lengthBytes[3]);
                byte[] typeBytes = new byte[4];
                fis.read(typeBytes);
                String type = new String(typeBytes);
                fis.skip(length + 4);
                System.out.printf("Chunk: %s, length: %d\n", type, length);
                if (type.equals("IEND")) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Napaka pri branju datoteke.");
        }
    }
}