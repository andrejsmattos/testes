package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class NutricionistaServiceTestIT {

    @Autowired
    private NutricionistaService nutricionistaService;

    @MockBean
    private NutricionistaRepository nutricionistaRepository;

    private Nutricionista nutricionista;

    @BeforeEach
    void setUp() {
        // Configurando um objeto Nutricionista para os testes
        nutricionista = new Nutricionista();
        nutricionista.setId(1L);
        nutricionista.setNome("Nutricionista Teste");
        nutricionista.setMatricula("12345");
        nutricionista.setTempoExperiencia(5);
        nutricionista.setCrn("CRN123");
        nutricionista.setEspecialidade("ESPECIALIDADE_TESTE");
    }

    @Test
    void testListarNutricionistas() {
        // Mockando o comportamento do repositório
        Mockito.when(nutricionistaRepository.findAll())
                .thenReturn(Collections.singletonList(nutricionista));

        // Testando o método listarNutricionistas
        var nutricionistas = nutricionistaService.listarNutricionistas();

        assertNotNull(nutricionistas);
        assertEquals(1, nutricionistas.size());

        NutricionistaResponseDTO dto = nutricionistas.get(0);
        assertEquals(nutricionista.getNome(), dto.getNome());
    }

    @Test
    void testSalvarNutricionista() {
        // Mockando o comportamento de salvar no repositório
        Mockito.when(nutricionistaRepository.findByNome(any(String.class)))
                .thenReturn(Optional.empty());

        Mockito.when(nutricionistaRepository.save(any(Nutricionista.class)))
                .thenReturn(nutricionista);

        // Criando um DTO para enviar no método
        NutricionistaRequestDTO requestDTO = new NutricionistaRequestDTO(
                "Nutricionista Teste",
                "12345",
                5,
                null,
                "CRN123",
                "ESPECIALIDADE_TESTE"
        );

        // Testando o método salvarNutricionista
        NutricionistaResponseDTO responseDTO = nutricionistaService.salvarNutricionista(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(nutricionista.getNome(), responseDTO.getNome());
        assertEquals(nutricionista.getMatricula(), responseDTO.getMatricula());
    }
}
