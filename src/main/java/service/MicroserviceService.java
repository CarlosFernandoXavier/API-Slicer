package service;

import model.*;
import model.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MicroserviceService {

    public List<Microservice> groupFunctionalitiesBySimilatiry(Map<String, List<Column>> similarityTable,
                                                                       Map<String, List<Class>> funcionalidadesMap,
                                                                       Integer similarityValue) {
        List<String> similarities = new ArrayList<>();
        List<Microservice> microservices = new ArrayList<>();

        similarityTable.forEach((row, columns) -> {
            String functionalities = row;
            List<Column> colunasFiltradas = columns.stream()
                    .filter(column -> column.getThreshold() >= similarityValue)
                    .collect(Collectors.toList());

            if (microservices.isEmpty()) {
                List<ClassResponse> classResponses = new ArrayList<>();
                gerarClassesParaMicrosservico(funcionalidadesMap, functionalities,
                        classResponses);

                for (Column colunaFiltrada : colunasFiltradas) {
                    gerarClassesParaMicrosservico(funcionalidadesMap, colunaFiltrada.getNomeFuncionalidade(),
                            classResponses);
                    functionalities += ", " + colunaFiltrada.getNomeFuncionalidade();
                }

                Microservice micro = new Microservice();
                micro.setId("model.Microservice " + (microservices.size() + 1));
                micro.setFunctionalities(functionalities);
                micro.setClasses(classResponses);
                microservices.add(micro);
                similarities.add(functionalities);
            } else {
                //PAra cada coluna, preciso ver se a funcionalidade já não existe em algum microsserviço
                Integer indiceMicrosservico1 = getIndiceMicrosservico(microservices, functionalities);
                Integer indiceMicrosservico2 = getIndiceMicrosservico(microservices, colunasFiltradas);

                if (Objects.isNull(indiceMicrosservico1) && Objects.isNull(indiceMicrosservico2)) {
                    List<ClassResponse> classResponses = new ArrayList<>();
                    gerarClassesParaMicrosservico(funcionalidadesMap, functionalities, classResponses);

                    for (Column colunaFiltrada : colunasFiltradas) {
                        gerarClassesParaMicrosservico(funcionalidadesMap, colunaFiltrada.getNomeFuncionalidade(),
                                classResponses);
                        functionalities += ", " + colunaFiltrada.getNomeFuncionalidade();
                    }
                    Microservice micro = new Microservice();
                    micro.setId("model.Microservice " + (microservices.size() + 1));
                    micro.setFunctionalities(functionalities);
                    micro.setClasses(classResponses);
                    microservices.add(micro);

                    similarities.add(functionalities);
                } else if (!Objects.isNull(indiceMicrosservico1) && Objects.isNull(indiceMicrosservico2)) {
                    Microservice micro = microservices.get(indiceMicrosservico1);
                    microservices.remove(micro);
                    List<ClassResponse> classResponses = micro.getClasses();

                    for (Column colunaFiltrada : colunasFiltradas) {
                        gerarClassesParaMicrosservico(funcionalidadesMap, colunaFiltrada.getNomeFuncionalidade(), classResponses);
                        if (!micro.getFunctionalities().contains(colunaFiltrada.getNomeFuncionalidade())) {
                            micro.setFunctionalities(micro.getFunctionalities() + ", " + colunaFiltrada.getNomeFuncionalidade());
                        }
                    }
                    micro.setClasses(classResponses);
                    microservices.add(indiceMicrosservico1, micro);

                } else if (Objects.isNull(indiceMicrosservico1) && !Objects.isNull(indiceMicrosservico2)) {
                    Microservice micro = microservices.get(indiceMicrosservico2);
                    microservices.remove(micro);
                    List<ClassResponse> classResponses = micro.getClasses();

                    gerarClassesParaMicrosservico(funcionalidadesMap, functionalities, classResponses);
                    micro.setFunctionalities(micro.getFunctionalities() + ", " + functionalities);
                    micro.setClasses(classResponses);
                    microservices.add(indiceMicrosservico2, micro);
                }
            }
        });
        return microservices;
    }

    private static void gerarClassesParaMicrosservico(Map<String, List<Class>> funcionalidadesMap,
                                                      String functionalities,
                                                      List<ClassResponse> classResponses) {

        List<Class> listaClasses = funcionalidadesMap.get(functionalities);

        listaClasses.forEach(classeA -> {
            if (!ehClasseAdicionada(classResponses, classeA)) {
                List<String> methodNames = new ArrayList<>();
                classeA.getMethodName().forEach(methodNames::add);
                MethodResponse methodResponse = new MethodResponse(methodNames);
                ClassResponse classResponse = new ClassResponse(classeA.getClassName(), methodResponse);
                classResponses.add(classResponse);
            } else {
                Integer indice = getIndiceClassedicionada(classResponses, classeA);
                ClassResponse classResponse = classResponses.get(indice);

                //Adicionar apenas os métodos que não estão presentes ainda
                List<String> metodosNaoAdicionados = classeA.getMethodName().stream()
                        .filter(nameMethod -> !ehMetodoJaAdicionado(classResponse.getMethods().getNames(), nameMethod))
                        .collect(Collectors.toList());

                metodosNaoAdicionados.forEach(metodoNaoAdicionado -> classResponse.getMethods().getNames().add(metodoNaoAdicionado));
            }
        });
    }

    private static boolean ehMetodoJaAdicionado(List<String> metodos, String metodoNovo) {
        return metodos.stream().anyMatch(metodo -> metodo.equals(metodoNovo));
    }

    private static boolean ehClasseAdicionada(List<ClassResponse> classResponses, Class classeA) {
        if (classResponses.isEmpty()) return false;
        return classResponses.stream().
                filter(classeResponse -> classeResponse.getName().equals(classeA.getClassName()))
                .findFirst()
                .isPresent();
    }

    private static Integer getIndiceClassedicionada(List<ClassResponse> classResponses, Class classeA) {
        Integer indice = null;
        for (int contador = 0; contador < classResponses.size(); contador++) {
            if (classResponses.get(contador).getName().equals(classeA.getClassName())) {
                indice = contador;
                break;
            }
        }
        return indice;
    }



    private static Integer getIndiceMicrosservico(List<Microservice> microsservicos, String functionality) {
        Integer indice = null;
        for (int contador = 0; contador < microsservicos.size(); contador++) {
            if (microsservicos.get(contador).getFunctionalities().contains(functionality)) {
                indice = contador;
                break;
            }
        }
        return indice;
    }

    private static Integer getIndiceMicrosservico(List<Microservice> microsservicos, List<Column> functionalities) {
        Integer indice = null;
        for (Column functionality : functionalities) {
            for (int contador = 0; contador < microsservicos.size(); contador++) {
                if (microsservicos.get(contador).getFunctionalities().contains(functionality.getNomeFuncionalidade())) {
                    indice = contador;
                    break;
                }
            }
            if (!Objects.isNull(indice)) {
                break;
            }
        }
        return indice;
    }


}
