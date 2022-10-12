package service;

import model.Class;
import model.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimilarityService {

    public Map<String, List<Column>> createSimilarityMap(Map<String, List<Class>> functionalities) {

        Map<String, List<Column>> similarityMap = new HashMap<>();

        functionalities.forEach((functionality1, classes1) -> {
            List<Column> colunas = new ArrayList<>();
            functionalities.forEach((functionality2, classes2) -> {
                if (!functionality1.equals(functionality2)) {
                    List<Class> classesIguais = intersection(classes1, classes2);
                    Double similarity = Double.valueOf(classesIguais.size() * (0.1 * classesIguais.size())) /
                            Double.valueOf(classes1.size() * (0.1 * classes1.size())) * 100;
                    colunas.add(new Column(functionality2, similarity));
                }
            });
            similarityMap.put(functionality1, colunas);
        });
        return similarityMap;
    }

    private List<Class> intersection(List<Class> classesF1, List<Class> classesF2) {
        List<Class> classesSimilares = new ArrayList<>();
        for (int contador1 = 0; contador1 < classesF1.size(); contador1++) {
            for (int contador2 = 0; contador2 < classesF2.size(); contador2++) {
                if (classesF1.get(contador1).getClassName().equals(classesF2.get(contador2).getClassName())) {
                    if (classesSimilares.isEmpty()) {
                        classesSimilares.add(classesF1.get(contador1));
                    } else if (!classeJaExistente(classesSimilares, classesF1.get(contador1))) {
                        classesSimilares.add(classesF1.get(contador1));
                    }
                }
            }
        }
        return classesSimilares;
    }

    private static boolean classeJaExistente(List<Class> classes, Class classeNova) {
        return classes.stream()
                .filter(classe ->
                        classe.getClassName().equals(classeNova.getClassName()))
                .findFirst()
                .isPresent();
    }
}
