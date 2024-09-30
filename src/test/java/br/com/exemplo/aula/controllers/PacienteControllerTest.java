package br.com.exemplo.aula.controllers;

import br.com.exemplo.aula.controllers.dto.PacienteResponseDTO;
import br.com.exemplo.aula.services.PacienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PacienteController.class)
@AutoConfigureMockMvc
class PacienteControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    PacienteService service;

    @Test
    void salvarPaciente() throws Exception {

        when(service.salvarPaciente(any())).thenReturn(new PacienteResponseDTO(
                1L,
                "nome",
                LocalDate.of(2000, 01, 01),
                "123-123-123-00",
                "(48) 98765-4321",
                "example@gmail.com"
        ));

        mvc.perform(post("/pacientes")
                .header("Authorization", "token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "nome": "nome",
                        "dataNascimento": "01/01/2000",
                        "cpf": "123-123-123-00",
                        "telefone": "(48) 98765-4321",
                        "email": "example@gmail.com",
                        "idEndereco": 1
                        }
                        """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("nome"));

        verify(service).salvarPaciente(any());
    }

    @Test
    void listarPacientes() throws Exception {

        PacienteResponseDTO response = new PacienteResponseDTO(
                1L,
                "nome",
                LocalDate.now(),
                "123-123-123-00",
                "(48) 98765-4321",
                "example@gmail.com"
        );

        when(service.listarPacientes()).thenReturn(List.of(response));

        mvc.perform(get("/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("[0].id").exists())
                .andExpect(jsonPath("$[0].nome").value(response.getNome()));
    }

    @Test
    void search() throws Exception {

        when(service.buscarPaciente(any())).thenReturn(new PacienteResponseDTO(
                1L,
                "nome",
                LocalDate.now(),
                "123-123-123-00",
                "(48) 98765-4321",
                "example@gmail.com"
                ));

        mvc.perform(get("/pacientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("nome"));

        verify(service).buscarPaciente(any());
    }

    @Test
    void remove() throws Exception {

        mvc.perform(delete("/pacientes/1"))
                .andExpect(status().isNoContent());

        verify(service).removerPaciente(any());
    }

    @Test
    void update() throws Exception {

        when(service.atualizarPaciente(any(), any())).thenReturn(new PacienteResponseDTO(
                1L,
                "nome",
                LocalDate.of(2000,01,01),
                "123.123.123-00",
                "(48) 98765-4321",
                "example@mail.com"
        ));

        mvc.perform(put("/pacientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "nome",
                                "dataNascimento": "01/01/2000",
                                "cpf": "123.123.123-00",
                                "telefone": "(48) 98765-4321",
                                "email": "example@mail.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("nome"));

        verify(service).atualizarPaciente(any(), any());
    }
}