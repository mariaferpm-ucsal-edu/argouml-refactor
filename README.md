# Refatoração do ArgoUML

O Argo UML é  é uma ferramenta de modelagem UML de código aberto, escrita em Java, amplamente utilizada no passado para o design de sistemas orientados a objetos. Por ser uma ferramenta de código legado, e sem atualizações estáveis recentes, se torna um objeto de estudo pertinente à disciplina de Evolução de software.Este projeto tem como principal finalidade analisar os principais pontos de débito técnico, identificar módulos passíveis de mudança, realizar procedimentos de engenharia reversa baseados em fundamentação teórica, e estudar como a refatoração pode ser aplicada para otimizar o funcionamento de software, a fim de atualizar arquiteturas defasadas.

> Este repositório é um **fork** de [argouml-tigris-org/argouml](https://github.com/argouml-tigris-org/argouml). O código original pertence aos seus autores e é distribuído sob a Eclipse Public License 1.0. As alterações deste fork são de responsabilidade da equipe abaixo e são para propósitos exclusivamente acadêmicos.

## Equipe

| Integrante | GitHub | Módulo / responsabilidade |
|---|---|---|
| Maria Fernanda Maia | [@mariaferpm-ucsal-edu](https://github.com/mariaferpm-ucsal-edu) | Baseline SonarCloud (antes e depois), redação geral do Relatório de refatoração e manual de build; correção dos 153 bugs BLOCKER |
| Rafael Rivas Garcia | [@rafaelrivas-arch](https://github.com/rafaelrivas-arch) | argouml-core-model: engenharia reversa e segregação da God Interface (Facade)|
| Bernardo Barbosa Garcia | [@BernardoBarbosaG](https://github.com/BernardoBarbosaG) | argouml-core-model-mdr: fechamento do vazamento de abstração getUmlPackage() |
| Almerindo Victor Farias Novais | [@Victor-Novais](https://github.com/Victor-Novais) | argouml-core-umlpropertypanels: testes de caracterização e remoção do singleton getCurrentProject() |
| Emilly Maciel Lima Araujo | [@emillymacielaraujo](https://github.com/emillymacielaraujo) | argouml-app: encapsulamento dos campos públicos de Fig em 32 classes de diagrama; refatoração de API's deprecadas |

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
| **Versão analisada** | 0.35.1  |
| **Última versão estável publicada** | 0.35.1 |
| **Tamanho** | 182.000 linhas de código, 2,003 classes, 15,791 functions |

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

| # | Funcionalidade | Descrição | Pacote / módulo principal | Evidência no código |
|---|---|---|---|---|
| 1 | Edição de diagramas UML | Criação e edição visual de diagramas de classes, casos de uso, sequência, estados, atividades, colaboração, componentes e implantação | `argouml-app` (`org.argouml.uml.diagram`) e módulos `argouml-core-diagrams-activity2`, `-class2`, `-deployment2`, `-state2`, `-structure2`, `-sequence2` | Pacotes `uml/diagram/ui` e `diagram/state/ui` com issues no SonarCloud; classe `BaseDiagram` em `activity2` |
| 2 | Gerenciamento do modelo UML | Armazenamento, consulta e eventos dos elementos do metamodelo, por trás de uma interface única | `argouml-core-model` (`org.argouml.model`), `argouml-core-model-mdr` (implementação sobre NetBeans MDR) e `argouml-core-model-euml` | Pacotes `model` e `model/mdr` (ex.: `XmiReaderImpl`, `ModelEventPumpMDRImpl`) |
| 3 | Críticas de projeto (*design critics*) | Análise automática do modelo que gera sugestões ao projetista sobre problemas de projeto, o chamado suporte cognitivo | `argouml-app` (pacotes `cognitive` e `uml/cognitive/critics`) | A confirmar com o `find` abaixo |
| 4 | Lista de tarefas (*to-do list*) | Registro dos itens pendentes gerados pelas críticas e pelo usuário, associados ao projeto | `argouml-app` (`org.argouml.kernel`, `cognitive`) | Mensagens `ProjectImpl addTodoMember` e `MemberList setTodoList` no log dos testes |
| 5 | Persistência de projetos | Abertura e gravação de projetos em arquivo (`.zargo`, `.uml`, XMI) | `argouml-app` (`org.argouml.persistence`) | Classes `SAXParserBase`, `UmlFilePersister`; 43 issues no diretório |
| 6 | Painéis de propriedades | Edição dos atributos dos elementos selecionados no modelo | `argouml-core-umlpropertypanels` (`org.argouml.core.propertypanels.ui`) | Classe `UMLModelElementListModel`; 17 issues no diretório |
| 7 | Perfis UML e notação | Aplicação de perfis (profiles) ao modelo e formatação textual de elementos conforme a notação escolhida | `argouml-app` (`org.argouml.profile`) e `argouml-core-notation` | Diretório `profile/internal` com 18 issues no SonarCloud |
| 8 | Aba de documentação/painel de detalhes  | Adição manual de detalhes,  textos informativos, notas descritivas e detalhamento do propósito de um componente | `argouml-app` (`org.argouml.uml.ui`) Classes `DocumentationManager` e `TabDocumentation` |


---

## 3. Justificativa da escolha

### Motivos

1. O sistema tem uma base de código escrita em Java, cuja é de domínio coletivo da equipe. Dessa forma, é possível ter um workflow colaborativo mais fluido, com melhor entendimento dos módulos e solução mais otimizada de problemas
2. O código-fonte possui  **Licença aberta (EPL 1.0)**, que permite fork e redistribuição.
3. O sistema também possui uma fonte extensa de documentação pública (wiki, manual e artigos acadêmicos), que apoia a engenharia reversa e torna o entendimento da arquitetura amplamente acessível.
4. É um sistema extenso e com muito tempo de desenvolvimento, o que faz com que possua uma presença realista de code smells e acoplamento vista em sistemas legados comerciais. No entanto, o sistema é extenso o suficiente para que a equipe consiga analisar sua estrutura de arquivos e identificar os pontos necessários para refatoração.

### Lista preliminar de problemas encontrados

> Os itens abaixo são hipóteses validadas com SonarQube, PMD, JaCoCo e análise manual.

| # | Problema | Categoria | Evidência |
|---|---|---|---|
| 1 | Vulnerabilidade de leitura de XML (entidades externas habilitadas, risco de XXE) | Segurança | SonarCloud, regra "Disable access to external entities in XML parsing", severidade Blocker, 15 min de esforço cada. Ocorrências vistas: `SAXParserBase.java` (L148), `UmlFilePersister.java` (L467), `BaseDiagram.java` (L202), `XmiReaderImpl.java` (L494). |
| 2 | Risco de segurança concentrado | Segurança | 40 issues de segurança, classificação de segurança **E**; entre eles, Blocker (15%), High (18%) e Low (68%) |
| 3 | Baixa cobertura de testes | Qualidade / testabilidade | Cobertura geral de 20,0% (SonarCloud, Overview, branch `master`) , 86,777 linhas a serem vistas|
| 4 | Duplicação de código | Duplicação | 4,5% de linhas duplicadas, em 182 mil linhas de código |
| 5 | Volume elevado de issues e de esforço de correção | Manutenibilidade | 438 issues abertos, 73h aprox. de esforço|
| 6 | Classes grandes (God Classes com muitas responsabilidades | Acoplamento / coesão | EX: FacadeMDRImpl.java Technical Debt: 0 coverage: 0.7% Lines of code: 3,896 Reliability Rating: D |
| 7 | Métodos com complexidade ciclomática alta | Complexidade | Complexidade total do projeto: 31.878 (exportação anterior). Exemplos de arquivos de maior complexidade (cognitiva): ProjectImpl.java e Designer.java |
| 8 | Documentação insuficiente | Documentação | 31,4% das linhas de código são comentários, sem um manual robusto.|

---


## 4. Manual de Compilação

### Pré-requisitos
- JDK 8 ou 11+
- Git 2.43.0
- Maven 3.8+

para verificar a sua versão do mvn:
```bash
mvn -version
```

**OBS: Caso seja utilizado um JDK mais novo, o JaCoCo (versão 0.8.5 no parentpom) e plugins antigos podem falhar.
 
**Passo 1. Clone o repositório**
 
```bash
git clone https://github.com/mariaferpm-ucsal-edu/argouml-refactor.git
cd argouml-refactor
```

**Passo 2: Instale o parentpom no repositório Maven local (nesse caso, diretamente em argouml-refactor)**

```bash
cd ~.../argouml-refactor
mvn -N install -f parentpom/pom.xml
```

Todos os comandos partem da raiz do repositório, a pasta que contém o pom.xml principal. Para chegar lá de qualquer lugar:
```bash
cd "$(git rev-parse --show-toplevel)"
```

NOTA: em casos de mudanças no pom.xml, reeinstale:

``` bash
cd ~.../argouml-refactor
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

exemplo:

```bash
/home/mafenandaup/Desktop/projeto-argouml/argouml-refactor/src/argouml-app/tests
```

ou
```bash
ls src/<Modulo>/tests
```

Depois rode, dentro do módulo:

```bash
cd src/<Modulo>
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
ls -l src/argouml-app/target/jacoco.exec src/argouml-app/target/site/jacoco/index.html
```

**Visualizar o relatório:**

```bash
xdg-open target/site/jacoco/index.html
```

ou, a partir da raiz do repositório:

```bash
xdg-open src/argouml-app/target/site/jacoco/index.html
```

Alternativamente, cole no navegador o caminho completo com suas variáveis de usuário:

```bash
/home/<usuario>/Desktop/projeto-argouml/argouml-refactor/src/argouml-app/target/site/jacoco/index.html
```
## 6. Exportar e atualizar os dados do SonarCloud

Os dados exportados ficam em `docs/baseline/` e servem de evidência para o relatório. A exportação guarda **todos** os issues abertos da branch `master`, com chave, regra, arquivo e linha.

### 6.1 Pré-requisitos

- `jq` instalado: `sudo apt install jq`
- Um token do SonarCloud (My Account > Security > Generate Tokens). **Nunca commite o token.**
- Uma análise do SonarCloud **concluída** para o commit que se quer registrar. Confira em *Activity* no SonarCloud, ou na aba *Actions* do GitHub, que o workflow terminou com sucesso.

### 6.2 Exportar os issues

Na raiz do repositório:

```bash
./scripts/exportar-issues.sh <fase>
```

O script pede o token (a digitação fica oculta). Se a variável `SONAR_TOKEN` já estiver definida no terminal, ele não pergunta.

| Fase | Quando usar |
|---|---|
| `antes` | Uma vez, antes de qualquer refatoração entrar na `master` |
| `parcial-AAAA-MM-DD` | Para acompanhar o progresso durante o trabalho (ex.: `parcial-2026-11-15`) |
| `depois` | Ao final, com todas as refatorações integradas |

Cada fase gera dois arquivos em `docs/baseline/`:

| Arquivo | Conteúdo |
|---|---|
| `issues-baseline-<fase>-completo.json` | Todos os issues, com os dados completos da API |
| `issues-baseline-<fase>-completo.csv` | Colunas: key, regra, severidade, tipo, arquivo, linha, esforço, status |

> **Atenção:** rodar o script duas vezes com a mesma fase **sobrescreve** os arquivos. Não reutilize `antes` depois que a baseline estiver commitada.

### 6.3 Validar a exportação

```bash
jq '.total, (.issues | length)' docs/baseline/issues-baseline-<fase>-completo.json
wc -l docs/baseline/issues-baseline-<fase>-completo.csv
```

- Os dois números do `jq` devem ser iguais e coincidir com o total mostrado na tela *Issues* do SonarCloud (branch `master`, filtro de issues abertos).
- O `wc -l` deve mostrar o total **mais 1** (a linha de cabeçalho).

Se os valores divergirem, não commite: confira a branch e os filtros da tela do Sonar, e se a análise correspondente ao commit terminou.

### 6.4 Atualizar os dados após novas refatorações

1. Faça o merge dos PRs na `master` e **espere o workflow do GitHub Actions terminar** com sucesso. O SonarCloud só reflete o código depois dessa análise.
2. Confira em *Activity* no SonarCloud que a análise mais recente corresponde ao último commit.
3. Rode `./scripts/exportar-issues.sh parcial-AAAA-MM-DD` (ou `depois`).
4. Valide (seção 5.3).
5. Faça o commit dos arquivos novos:

```bash
git add docs/baseline/
git commit -m "docs: atualiza exportação de issues do SonarCloud (<fase>)"
git push
```

### 6.5 Comparar duas exportações

```bash
jq -r '.issues[].key' docs/baseline/issues-baseline-antes-completo.json | sort > /tmp/antes.txt
jq -r '.issues[].key' docs/baseline/issues-baseline-depois-completo.json | sort > /tmp/depois.txt
comm -23 /tmp/antes.txt /tmp/depois.txt | wc -l   # issues resolvidos
comm -13 /tmp/antes.txt /tmp/depois.txt | wc -l   # issues novos
```

Para listar os issues resolvidos, com arquivo e linha, troque o `wc -l` por uma consulta ao CSV da fase `antes`:

```bash
comm -23 /tmp/antes.txt /tmp/depois.txt > /tmp/resolvidos.txt
grep -F -f /tmp/resolvidos.txt docs/baseline/issues-baseline-antes-completo.csv
```

## 7. Licença e créditos

O ArgoUML é distribuído sob a **Eclipse Public License 1.0**. Os créditos do projeto original pertencem à comunidade ArgoUML.
