## Objective: ##
Read a Monolithic API and recommend microservices

## Requirements: ##
- Java 11;
- Maven;
- IDE (In this project was used Intellij IDEA);

## Steps to run the program: ##
1. Clone the project: **git clone https://github.com/CarlosFernandoXavier/API-Slicer.git**;
2. Execute the command: **mvn clean install**;
3. Execute the APISlicerTest class;
4. Provide a zip path of your execution traces
   - Example: C:\Users\carlos\Documents\projetos\sugestao_microsservico\src\main\resources\arquivos.zip
5. Provide a package names you want to consider;
   - Example: com.unisinos.sistema.adapter.inbound.controller, com.unisinos.sistema.application.service, com.unisinos.sistema.adapter.outbound.repository
6. Provide the similarity value;
   - Example: 90

