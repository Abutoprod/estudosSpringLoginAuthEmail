package com.example.demo.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter // me permite criar os getters e seters sem precisar escrever o código
@NoArgsConstructor // me permite criar um construtor sem argumentos
@AllArgsConstructor // me permite criar um construtor com todos os argumentos 
@Entity // ele diz ao Spring que esta classe é uma entidade, ou seja, ela representa uma tabela no banco de dados por padrão o nome da tabela é o mesmo nome da classe, mas em letras minúsculas e com underline, ou seja, item_estoque
public class ItemEstoque{
    @Id // Definir o id como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Gerar o id automaticamente
    private Long id;
    private String nome;
    private int quantidade;



// DDD faz com que a regra de Negocios fique dentro do dominio, ou seja aqui
// Regra de Negocios sempre dentro da domain
public void adicionarEstoque( int valor){
    if(valor<= 0){
        throw new IllegalArgumentException("A quantia adicionada deve ser maior que zero!");
    }
    this.quantidade += valor;
}

public void removerEstoque(int valor){
    if(valor > this.quantidade){
        throw new IllegalArgumentException("Estoque insuficiente!");
    }
    this.quantidade -= valor;
}

}