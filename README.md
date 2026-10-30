# Refatoração do ArgoUML

O Argo UML é  é uma ferramenta de modelagem UML de código aberto, escrita em Java, amplamente utilizada no passado para o design de sistemas orientados a objetos. Por ser uma ferramenta de código legado, e sem atualizações estáveis recentes, se torna um objeto de estudo pertinente à disciplina de Evolução de software.Este projeto tem como principal finalidade analisar os principais pontos de débito técnico, identificar módulos passíveis de mudança, realizar procedimentos de engenharia reversa baseados em fundamentação teórica, e estudar como a refatoração pode ser aplicada para otimizar o funcionamento de software, a fim de atualizar arquiteturas defasadas.

> Este repositório é um **fork** de [argouml-tigris-org/argouml](https://github.com/argouml-tigris-org/argouml). O código original pertence aos seus autores e é distribuído sob a Eclipse Public License 1.0. As alterações deste fork são de responsabilidade da equipe abaixo e são para propósitos exclusivamente acadêmicos.

## Equipe

| Integrante | GitHub | Módulo / responsabilidade |
|---|---|---|
| Maria Fernanda Maia | [@mariaferpm-ucsal-edu](https://github.com/mariaferpm-ucsal-edu) | [módulo ou classes refatoradas] |
| Rafael Rivas Garcia | [@rafaelrivas-arch](https://github.com/rafaelrivas-arch) | [módulo ou classes refatoradas] |
| Bernardo Barbosa Garcia | [@BernardoBarbosaG](https://github.com/BernardoBarbosaG) | [módulo ou classes refatoradas] |
| Almerindo Victor Farias Novais | [@Victor-Novais](https://github.com/Victor-Novais) | [módulo ou classes refatoradas] |
| Emilly Maciel Lima Araujo | [@emillymacielaraujo](https://github.com/emillymacielaraujo) | [módulo ou classes refatoradas] |

---

## 1. Ficha Técnica

| Campo | Descrição |
|---|---|
| **Nome** | ArgoUML |
| **Tipo** | Aplicação desktop (Java Swing) |
| **Objetivo principal** | Modelagem de software com diagramas UML 1.4, com suporte cognitivo ao projetista (críticas de projeto) e geração de código a partir dos modelos |
| **Origem** | Criado na Universidade da Califórnia em Irvine, hospedado no Tigris.org e migrado para o GitHub em 2019 |
| **Repositório original** | https://github.com/argouml-tigris-org/argouml |
| **Licença** | Eclipse Public License 1.0 (permite fork e modificação) |
| **Versão analisada** | [VERSÃO OU COMMIT, por exemplo 0.35.1 / hash] |
| **Última versão estável publicada** | 0.35.1 |
| **Tamanho** | [N] linhas de código, [N] classes, [N] módulos (preencher com o resultado do SonarQube ou `cloc`) |

### Tecnologias legadas

| Tecnologia | Situação | Observação |
|---|---|---|
| Java 5/6 | Defasada | O projeto foi concebido para essas versões, e há relatos de adaptações necessárias para compilar em Java 8 e 11 |
| Apache Ant | Legada | Build original por `build.xml`, com suporte a Maven apenas parcial |
| Swing | Legada | Interface gráfica baseada em componentes Swing, com forte acoplamento entre lógica e visão |
| NetBeans MDR | Descontinuada | Implementação do metamodelo UML usada pelo módulo `argouml-core-model-mdr` |
| UML 1.4 | Defasada | O sistema não implementa versões mais recentes da UML |
| JUnit (versão antiga) | Defasada | Cobertura de testes baixa e testes dependentes de recursos externos |

---

## 2. Mapeamento das funcionalidades atuais

| # | Funcionalidade | Descrição | Pacote / módulo principal |
|---|---|---|---|
| 1 | Edição de diagramas UML | Criação e edição de diagramas de classes, casos de uso, sequência, estados, atividades, colaboração, componentes e implantação | [ex.: `argouml-app`] |
| 2 | Gerenciamento do modelo UML | Armazenamento e consulta dos elementos do metamodelo | [ex.: `argouml-core-model`, `argouml-core-model-mdr`] |

a inserir outras funções

---

## 3. Justificativa da escolha

### Motivos

1. O sistema tem uma base de código escrita em Java, cuja é de domínio coletivo da equipe. Dessa forma, é possível ter um workflow colaborativo mais fluido, com melhor entendimento dos módulos e solução mais otimizada de problemas
2. O código-fonte possui  **Licença aberta (EPL 1.0)**, que permite fork e redistribuição.
3. O sistema também possui uma fonte extensa de documentação pública (wiki, manual e artigos acadêmicos), que apoia a engenharia reversa e torna o entendimento da arquitetura amplamente acessível.
4.
5.
6.

### Lista preliminar de problemas encontrados

> Os itens abaixo são hipóteses a validar com SonarQube, PMD, JaCoCo e análise manual. Substitua cada um por evidência (classe, linha, métrica) na Etapa 2.

| # | Problema | Categoria | Evidência (preencher) |
|---|---|---|---|
| 1 | Dificuldade de compilar em versões modernas do Java | Obsolescência tecnológica | [erro de build observado] |
| 2 | Classes grandes que concentram muitas responsabilidades (God Class) | Acoplamento / coesão | [classe, linhas, métodos] |

....

---


## 4. Manual de Compilação

### Pré-requisitos
- JDK 8 ou 11 CONFI
- Apache Ant 4.0 ou Maven 0.35.5 (SNAPSHOT)
- Git 2.43.0
- Maven 3.8 

para verificar a sua versão do mvn:
```bash
mvn -version
```

**OBS: Caso seja utilizado um JDK mais novo, o JaCoCo (versão 0.8.5 no parentpom) e plugins antigos podem falhar.
 
**Passo 1. Clone o repositório**
 
```bash
git clone git clone https://github.com/mariaferpm-ucsal-edu/argouml-refactor.git
cd argouml-refactor
```

**Passo 2: Instale o parentpom no repositório Maven local (nesse caso, diretamente em argouml-refactor)**

```bash
cd ~/Desktop/projeto-argouml/argouml-refactor
mvn -N install -f parentpom/pom.xml
```

NOTA: em casos de mudanças no pom.xml, reeinstale:

``` bash
cd ~/Desktop/projeto-argouml/argouml-refactor
mvn -N install -f parentpom/pom.xml
```

A opção `-N` instala apenas o parentpom do repo original, sem compilar módulos. O `pom.xml` da raiz declara o parent `org.argouml:parentpom:0.35.5-SNAPSHOT`, e o Maven precisa encontrá-lo. Sem este passo, o build falha ao resolver o parent.

**Passo 3: Compile os módulos, pulando os testes**

```bash
mvn clean install -DskipTests
```

Rode sempre na **raiz do repositório (argouml-refactor)**, a pasta onde está o `pom.xml` principal.

**Passo 4: Rode os testes**

NOTA: É Importante que você rode APENAS UMA CLASSE POR VEZ, Pois  suíte completa é lenta (uma JVM por classe de teste) e emite mensagens apenas informativas.
verifique se o nome das classes que você for rodar são classes DE TESTE;

```bash
/home/<User>/../argouml-refactor/src/<Modulo>/tests
```

ou
```bash
ls src/argouml-app/tests
```

Depois rode, dentro do módulo:

```bash
cd src/argouml-app
mvn test -Dtest=NomeDaClasseDeTeste -DfailIfNoTests=false
```

Exemplo:

```bash
cd src/argouml-app
mvn test -Dtest=TestDiagramUpdateAtLoad -DfailIfNoTests=false
```

### 4. Como executar a aplicação (IntelliJ)

1. Abra a pasta do repositório (File > Open) e deixe o IntelliJ importar como projeto Maven.
2. Em Project Structure, confirme que o SDK do projeto é o mesmo JDK validado.
3. Localize a classe `org.argouml.application.Main` (Ctrl+Shift+N) no módulo `argouml-app`.
4. Clique com o botão direito e escolha **Run 'Main.main()'**. Se faltar classe no classpath, em Run > Edit Configurations ajuste o campo *Use classpath of module* para `argouml-app`.

### 5. Cobertura de testes (JaCoCo)

O parentpom já inclui o JaCoCo. Depois de rodar `mvn test`, o relatório de cada módulo fica em:

```text
src/<modulo>/target/site/jacoco/index.html
```
**Pré-requisito para o JDK 21:** o parentpom deve usar uma versão recente do JaCoCo. A versão antiga (0.8.5) falha ao instrumentar classes do JDK 21 com erro em `org.jacoco.agent.rt...ClassReader`. Em `parentpom/pom.xml`:

```xml
<jacoco.version>0.8.12</jacoco.version>
```

Depois de editar, reinstale o parentpom (Passo 1).

**gerar relatório de testes:**

```bash
cd ~/Desktop/projeto-argouml/argouml-refactor/src/argouml-app
mvn test -Dtest=TestDiagramUpdateAtLoad -DfailIfNoTests=false > test.log 2>&1
```

**liste se o arquivo de relatório de testes do jacoco já existe:**

```bash
cd ~/Desktop/projeto-argouml/argouml-refactor/src/argouml-app
ls -l target/jacoco.exec target/site/jacoco/index.html
```

**Visualizar o relatório:**

```bash
xdg-open target/site/jacoco/index.html
```

ou, a partir da raiz do repositório:

```bash
xdg-open src/argouml-app/target/site/jacoco/index.html
```

Alternativamente, cole no navegador o caminho completo com `file://`:

```text
file:///home/<usuario>/Desktop/projeto-argouml/argouml-refactor/src/argouml-app/target/site/jacoco/index.html
```

## 7. Licença e créditos

O ArgoUML é distribuído sob a **Eclipse Public License 1.0**. Os créditos do projeto original pertencem à comunidade ArgoUML.
