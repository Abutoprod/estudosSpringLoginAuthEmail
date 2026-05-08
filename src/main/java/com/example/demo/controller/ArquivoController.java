package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {

    private final String diretorioUpload = "/app/fotos-eventos/";

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("imagem") MultipartFile arquivo) {
        try {
            Path pasta = Paths.get(diretorioUpload);
            if (!Files.exists(pasta)) Files.createDirectories(pasta);

            String nomeArquivo = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            Path caminho = pasta.resolve(nomeArquivo);

            Files.copy(arquivo.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            // Retorna apenas o nome do arquivo para salvarmos no banco
            return ResponseEntity.ok(nomeArquivo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem: " + e.getMessage());
        }
    }
}