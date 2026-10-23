# Refatoração do ArgoUML

O Argo UML é [..]. Este projeto tem como principal finalidade realizar procedimentos de refatoração e engenharia reversa para otimizar [..]

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
- JDK (a pesquisar)
- Apache Ant 4.0 ou Maven 0.35.5 (SNAPSHOT)
- Git

a adicionar 
```bash
git clone https://github.com/mariaferpm-ucsal-edu/argouml-refactor.git
cd argouml-refactor
mvn clean install -DskipTests

instruçoes de como passar o parentpom
```

## 7. Licença e créditos

O ArgoUML é distribuído sob a **Eclipse Public License 1.0**. Os créditos do projeto original pertencem à comunidade ArgoUML.
