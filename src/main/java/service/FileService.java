package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileService {

    public List<String> buscarNomeArquivos(String diretorioLeitura) {
        String diretorioDestino = "src/main/resources/output";
        List<String> nomeArquivos = new ArrayList<>();
        descompactar(diretorioLeitura, diretorioDestino, nomeArquivos);
        return getPathArquivos(diretorioDestino, nomeArquivos);
    }

    private void descompactar(String zipFilePath, String destDir, List<String> nomeArquivos) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                nomeArquivos.add(fileName);
                File newFile = new File(destDir + File.separator + fileName);
                //System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String> getPathArquivos(String diretorioDestino, List<String> nomeArquivos) {
        List<String> pathArquivos = new ArrayList<>();
        nomeArquivos.forEach(nome -> pathArquivos.add(diretorioDestino + "/" + nome));
        return pathArquivos;
    }
}
