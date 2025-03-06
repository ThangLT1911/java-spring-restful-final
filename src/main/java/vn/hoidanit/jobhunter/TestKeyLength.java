package vn.hoidanit.jobhunter;

import java.util.Base64;

public class TestKeyLength {
    public static void main(String[] args) {
        String secretKey = "l+epkZnJ8+IxCmX7O9mz0V4IQ0IKUIm/YB83yCArrjNAaNpYw75hwrmngVkyrT8+VwILpDwhVt4VuTgCsaBkig==";

        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        System.out.println("Key length (bytes): " + decodedKey.length);
    }
}
