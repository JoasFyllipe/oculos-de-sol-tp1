package io.github.JoasFyllipe.util;

import io.github.JoasFyllipe.model.usuario.Perfil;
import io.github.JoasFyllipe.model.usuario.Usuario;
import io.github.JoasFyllipe.repository.UsuarioRepository;
import io.github.JoasFyllipe.service.usuario.HashService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.util.ArrayList;

@ApplicationScoped
public class AdminUserCreator {

    private static final Logger LOG = Logger.getLogger(AdminUserCreator.class);

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    HashService hashService;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOG.info("Verificando a existência do usuário ADMIN na inicialização...");

        String adminEmail = "admin@email.com";
        String adminPassword = "admin#77015@";

        // 1. Verifica se o usuário ADMIN já existe no banco de dados
        if (usuarioRepository.findByEmail(adminEmail) == null) {
            LOG.info("Usuário ADMIN não encontrado. Criando um novo...");

            Usuario admin = new Usuario();
            admin.setNome("Administrador Padrão");
            admin.setCpf("00000000000");
            admin.setDataNascimento(LocalDate.of(1990, 1, 1));
            admin.setEmail(adminEmail);
            admin.setSenha(hashService.getHashSenha(adminPassword));

            admin.setPerfis(new ArrayList<>());
            admin.getPerfis().add(Perfil.ADM);
            admin.getPerfis().add(Perfil.USER);

            usuarioRepository.persist(admin);

            LOG.infof("Usuário ADMIN '%s' criado com sucesso.", adminEmail);
        } else {
            LOG.info("Usuário ADMIN já existe. Nenhuma ação necessária.");
        }
    }
}