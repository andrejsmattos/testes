package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.NutricionistaRequestDTO;
import br.com.exemplo.aula.controllers.dto.NutricionistaResponseDTO;
import br.com.exemplo.aula.entities.Nutricionista;
import br.com.exemplo.aula.repositories.NutricionistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutricionistaServiceTest {

    @Mock
    NutricionistaRepository repository;

    @InjectMocks
    NutricionistaService service;

    Nutricionista nutricionista;

    @BeforeEach
    public void setup() {
        nutricionista = new Nutricionista(
                1L,
                "André",
                "001",
                8,
                "123456",
                "Esportivo",
                new HashSet<String>(Set.of("Microbiologia", "Nutrição esportiva")));
    }

    @Test
    void listarNutricionistas() {

        Nutricionista nutricionista2 = new Nutricionista(
                2L,
                "Nome",
                "002",
                3,
                "654321",
                "Esportivo",
                new HashSet<String>(Set.of("Microbiologia", "Nutrição esportiva")));

        when(repository.findAll()).thenReturn(List.of(nutricionista, nutricionista2));

        List<NutricionistaResponseDTO> nutricionistas = service.listarNutricionistas();

        assertEquals(2, nutricionistas.size());
        assertEquals(nutricionista.getNome(), nutricionistas.get(0).getNome());
        assertEquals(nutricionista2.getNome(), nutricionistas.get(1).getNome());

        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarNutricionista() {
        when(repository.findById(1L)).thenReturn(Optional.ofNullable(nutricionista));

        NutricionistaResponseDTO resposta = service.buscarNutricionista(1L);

        assertEquals(1L, resposta.getId());
        assertEquals("André", resposta.getNome());
        assertEquals("001", resposta.getMatricula());
        assertEquals(8, resposta.getTempoExperiencia());
        assertEquals("123456", resposta.getCrn());
        assertEquals("Esportivo", resposta.getEspecialidade());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void salvarNutricionista() {

        when(repository.save(any(Nutricionista.class))).thenReturn(nutricionista);

        NutricionistaRequestDTO request = new NutricionistaRequestDTO();

        request.setNome(nutricionista.getNome());
        request.setMatricula(nutricionista.getMatricula());
        request.setTempoExperiencia(nutricionista.getTempoExperiencia());
        request.setCrn(nutricionista.getCrn());
        request.setEspecialidade(nutricionista.getEspecialidade());

        NutricionistaResponseDTO response = service.salvarNutricionista(request);

        verify(repository, times(1)).save(any(Nutricionista.class));
    }

    @Test
    void atualizarNutricionista() {

        NutricionistaRequestDTO request = new NutricionistaRequestDTO();

        request.setNome("novoNome");
        request.setMatricula("novaMatricula");
        request.setTempoExperiencia(5);
        request.setCrn("novoCRN");
        request.setEspecialidade("novaEspecialidade");

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(nutricionista));
        when(repository.save(any(Nutricionista.class))).thenReturn(nutricionista);

        NutricionistaResponseDTO response = service.atualizarNutricionista(1L, request);

        verify(repository, times(1)).findById(anyLong());

    }

    @Test
    void removerNutricionista() {

        doNothing().when(repository).deleteById(anyLong());

        assertDoesNotThrow(
                ()-> service.removerNutricionista(1L));

        verify(repository).deleteById(anyLong());
    }

    @Test
    void adicionarAnoExperiencia() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(nutricionista));

        service.adicionarAnoExperiencia(1L);

        assertEquals(9, nutricionista.getTempoExperiencia());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void adicionarCertificacao() {
        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(nutricionista));

        service.adicionarCertificacao("nova certificação", 1L);

        assertTrue(nutricionista.getCertificacoes().contains("nova certificação"));

        verify(repository, times(1)).findById(1L);
    }
}