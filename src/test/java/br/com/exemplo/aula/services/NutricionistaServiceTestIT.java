package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class NutricionistaServiceTestIT {

    @Autowired
    private NutricionistaService nutricionistaService;

    @MockBean
    private NutricionistaRepository nutricionistaRepository;

    Nutricionista nutricionista;

    @BeforeEach
    public void setup() {
        nutricionista = new Nutricionista(
                1L,
                "nome",
                "123456",
                5,
                "CRN123",
                "especialidade",
                new HashSet<String>(Set.of("certificação 1", "certificação 2"))
        );
    }

    @Test
    void listarNutricionistas() {

        List<Nutricionista> nutricionistas = new ArrayList<>();
        nutricionistas.add(nutricionista);

        when(nutricionistaRepository.findAll()).thenReturn(nutricionistas);

        var resultado = nutricionistaService.listarNutricionistas();

        assertNotNull(resultado);
        assertEquals(nutricionistas.get(0).getId(), resultado.get(0).getId());
//        assertEquals(1, nutricionistas.size());

        verify(nutricionistaRepository).findAll();
    }

    @Test
    void salvarNutricionista() {

        NutricionistaRequestDTO request = new NutricionistaRequestDTO(
                "nutricionista",
                "123456",
                5,
                1L,
                "CRN123",
                "especialidade"
        );

        when(nutricionistaRepository.save(any())).thenReturn(nutricionista);
        when(nutricionistaRepository.findByNome(any(String.class))).thenReturn(Optional.empty());

        var resultado = nutricionistaService.salvarNutricionista(request);

        assertNotNull(resultado);
        assertEquals(nutricionista.getNome(), resultado.getNome());

        verify(nutricionistaRepository).save(any());
    }
}
