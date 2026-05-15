package com.example.demo.service;

import com.example.demo.domain.AvisoComunidade;
import com.example.demo.repository.AvisoComunidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvisoSchedulerService {

    @Autowired
    private AvisoComunidadeRepository repository;

    // Caminho da pasta física onde você salvou as fotos dos avisos/cards
    private final String diretorioUploadAvisos = "/app/fotos-avisos/";

    // Executa à meia-noite todos os dias
    @Scheduled(cron = "0 0 0 * * *")
    public void limparAvisosVencidos() {
        System.out.println("🧹 [Scheduler] Iniciando limpeza de avisos expirados...");

        // 1. Pega do banco todos os avisos que já venceram
        LocalDateTime agora = LocalDateTime.now();
        List<AvisoComunidade> avisosVencidos = repository.findByDataExpiracaoBefore(agora);

        if (!avisosVencidos.isEmpty()) {
            for (AvisoComunidade aviso : avisosVencidos) {

                // 2. Se o aviso tiver uma foto associada, apaga ela do HD da VPS
                if (aviso.getUrlFoto() != null && !aviso.getUrlFoto().trim().isEmpty()) {
                    try {
                        // Como você está guardando apenas o nome (ex: news_1.jpg), pegamos direto do campo
                        String nomeArquivo = aviso.getUrlFoto();

                        // Resolve o caminho completo: /app/fotos-avisos/news_1.jpg
                        Path caminhoDoArquivo = Paths.get(diretorioUploadAvisos).resolve(nomeArquivo);

                        // Deleta do HD se o arquivo realmente existir lá
                        boolean foiDeletado = Files.deleteIfExists(caminhoDoArquivo);

                        if (foiDeletado) {
                            System.out.println("🗑️ [HD] Foto do aviso excluída com sucesso: " + nomeArquivo);
                        } else {
                            System.out.println("⚠️ [HD] Arquivo não encontrado no disco: " + nomeArquivo);
                        }

                    } catch (IOException e) {
                        System.err.println("❌ [Erro HD] Falha ao tentar excluir imagem do card: " + e.getMessage());
                    }
                }
            }

            // 3. Agora que os arquivos físicos saíram do HD, deleta os registros do banco de dados
            repository.deleteAll(avisosVencidos);
            System.out.println("✅ [Scheduler] Limpeza concluída! Total de " + avisosVencidos.size() + " cards antigos removidos.");
        } else {
            System.out.println("💤 [Scheduler] Nenhum card expirado para limpar hoje.");
        }
    }
}