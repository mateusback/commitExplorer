package br.edu.ifpr.commitexplorer.CommitExplorer.crosscutting.security;

public interface EncryptionService {
    String encrypt(String data);
    String decrypt(String encryptedData);
}