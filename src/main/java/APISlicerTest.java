import model.Microservice;
import service.RecommendationService;

import java.util.List;

public class APISlicerTest {

    public static void main(String[] args) {
        RecommendationService recommendationService = new RecommendationService();
        List<Microservice> microsservicos = recommendationService.getRecommendations();
        recommendationService.printMicrosservices(microsservicos);
    }
}
