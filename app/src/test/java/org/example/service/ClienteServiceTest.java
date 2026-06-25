package org.example.service;

import org.example.model.Cliente;
import org.example.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveCadastrarClienteComSucesso() {
        Cliente cliente = new Cliente("João Silva", "48 99999-1111", "joao@email.com");
        when(clienteRepository.salvar(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.cadastrar(cliente);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(clienteRepository, times(1)).salvar(cliente);
    }

    @Test
    void naoDeveCadastrarClienteComNomeVazio() {
        Cliente cliente = new Cliente("", "48 99999-1111", "joao@email.com");
        assertThrows(IllegalArgumentException.class, () -> clienteService.cadastrar(cliente));
    }

    @Test
    void deveBuscarClientePorId() {
        Cliente cliente = new Cliente(1, "João Silva", "48 99999-1111", "joao@email.com");
        when(clienteRepository.buscarPorId(1)).thenReturn(cliente);

        Cliente resultado = clienteService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("João Silva", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.buscarPorId(99)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> clienteService.buscarPorId(99));
    }
}
