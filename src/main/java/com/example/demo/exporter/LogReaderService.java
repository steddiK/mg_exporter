package com.example.demo.exporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class LogReaderService {

    public void readAndProcessLogs(String logFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Analyser chaque ligne de log
                if (containsError(line,"401")) {
                    processErrorLog(line);
                }
                if (containsError(line,"402")) {
                    processErrorLog(line);
                }
                if (containsError(line,"403")) {
                    processErrorLog(line);
                }
                if (containsError(line,"404")) {
                    processErrorLog(line);
                }
                if (containsError(line,"500")) {
                    processErrorLog(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception selon les besoins
        }
    }
    private boolean containsError(String logLine, String stt) {
        // Utiliser une expression régulière ou une logique de filtre pour identifier les lignes d'erreur
        // Dans cet exemple, une simple vérification de la présence du mot "error" est effectuée.
        Pattern pattern = Pattern.compile("\\b"+stt+"\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(logLine);
        return matcher.find();
    }
    private void processErrorLog(String logLine) {
        // Ajouter ici la logique pour analyser chaque ligne de log

        System.out.println("Log Line: " + logLine);
    }
}
