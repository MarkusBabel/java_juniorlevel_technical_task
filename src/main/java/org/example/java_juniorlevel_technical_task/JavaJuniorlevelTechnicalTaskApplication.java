package org.example.java_juniorlevel_technical_task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class JavaJuniorlevelTechnicalTaskApplication {

    @Autowired
    private JobsTechstarsService jobsTechstarsService;

    public static void main(String[] args) {
        SpringApplication.run(JavaJuniorlevelTechnicalTaskApplication.class, args);
    }

    @PostConstruct
    public void fetchDataAndPrint() {
        jobsTechstarsService.fetchAndPrintJobsDataWithDescriptionAndFilteredLaborFunctionFromUrl();

        jobsTechstarsService.createCSVFileFromDatabase();
    }
}

