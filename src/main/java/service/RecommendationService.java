package service;

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
        List<String> files = fileService.getFileNames(consoleService.getReadDirectory());
        List<String> packages = Arrays.asList(consoleService.getImportantPackages().split(", "));
        Map<String, List<Class>> functionalities = featureService.convertFilesToFeatures(files, packages);
        Map<String, List<Column>> similarityMap = similarityService.createSimilarityMap(functionalities);
        Map<String, List<Class>> completeFunctionalities = featureService.convertFilesToFeatures(files);
        return microservice.groupFeaturesBySimilarity(similarityMap, completeFunctionalities,
                consoleService.getSimilarityValue());

    }

    public void printMicrosservices(List<Microservice> recommendations) {
        consoleService.printMicrosservices(recommendations);
    }
}
