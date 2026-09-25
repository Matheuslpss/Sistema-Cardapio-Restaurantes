# Sistema de Cardápio e Gerenciamento de Pedidos

Projeto de estudo desenvolvido em Java para criar um sistema de cardápio digital e gerenciamento de pedidos para restaurantes.

Além de ser um sistema em desenvolvimento, este projeto é utilizado para praticar Java, orientação a objetos, backend, banco de dados, Git e GitHub.

## Visão do sistema

O sistema deverá possuir três áreas principais:

### Administração e gerente

- cadastrar, editar e excluir categorias;
- cadastrar, editar e excluir produtos;
- associar produtos às categorias;
- ativar ou desativar produtos futuramente;
- acompanhar os pedidos.

### Cliente

- acessar o cardápio, possivelmente por QR Code;
- visualizar produtos separados por categoria;
- consultar nome, descrição, preço e futuramente imagem;
- adicionar produtos ao pedido;
- escolher quantidades;
- adicionar observações;
- visualizar o carrinho;
- enviar o pedido;
- acompanhar o status do pedido.

O cliente não terá login. O pagamento também não será integrado ao sistema: será realizado presencialmente na mesa, usando a máquina do restaurante.

### Cozinha

- receber pedidos;
- visualizar itens, quantidades e observações;
- atualizar o status do pedido.

Fluxo previsto:

```text
PENDENTE -> EM PREPARO -> PRONTO -> FINALIZADO
```

## Estado atual

O projeto funciona pelo console e armazena os dados temporariamente em `ArrayList`.

Já foi implementado:

- cadastro, listagem, edição e exclusão de categorias;
- cadastro, listagem, edição e exclusão de produtos;
- associação de produtos às categorias;
- menu inicial de administração;
- menu inicial do cliente para visualizar produtos cadastrados.

Os dados são perdidos quando o programa é encerrado, pois ainda não existe banco de dados.

## Arquitetura planejada

```text
Front-end do cliente
Front-end da administração e cozinha
              |
              v
       API REST em Java
              |
              v
          Banco de dados
```

A organização das responsabilidades será mantida da seguinte forma:

- modelos representam os dados;
- services concentram operações e regras de negócio;
- menus e interfaces conversam com o usuário;
- controllers receberão requisições da API futuramente;
- repositories cuidarão do acesso ao banco de dados.

## Estrutura atual

```text
src/
├── CardapioApplication.java
├── categoria/
│   ├── Categoria.java
│   └── CategoriaService.java
├── menu/
│   ├── MenuAdmin.java
│   ├── MenuCategoria.java
│   ├── MenuCliente.java
│   └── MenuProduto.java
├── model/
│   └── Produto.java
└── service/
    └── ProdutoService.java
```

## Plano de evolução

1. Consolidar e revisar a estrutura atual.
2. Concluir o cardápio do cliente.
3. Criar `Pedido` e `ItemPedido`.
4. Implementar carrinho e envio de pedidos no console.
5. Criar o fluxo da cozinha.
6. Implementar os status dos pedidos.
7. Migrar gradualmente para Spring Boot.
8. Criar uma API REST.
9. Adicionar PostgreSQL com JPA/Hibernate.
10. Substituir os `ArrayList` por persistência no banco.
11. Adicionar IDs e validações.
12. Criar autenticação para administração e funcionários.
13. Desenvolver ou integrar o front-end.
14. Adicionar QR Code e associação com mesas.

## Como executar atualmente

É necessário ter o Java Development Kit (JDK) instalado.

Compile os arquivos a partir da pasta do projeto:

```bash
javac -encoding UTF-8 -d out src/CardapioApplication.java src/model/Produto.java src/categoria/Categoria.java src/categoria/CategoriaService.java src/service/ProdutoService.java src/menu/MenuAdmin.java src/menu/MenuCategoria.java src/menu/MenuProduto.java src/menu/MenuCliente.java
```

Depois execute:

```bash
java -cp out src.CardapioApplication
```

## Limitações atuais

- ainda não há banco de dados;
- ainda não há pedidos ou carrinho;
- ainda não há cozinha implementada;
- ainda não há autenticação;
- ainda não há front-end web;
- a validação de entradas ainda é básica;
- não há pagamento integrado;
- não há testes automatizados.
