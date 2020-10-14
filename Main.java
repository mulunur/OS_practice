/*
    Картамышева Таисия Николаевна
    БСБО-05-19
    Задание для 2 практики. Расшифровка 3-х захэшированных паролей методом подбора,
    с использованием функционала многопоточности.
 */
package com.company;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {

    static HashSet<String> hashPasswords;
    static HashSet<String> answers;
    static ArrayList<Thread> threads;

    public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
        hashPasswords = new HashSet<>();
        answers = new HashSet<>();
        threads = new ArrayList<>();
        hashPasswords.add("1115dd800feaacefdf481f1f9070374a2a81e27880f187396db67958b207cbad");
        hashPasswords.add("3a7bd3e2360a3d29eea436fcfb7e44c735d117c42d1c1835420b6b9942dd4f1b");
        hashPasswords.add("74e1bb62f8dabb8125a58852b63bdf6eaef667cb56ac7f7cdba6d7305c50a22f");
        for (char i = 'a'; i < 'z'; i += +2) {
            threads.add(new MyThreads(i, (char) (i + 2)));
            threads.get(threads.size() - 1).start();
        }
        while (threads.size() != 0) {
            threads.removeIf(Thread -> !Thread.isAlive());
        }

        System.out.println("Ответы: " + answers.size());
        for (String var : answers) {
            System.out.println(var);
        }


        //SolveProblem('a', 'z');
    }

    public static void SolveProblem(char bound1, char bound2) throws NoSuchAlgorithmException {
        System.out.println(bound1 + " " + bound2);
        MessageDigest messageDigest;
        messageDigest = MessageDigest.getInstance("SHA-256");
        char[] password = new char[5];
        for (char i = bound1; i <= bound2; i++) {
            password[0] = i;
            for (char j = 'a'; j <= 'z'; j++) {
                password[1] = j;
                for (char k = 'a'; k <= 'z'; k++) {
                    password[2] = k;
                    for (char l = 'a'; l <= 'z'; l++) {
                        password[3] = l;
                        for (char m = 'a'; m <= 'z'; m++) {
                            password[4] = m;
                            try {
                                byte[] hashInBytes = messageDigest.digest(new String(password).getBytes(StandardCharsets.UTF_8));
                                if (hashPasswords.contains(bytesToHex_(hashInBytes))) {
                                    System.out.println(new String(password));
                                    answers.add(new String(password));
                                }
                            } catch (Exception e) {
                                System.out.println(e);
                            }

                        }
                    }
                }
            }
        }
    }

    public static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHex_(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte var : bytes) {
            stringBuilder.append(String.format("%02x", var));
        }
        return stringBuilder.toString();
    }
}

class MyThreads extends Thread {
    char bound1, bound2;

    public MyThreads(char bound1, char bound2) {
        this.bound1 = bound1;
        if (bound2 <= 'z') {
            this.bound2 = bound2;
        } else {
            this.bound2 = 'z';
        }
    }

    public void run() {
        System.out.println("Поток создан");
        try {
            Main.SolveProblem(bound1, bound2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}