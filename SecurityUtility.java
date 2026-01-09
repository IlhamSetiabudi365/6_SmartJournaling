public class SecurityUtility {
    private static final int SHIFT_KEY = 67;

    public static String encrypt(String password){
        StringBuilder encrypted = new StringBuilder();
        for (char character : password.toCharArray()) {
            char encryptedChar = (char) (character + SHIFT_KEY);
            encrypted.append(encryptedChar);
        }
        return encrypted.toString();
    }
}
