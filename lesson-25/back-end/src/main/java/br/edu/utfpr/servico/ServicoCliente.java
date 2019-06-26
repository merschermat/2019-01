package br.edu.utfpr.servico;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;


@RestController
public class ServicoCliente {

    private List<PaisDTO> paises;
    private List<ClienteDTO> clientes;


    public ServicoCliente() {
        paises = Stream.of(
                PaisDTO.builder().id(1).nome("Brasil").sigla("BR").codigoTelefone(55).build(),
                PaisDTO.builder().id(2).nome("Estados Unidos da América").sigla("EUA").codigoTelefone(33).build(),
                PaisDTO.builder().id(3).nome("Reino Unido").sigla("RU").codigoTelefone(44).build()
        ).collect(Collectors.toList());

        clientes = Stream.of(
                ClienteDTO.builder().id(1).nome("Igor Santana").idade(21).limiteCredito(1500.00)
                        .telefone("18 99600-6891").pais(paises.get(1)).build(),
                ClienteDTO.builder().id(2).nome("Mateus Merscher").idade(21).limiteCredito(1000.00)
                        .telefone("18 99600-6891").pais(paises.get(2)).build(),
                ClienteDTO.builder().id(3).nome("Gabriel Romero").idade(21).limiteCredito(700.00)
                        .telefone("18 99600-6891").pais(paises.get(0)).build()
        ).collect(Collectors.toList());
    }

    @GetMapping ("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> listar() {

        return ResponseEntity.ok(clientes);
    }

    @GetMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> listarPorId(@PathVariable int id) {
        Optional<ClienteDTO> clienteEncontrado = clientes.stream().filter(p -> p.getId() == id).findAny();

        return ResponseEntity.of(clienteEncontrado);
    }

    @PostMapping ("/servico/cliente")
    public ResponseEntity<ClienteDTO> criar (@RequestBody ClienteDTO cliente) {

        cliente.setId(clientes.size() + 1);
        clientes.add(cliente);

        return ResponseEntity.status(201).body(cliente);
    }

    @DeleteMapping ("/servico/cliente/{id}")
    public ResponseEntity excluir (@PathVariable int id) {
        
        if (clientes.removeIf(cliente -> cliente.getId() == id))
            return ResponseEntity.noContent().build();

        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> alterar (@PathVariable int id, @RequestBody ClienteDTO cliente) {
        Optional<ClienteDTO> clienteExistente = clientes.stream().filter(p -> p.getId() == id).findAny();

        clienteExistente.ifPresent(cli -> {
            try{
                cli.setNome(cliente.getNome());

            }catch (Exception e){
                e.printStackTrace();
            }
            cli.setIdade(cliente.getIdade());
            cli.setLimiteCredito(cliente.getLimiteCredito());
            cli.setTelefone(cliente.getTelefone());
            cli.setPais(paises.stream().filter(pais -> pais.getId() == cliente.getPais().getId()).findAny().get());
        });

        return ResponseEntity.of(clienteExistente);
    }
}