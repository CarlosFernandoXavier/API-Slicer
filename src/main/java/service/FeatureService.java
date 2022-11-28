package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import model.Class;
import model.ClassResponse;
public class FeatureService {

    public Map<String, List<model.Class>> convertFilesToFeatures(List<String> functionalityFiles,
                                                                 List<String> packages) {
        Map<String, List<model.Class>> functionalityMaps = new HashMap<>();
        model.Class classe;
        List<model.Class> classes;
        File file;
        String nomeFuncionalidade;
        for (String nomeArquivo : functionalityFiles) {
            file = new File(nomeArquivo);
            nomeFuncionalidade = file.getName().substring(0, file.getName().lastIndexOf("."));
            classes = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String auxiliary;
                while ((auxiliary = br.readLine()) != null) {
                    String[] line = auxiliary.split(", ");

                    String className = line[0].substring(line[0].lastIndexOf(": ") + 2);
                    String packageName = className.substring(0, className.lastIndexOf("."));
                    if (isAuthorizedPackage(packageName, packages)) {
                        classe = new model.Class();
                        classe.setClassName(className);
                        classe.setPackageName(packageName);

                        //TODO corrigir o nome dessa variável abaixo
                        List<String> methodList = new ArrayList<>();

                        methodList.add(line[1].substring(line[1].lastIndexOf(": ") + 2));
                        classe.setMethodName(methodList);

                        Integer classIndex = getClassIndex(classes, classe);
                        if (Objects.isNull(classIndex)) {
                            classes.add(classe);
                        } else {
                            model.Class classeA = classes.get(classIndex);
                            classes.remove(classeA);
                            classeA.addMethodName(line[1].substring(line[1].lastIndexOf(": ") + 2));
                            classes.add(classIndex, classeA);
                        }
                    }


                }
                functionalityMaps.put(nomeFuncionalidade, classes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return functionalityMaps;
    }

    public Map<String, List<Class>> convertFilesToFeatures(List<String> functionalityFiles) {
        Map<String, List<Class>> functionalityMaps = new HashMap<>();
        Class classe;
        List<Class> classes;
        File file;
        String nomeFuncionalidade;
        for (String nomeArquivo : functionalityFiles) {
            file = new File(nomeArquivo);
            nomeFuncionalidade = file.getName().substring(0, file.getName().lastIndexOf("."));
            classes = new ArrayList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null) {
                    String[] linha = st.split(", ");

                    String className = linha[0].substring(linha[0].lastIndexOf(": ") + 2);
                    String packageName = className.substring(0, className.lastIndexOf("."));

                    classe = new Class();
                    classe.setClassName(className);
                    classe.setPackageName(packageName);

                    //TODO corrigir o nome dessa variável abaixo
                    List<String> t = new ArrayList<>();

                    t.add(linha[1].substring(linha[1].lastIndexOf(": ") + 2));
                    classe.setMethodName(t);

                    Integer indiceClasse = getClassIndex(classes, classe);
                    if (Objects.isNull(indiceClasse)) {
                        classes.add(classe);
                    } else {
                        Class classeA = classes.get(indiceClasse);
                        classes.remove(classeA);
                        classeA.addMethodName(linha[1].substring(linha[1].lastIndexOf(": ") + 2));
                        classes.add(indiceClasse, classeA);
                    }


                }
                functionalityMaps.put(nomeFuncionalidade, classes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return functionalityMaps;
    }

    private Boolean isAuthorizedPackage(String packageName, List<String> packages) {
        return packages.stream()
                .filter(pack -> pack.equals(packageName))
                .findFirst()
                .isPresent();

    }

    private Integer getIndiceClassedicionada(List<ClassResponse> classResponses, Class classeA) {
        Integer indice = null;
        for (int contador = 0; contador < classResponses.size(); contador++) {
            if (classResponses.get(contador).getName().equals(classeA.getClassName())) {
                indice = contador;
                break;
            }
        }
        return indice;
    }

    private static Integer getClassIndex(List<Class> classes, Class classe) {
        Integer indice = null;
        if (classes.isEmpty()) return null;

        for (int contador = 0; contador < classes.size(); contador++) {
            if (classes.get(contador).getClassName().equals(classe.getClassName())) {
                indice = contador;
                break;
            }
        }
        return indice;
    }
}
