package io.github.JoasFyllipe.service.usuario;

import jakarta.enterprise.context.ApplicationScoped;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;


@ApplicationScoped
public class HashServiceImpl implements HashService{

    private String salt = "@#1237zt";
    private Integer iterationCount = 403;
    private Integer keyLength = 512;

    @Override
    public String getHashSenha(String senha) {

        try{
            byte[] result = SecretKeyFactory.getInstance("PBKDF2withHmacSHA512")
                    .generateSecret(
                            new PBEKeySpec(senha.toCharArray(), salt.getBytes(), iterationCount, keyLength)
                    ).getEncoded();
            return Base64.getEncoder().encodeToString(result);
        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HashServiceImpl hash = new HashServiceImpl();
        try{
            System.out.println(hash.getHashSenha("123456"));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
