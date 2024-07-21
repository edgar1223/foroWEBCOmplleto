package com.foroweb.foroweb.service;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ScheduledTaskService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void start() {
        scheduler.scheduleAtFixedRate(this::runPythonScript, 0, 1, TimeUnit.MINUTES);
    }

    private void runPythonScript() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "/home/edgar-david/Documentos/foroweb1/.venv/bin/python",
                    "/home/edgar-david/Documentos/foroweb1/entrenamineto.py"
            );
            processBuilder.environment().put("VIRTUAL_ENV", "/home/edgar-david/Documentos/foroweb1/.venv");
            processBuilder.environment().put("PATH", "/home/edgar-david/Documentos/foroweb1/.venv/bin:" + System.getenv("PATH"));

            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Script executed successfully");
            } else {
                System.err.println("Script execution failed with exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
