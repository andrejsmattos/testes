package br.com.exemplo.aula.services;

import br.com.exemplo.aula.controllers.dto.PacienteRequestDTO;
import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.entities.Paciente;
import br.com.exemplo.aula.repositories.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    PacienteRepository repository;

    @InjectMocks
    PacienteService service;

    Paciente paciente;

    @BeforeEach
    public void setup() {
        paciente = new Paciente(
                1L,
                "nome",
                LocalDate.now(),
                "123.123.123-12",
                "55 48 98765-4321",
                "example@mail.com"
        );
    }

    @Test
    void listarPacientes() {

        Paciente paciente2 = new Paciente(2L,
                "nome2",
                LocalDate.now(),
                "123.456.789-00",
                "55 21 98765-4321",
                "exemplo@mail.com");

        when(repository.findAll()).thenReturn(List.of(paciente, paciente2));

        List<PacienteResponseDTO> pacientes = service.listarPacientes();

        assertEquals(2, pacientes.size());
        assertEquals(paciente.getNome(), pacientes.get(0).getNome());
        assertEquals(paciente2.getNome(), pacientes.get(1).getNome());

        verify(repository, times(1)).findAll();
    }

    @Test
    void buscarPaciente() {

        when(repository.findById(1L)).thenReturn(Optional.ofNullable(paciente));

        PacienteResponseDTO resultado = service.buscarPaciente(1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("nome", resultado.getNome());
        assertEquals(LocalDate.now(), resultado.getDataNascimento());
        assertEquals("123.123.123-12", resultado.getCpf());
        assertEquals("55 48 98765-4321", resultado.getTelefone());
        assertEquals("example@mail.com", resultado.getEmail());

        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void salvarPaciente() {

        when(repository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteRequestDTO request = new PacienteRequestDTO();

        request.setNome(paciente.getNome());
        request.setDataNascimento(paciente.getDataNascimento());
        request.setCpf(paciente.getCpf());
        request.setTelefone(paciente.getTelefone());
        request.setEmail(paciente.getEmail());

        PacienteResponseDTO response = service.salvarPaciente(request);

        verify(repository, times(1)).save(any(Paciente.class));
    }

    @Test
    void atualizarPaciente() {

        PacienteRequestDTO request = new PacienteRequestDTO();
        request.setNome("novoNome");
        request.setDataNascimento(LocalDate.now());
        request.setCpf("novoCPF");
        request.setTelefone("novoTelefone");
        request.setEmail("novoEmail");

        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteResponseDTO pacienteAtualizado = service.atualizarPaciente(1L, request);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).save(any(Paciente.class));

    }

    @Test
    void removerPaciente() {

        doNothing().when(repository).deleteById(anyLong());

        assertDoesNotThrow(
                ()-> service.removerPaciente(1L));

        verify(repository).deleteById(anyLong());
    }
}