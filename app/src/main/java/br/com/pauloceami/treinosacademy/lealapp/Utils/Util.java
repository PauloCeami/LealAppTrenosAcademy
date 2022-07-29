package br.com.pauloceami.treinosacademy.lealapp.Utils;

import android.content.Context;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Util {
    public static final String TREINO_SERIALIZABLE = "TREINO";
    public static final String EXERCICIO_SERIALIZABLE = "EXERCICIO";
    public static final String TAG_LEALAPPS = "LEAL_APPS";
    public static final String FOLDERNAME_STORAGE = "imagens";


    public static String getStringHash(Context ctx, String str) {
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(ctx, "Não foi possivel gerar hash " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        byte hash[] = new byte[0];
        try {
            hash = algorithm.digest(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(ctx, "Não foi possivel gerar hash " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        StringBuilder texto = new StringBuilder();
        for (byte b : hash) {
            texto.append(String.format("%02X", 0xFF & b));
        }

        String[] nome = texto.toString().split(" ");
        Random rand = new Random();
        Integer id = rand.nextInt(1000000000);
        String nomes = nome[0].replace(" ", "") + id.toString();

        return nomes.toString();
    }
}
