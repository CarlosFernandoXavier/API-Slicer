package service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import model.Class;
import model.Column;
import model.Microservice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RecommendationService {
    private ConsoleService consoleService;
    private FileService fileService;
    private FeatureService featureService;
    private SimilarityService similarityService;
    private MicroserviceService microservice;

    public RecommendationService() {
        consoleService = new ConsoleService();
        fileService = new FileService();
        featureService = new FeatureService();
        similarityService = new SimilarityService();
        microservice = new MicroserviceService();
    }

    public List<Microservice> getRecommendations() {

        consoleService.setInputData();
        List<String> arquivos = fileService.buscarNomeArquivos(consoleService.getReadDirectory());
        List<String> packages = Arrays.asList(consoleService.getImportantPackages().split(", "));

        Map<String, List<Class>> functionalities = featureService.convertFilesToFeatures(arquivos, packages);
        Map<String, List<Column>> similarityTable = similarityService.createSimilarityMap(functionalities);
        Map<String, List<Class>> functionalities2 = featureService.convertFilesToFeatures(arquivos);
        return microservice.groupFunctionalitiesBySimilatiry(similarityTable, functionalities2,
                consoleService.getSimilarityValue());

    }

    public void printMicrosservices(List<Microservice> recommendations) {
        consoleService.printMicrosservices(recommendations);
    }
}
