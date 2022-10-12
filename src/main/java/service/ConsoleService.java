package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Microservice;

import java.util.List;
import java.util.Scanner;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsoleService {

    private String readDirectory;
    private String importantPackages;
    private Integer similarityValue;


    public void setInputData() {
        Scanner input = new Scanner(System.in);

        System.out.println("Enter the path of zip file: ");
        //readDirectory = input.nextLine();
        readDirectory = "C:\\Users\\carlo\\Documents\\projetos\\sugestao_microsservico\\src\\main\\resources\\arquivos.zip";

        System.out.println("Enter the packages: ");
        //importantPackages = input.nextLine();
        importantPackages = "com.unisinos.sistema.adapter.inbound.controller, com.unisinos.sistema.application.service, com.unisinos.sistema.adapter.outbound.repository";

        System.out.println("Enter the similarity value: ");
        //similarityValue = Integer.parseInt(input.nextLine());
        similarityValue = 90;
    }

    public void printMicrosservices(List<Microservice> microsservicos) {

        System.out.println("Results:");
        microsservicos.forEach(microservice -> {
            try {
                String json = new ObjectMapper().writeValueAsString(microservice);
                System.out.println(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

}
