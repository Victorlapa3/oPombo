package com.example.opombo.service;

import com.example.opombo.exception.PomboException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImagemService {

    public String processarImagem(MultipartFile file) throws PomboException {
        byte[] imagemBytes;
        try {
            imagemBytes = file.getBytes();
        } catch (IOException e) {
            throw new PomboException("Erro ao processar arquivo");
        }

        String base64Imagem = Base64.getEncoder().encodeToString(imagemBytes);

        return base64Imagem;
    }
}
