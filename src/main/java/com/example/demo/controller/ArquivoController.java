package com.example.demo.controller;

import com.example.demo.domain.AvisoComunidade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {

    private final String diretorioUpload = "/app/fotos-eventos/";
    private final String diretorioUploadAvisos = "/app/fotos-avisos/";

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("imagem") MultipartFile arquivo,
            @RequestParam(value = "nome", required = false) String nomePersonalizado) { // Parâmetro opcional
        try {
            Path pasta = Paths.get(diretorioUpload);
            if (!Files.exists(pasta)) Files.createDirectories(pasta);

            String nomeFinal;

            // Lógica: Se vier nome, usamos ele. Se não, gera UUID.
            if (nomePersonalizado != null && !nomePersonalizado.trim().isEmpty()) {
                // Removemos espaços e caracteres estranhos por segurança
                nomeFinal = nomePersonalizado.trim().replaceAll("\\s+", "_");
                // Garante que tenha a extensão correta se o usuário esquecer
                if (!nomeFinal.contains(".")) {
                    String extensaoOriginal = arquivo.getOriginalFilename().substring(arquivo.getOriginalFilename().lastIndexOf("."));
                    nomeFinal += extensaoOriginal;
                }
            } else {
                nomeFinal = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            }

            Path caminho = pasta.resolve(nomeFinal);
            Files.copy(arquivo.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(nomeFinal);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem: " + e.getMessage());
        }


    }

    // NOVO UPLOAD: Nome da imagem vem Direto no Header!
    // NOVO UPLOAD: O nome personalizado também vem no form-data via @RequestParam!
    @PostMapping("/upload-aviso")
    public ResponseEntity<String> uploadAviso(
            @RequestParam("imagem") MultipartFile arquivo,
            @RequestParam("nome") String nomePersonalizado) { // Ex: news_1.jpg vindo do form-data

        return processarUpload(arquivo, nomePersonalizado, diretorioUploadAvisos);
    }

    // Método centralizador que faz a gravação física na pasta correta
    private ResponseEntity<String> processarUpload(MultipartFile arquivo, String nomePersonalizado, String diretorio) {
        try {
            Path pasta = Paths.get(diretorio);
            if (!Files.exists(pasta)) Files.createDirectories(pasta);

            String nomeFinal;

            if (nomePersonalizado != null && !nomePersonalizado.trim().isEmpty()) {
                nomeFinal = nomePersonalizado.trim().replaceAll("\\s+", "_");
                if (!nomeFinal.contains(".")) {
                    String extensaoOriginal = arquivo.getOriginalFilename().substring(arquivo.getOriginalFilename().lastIndexOf("."));
                    nomeFinal += extensaoOriginal;
                }
            } else {
                nomeFinal = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();
            }

            Path caminho = pasta.resolve(nomeFinal);
            Files.copy(arquivo.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok(nomeFinal);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar imagem: " + e.getMessage());
        }
    }
}