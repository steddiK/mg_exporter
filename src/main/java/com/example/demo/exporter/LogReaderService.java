package com.example.demo.exporter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service
public class LogReaderService {
    //les metriques sont de type counter
    private final Counter Erreur400;
    private final Counter Erreur401;
    private final Counter Erreur403;
    private final Counter Erreur404;
    private final Counter Erreur500;
    private final List<String> typeError= Arrays.asList(" 400 "," 401 "," 403 "," 404 "," 500 ");

    public LogReaderService(PrometheusMeterRegistry registry) {
        //Initialisation des métriques, et création
        this.Erreur400 = Counter.builder("error_http_400")
                .description("Erreur_400")
                .register(registry);
        this.Erreur401 = Counter.builder("error_http_401")
                .description("Erreur_401")
                .register(registry);
        this.Erreur403 = Counter.builder("error_http_403")
                .description("Erreur_403")
                .register(registry);
        this.Erreur404 = Counter.builder("error_http_404")
                .description("Erreur_404")
                .register(registry);
        this.Erreur500 = Counter.builder("error_http_500")
                .description("Erreur_500")
                .register(registry);
    }

    public void readAndProcessErrorLogs(String logFilePath) {
        //Lire les lignes déjà existant dans la page log
        //nécessaire pour bien compter le nombre d'erreur survenu
        try {
            List<String> logLines = FileUtils.readLines(new File(logFilePath), StandardCharsets.UTF_8);

            for (String logLine : logLines) {
                for (String s : typeError) {
                    if (containsError(logLine, s)) {
                        processErrorLog(logLine, s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void watchAndProcessErrorLogs(String logFilePath) {
        //recupere les erreur déja dans le fichier log.
        readAndProcessErrorLogs(logFilePath);

        //vision en temps réel du changement du fichier et analyse des nouvelles lignes
        File logFile = new File(logFilePath);
        TailerListenerAdapter listener = new TailerListenerAdapter() {
            @Override
            public void handle(String line) {
                for (String s : typeError) {
                    if (containsError(line, s)) {
                        processErrorLog(line, s);
                    }
                }
            }
        };

        // tailer c'est pour voir en temps réel le changement ou modification du fichier
        Tailer tailer = Tailer.create(logFile, listener, 1000, true);
        System.out.println("En attente de modifications dans le fichier de logs...");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean containsError(String logLine, String errorStatus) {
        //fonction qui teste l'existence du String errorStatus dans la ligne logLine
        return logLine.toLowerCase().contains(errorStatus);
    }

    private void processErrorLog(String errorLogLine,String errorStatus) {
        // Logique pour traiter la ligne d'erreur
        if(errorStatus.equals(" 404 ")){
            this.Erreur404.increment();
        }
        if(errorStatus.equals(" 401 ")){
            this.Erreur401.increment();
            //debug manuelle
            System.out.println("Erreur detecté: " + errorLogLine);
        }
        if(errorStatus.equals(" 400 ")){
            this.Erreur400.increment();
        }
        if(errorStatus.equals(" 403 ")){
            this.Erreur403.increment();
        }
        if(errorStatus.equals(" 500 ")){
            this.Erreur500.increment();
        }
    }
}