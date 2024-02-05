package com.example.demo.exporter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LogReaderService {

    public void readAndProcessErrorLogs(String logFilePath) {
        try {
            List<String> logLines = FileUtils.readLines(new File(logFilePath), StandardCharsets.UTF_8);

            for (String logLine : logLines) {
                if (containsError(logLine)) {
                    processErrorLog(logLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception selon les besoins
        }
    }
    public void watchAndProcessErrorLogs(String logFilePath) {
        readAndProcessErrorLogs(logFilePath);
        File logFile = new File(logFilePath);

        TailerListenerAdapter listener = new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                if (containsError(line)) {
                    processErrorLog(line);
                }
            }
        };

        Tailer tailer = Tailer.create(logFile, listener, 1000, true);

        System.out.println("En attente de modifications dans le fichier de logs...");

        // Note: Le programme ne se bloquera pas ici, il continuera à s'exécuter normalement.

        // À un moment donné, lorsque vous souhaitez arrêter la surveillance (par exemple, lorsque votre application se termine),
        // appelez la méthode stop de Tailer :
        // tailer.stop();
    }

    private boolean containsError(String logLine) {
        // Logique pour vérifier si la ligne contient une erreur
        return logLine.toLowerCase().contains(" 404 ");
    }

    private void processErrorLog(String errorLogLine) {
        // Logique pour traiter la ligne d'erreur
        System.out.println("Error Log Line: " + errorLogLine);
    }
}


