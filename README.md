# ECommerce
![banner](https://github.com/huelton/ecommerce-backend/blob/main/src/main/java/com/ecommerce/dscatalog/assets/banner.jpeg)

## Introdução

### Spring Boot
[Spring Boot](https://spring.io/projects/spring-boot) é um dos mais famosos projeto [Spring](https://spring.io/projects/spring-framework) utilizado atualmente. Ele veio para facilitar o processo de configuração e publicação de aplicações corporativas. Ele ajuda a criar aplicativos autônomos baseados em Spring de nível de produção com o mínimo de esforço. **Spring Boot** foi concebido para ser um "framework opinativo" porque segue uma abordagem de configuração padrão opinativa que reduz os esforços do desenvolvedor para configurar o aplicativo.

Cada aplicativo construído usando **Spring Boot** precisa apenas definir uma classe Java anotada com `@SpringBootApplication` como seu ponto de entrada principal. Esta anotação encapsula as seguintes outras anotações:
- `@Configuration` – marca a classe como uma padrão de definições de bean.
- `@EnableAutoConfiguration` – indica ao framework para adicionar beans com base nas dependências do caminho de classe automaticamente.
- `@ComponentScan` – verifica outras configurações e beans no mesmo pacote que a classe Application ou abaixo.


## Estrutura do Projeto
Ao trabalhar com projetos **Spring Boot**, não há estrutura de pacote restrita e a estrutura real será orientada por sua necessidade. No entanto, por conveniência e simplicidade, a estrutura deste projeto de amostra é organizada, embora não totalmente, seguindo o padrão MVC (também conhecido como **M**odel **V**iew **C**ontroller). Você pode encontrar mais detalhes sobre esse padrão [aqui](https://examples.javacodegeeks.com/spring-mvc-architecture-overview-example/).

A seguir estão as pastas base nas quais o projeto está organizado e a finalidade de cada uma:
- [📁 application](src/main/java/com/commerce/dscatalog): contém a classe principal, anotada com `@SpringBootApplication`, que é responsável por inicializar a aplicação;
- [📁 config](src/main/java/com/commerce/dscatalog/config): contém definição de beans através de classes de configuração do Spring anotadas com `@Configuration`;
- [📁 resource](src/main/java/com/commerce/dscatalog/resources): contem as classes anotadas com `@Controller` responsável por processar as solicitações de entrada da API REST;
- [📁 exception](src/main/java/com/commerce/dscatalog/services/exceptions): contem as exceções personalizadas para lidar com dados específicos consistentes e/ou violações de regras de negócios;
- [📁 model](src/main/java/com/commerce/dscatalog/entities): contém as classes POJO (também conhecidas como **P**lain **O**ld **J**ava **O**bject) anotadas com `@Entity` representando entidades de banco de dados, ou seja, classes mapeando tabelas de banco de dados;
- [📁 dto](src/main/java/com/commerce/dscatalog/dto): contêm as classes DTO que são usadas como objetos que passam pelos limites da arquitetura para transferir dados;
- [📁 repository](src/main/java/com/commerce/dscatalog/repositories): contem as classes anotadas com `@Repository` responsável por fornecer o mecanismo para armazenamento, recuperação, busca, atualização e operação de exclusão em objetos normalmente presentes em um banco de dados;
- [📁 service](src/main/java/com/commerce/dscatalog/services): contém as classes anotadas com `@Service` na qual a lógica de negócios é implementada;

## Prerequesitos
- Maven 3+
- Java 11


## Bibliotecas e Dependências no Projeto
- [Spring Web](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Hamcrest](http://hamcrest.org/)
- [Mapper Struct](https://mapstruct.org/)
- [H2 Database](https://www.h2database.com/html/main.html)
- [Mysql](https://www.mysql.com/)
- [Lombok](https://projectlombok.org/)
- [Swagger Doc API](https://swagger.io/)
- [Docker](https://docs.docker.com/)


## Projeto Ecommerce-backend API
Projeto Ecommerce-backend API - o projeto implementa uma API para criação de produtos e categorias para um ecommerce fictício utilizando banco de dados Mysql para a persistência dos dados.

## Executando a Aplicação
Para simplificar, prefiro usar a linha de comando ao tentar algo novo, mas você pode pesquisar no Google como configurar um projeto maven em seu IDE favorito.. 😉
<ol>
<li>Clonando o Repositorio Remoto:</li>
  <code>git clone git@github.com:huelton/ecommerce-backend.git</code>
<li>Navegando na pasta:</li>
  <code>cd ecommerce-backend</code>
<li>Rodar o Docker para o Banco de dados Mysql: arquivo docker-compose.yml</li>
  <code>docker-compose up</code>
<li>Rodando a aplicação:</li>
  <code>mvn clean install</code>
  <code>mvn spring-boot:run</code>
</ol>

## Testando a aplicação localmente
1. `Criando novo Produto`
- URL: http://localhost:8080/products
- HTTP Method: POST
- Body:
````json
{
  "name": "The Lord of the Rings",
  "description": "officia deserunt mollit anim id est laborum.",
  "price": 90.50,
  "imgUrl": "https://raw.githubusercontent.com/commerce/dscatalog-resources/master/backend/img/1-big.jpg",
  "date": "2023-06-13"
}  
````
![cria_produto](https://github.com/huelton/ecommerce-backend/blob/main/src/main/java/com/ecommerce/dscatalog/assets/imagem1.jpeg)

**NOTA:** de Acordo com [RFC Padrão](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html), devemos retornar um status HTTP 201 ao criar o recurso de solicitação com sucesso. Na maioria das aplicações, o id do recurso recém-criado é gerado, portanto, é uma boa prática retorná-lo. Para fazer isso, o recurso recém-criado pode ser referenciado pelo(s) URI(s) retornado(s) na entidade da resposta, com o URI mais específico para o recurso fornecido por um campo de cabeçalho `Location`. De acordo com o descrito na captura de tela, ele retorna de acordo com o cabeçalho de resposta.

2. `Retorna um Produto pelo ID`
- URL: http://localhost:8080/products/1
- HTTP Method: GET
  ![retorna_produto](https://github.com/huelton/ecommerce-backend/blob/main/src/main/java/com/ecommerce/dscatalog/assets/imagem2.jpeg)

3. `Cria uma Categoria`
- URL: http://localhost:8080/categories
- HTTP Method: POST
- Body:
````json
{
  "name": "Computadores"
}  
````
![cria_categoria](https://github.com/huelton/ecommerce-backend/blob/main/src/main/java/com/ecommerce/dscatalog/assets/imagem3.jpeg)


## Teste Unitários

Os testes unitários são obrigatórios para qualquer aplicativo de negócios, independentemente de sua complexidade e tamanho. Portanto, para demonstrar algumas das funcionalidades da combinação de **JUnit 5** + **Mockito** + **Hamcrest**, alguns [repository](.src/test/java/com/commerce/dscatalog/repositories) , [service] (.src/test/java/com/commerce/dscatalog/services) e [controller](./src/test/java/com/commerce/dscatalog/resources) testes foram implementados. Você pode verificá-los acessando a pasta  [📁 test](./src/test/java/com/commerce/dscatalog) Para executar os testes, na raiz do projeto, execute:

````bash
$ mvn test
````
## Swagger documentação API
Você pode checar a documentação da API localmente acessando a URL:  [api documentation](http://localhost:8080/swagger-ui.html#)


