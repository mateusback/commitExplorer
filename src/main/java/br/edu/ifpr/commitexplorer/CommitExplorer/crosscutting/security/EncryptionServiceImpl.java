package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    @Value("${security.encryption.key}")
    private String keyFromConfig;
    private SecretKeySpec secretKey;

    @PostConstruct
    public void init() {
        secretKey = new SecretKeySpec(keyFromConfig.getBytes(), "AES");
    }

    public String encrypt(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar dados", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar dados", e);
        }
    }
}