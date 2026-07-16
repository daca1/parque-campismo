# Manual de Utilizador — Gestão de um Parque de Campismo (Fase 2)

Este manual explica como compilar, executar e utilizar a aplicação de consola desenvolvida na Fase 2 do projeto. É uma visita guiada às funcionalidades, não um manual técnico de arquitetura (esse está no relatório da Fase 1 e nos comentários Javadoc do código-fonte).

## 1. Requisitos

- Java 11 (JDK) ou superior
- NetBeans (recomendado, é o IDE usado no enunciado) ou qualquer IDE/linha de comandos com suporte a Java 11

## 2. Compilar e executar

### Via NetBeans

1. Abrir o NetBeans → `File > Open Project` → selecionar a pasta `src/parquecampismo`.
2. Clicar em `Run > Run Project` (ou `F6`).
3. A consola de output do NetBeans mostra o menu principal.

### Via linha de comandos

```bash
cd src
javac parquecampismo/*.java
java parquecampismo.Main
```

## 3. Primeiro arranque

Ao iniciar, a aplicação verifica se existe um ficheiro de dados gravado (`parque.dat`, criado automaticamente por uma sessão anterior).

- **Se existir**, pergunta se deseja carregá-lo (para continuar de onde ficou).
- **Se não existir** (ou se optar por não carregar), é apresentado um menu para escolher o ponto de partida:
  1. **Parque de Teste 1** — 3 setores, 74 talhões (configuração mais pequena, ideal para testar rapidamente)
  2. **Parque de Teste 2** — 4 setores, 150 talhões (configuração maior, mais próxima de um cenário real)
  3. **Parque vazio** — sem setores nem talhões pré-definidos

> Os valores exatos dos dois parques de teste estão documentados no Javadoc de `ParqueCampismo.criarParqueDeTeste1()` / `criarParqueDeTeste2()`: a Tabela 3 do enunciado ficou com formatação ambígua na extração automática de texto do PDF, pelo que as quantidades foram adaptadas para valores de teste razoáveis, mantendo fielmente a estrutura pedida (setores com talhões Grandes/Médios/Pequenos, alguns com WiFi e/ou eletricidade).

## 4. Menu principal

```
[1] Gerir Utentes
[2] Gerir Unidades
[3] Gerir Entradas e Reservas
[4] Faturação
[5] Consultar Setores e Talhões
[6] Gravar Parque
[0] Sair (grava automaticamente)
```

O estado completo do parque (utentes, unidades, entradas, faturas) é sempre gravado automaticamente ao sair pela opção `[0]`, através de serialização Java (`GestorFicheiros`). A opção `[6]` permite gravar manualmente a qualquer momento, sem sair da aplicação.

## 5. Gerir Utentes

- **Listar** — mostra todos os utentes registados e indica se estão atualmente dentro do parque (e em que unidade).
- **Procurar** — por nome (parcial, insensível a maiúsculas) ou por número de identificação.
- **Adicionar** — pede o tipo de identificação (Cartão de Cidadão, Passaporte, Carta de Condução ou Documento Genérico), os respetivos dados, e opcionalmente um cartão de crédito (obrigatório apenas para quem vai ser responsável por uma unidade/entrada).
- **Alterar** — atualiza telemóvel e/ou email (Enter em branco mantém o valor atual).
- **Eliminar** — só é permitido se o utente nunca tiver frequentado o parque e não estiver atualmente associado a nenhuma unidade.

O tipo de utente (Adulto / Criança / Criança grátis) é calculado automaticamente a partir da data de nascimento: menores de 5 anos não pagam diária, dos 5 aos 14 pagam o preço de criança, a partir dos 15 pagam o preço de adulto.

## 6. Gerir Unidades

- **Listar** — mostra todas as unidades, o talhão onde estão instaladas (se alguma) e quantos utentes têm associados.
- **Procurar por talhão** — introduzindo um código como `A3`.
- **Adicionar** — pede o utente responsável (tem de já estar registado), um identificador único, o tipo (Autocaravana, Caravana ou Tenda) e as respetivas dimensões/marca/matrícula. O tipo exato (por exemplo, Autocaravana Grande vs. Pequena) é derivado automaticamente da dimensão indicada.
- **Eliminar** — só é permitido se a unidade não tiver utentes atualmente associados.

> Autocaravanas e Caravanas já incluem eletricidade e WiFi no preço da diária — a aplicação impede a adição desses serviços extra a essas unidades (só se aplicam a Tendas).

## 7. Gerir Entradas e Reservas

Uma **Entrada** agrega um responsável, os utentes que o acompanham, as unidades utilizadas e os talhões ocupados, durante uma estadia. Se a data de entrada introduzida for futura, a aplicação cria automaticamente uma **Reserva** em vez de uma entrada aberta.

- **Registar entrada/reserva** — escolhe o utente responsável e a data de entrada; opcionalmente já associa logo uma unidade e talhão.
- **Adicionar unidade a entrada** — associa mais uma unidade (e o talhão correspondente) a uma entrada já aberta.
- **Remover unidade de entrada** — desassocia uma unidade; o talhão é libertado automaticamente se ficar vazio.
- **Remover utente de entrada** — retira um utente da entrada. O responsável só pode ser removido quando for o último utente a sair (nesse caso a entrada fica pronta a ser eliminada).
- **Eliminar entrada fechada e vazia** — só é possível depois de a entrada estar fechada (fatura emitida) e sem qualquer utente ou unidade associados.

## 8. Faturação

- **Emitir fatura (check-out)** — escolhe uma entrada aberta ou reserva, pede a data de saída, calcula o valor (preço da unidade + serviços extra + preço por utente, tudo multiplicado pelos dias de estadia, mais 25% de IVA, mais a taxa turística de 1€/dia para adultos estrangeiros, que é isenta de IVA). A entrada é fechada automaticamente e os talhões ocupados são libertados. A fatura é também gravada num ficheiro de texto (`fatura_<id>.txt`), que serve como "impressão" da fatura.
- **Listar faturas** — mostra todas as faturas emitidas, o respetivo estado (Fechada/Anulada) e valor.
- **Anular fatura** — marca uma fatura como anulada (as faturas nunca são editadas nem eliminadas, apenas anuladas).

O preço aplicado depende da época: **época alta** é de 15 de junho a 15 de setembro, o resto do ano é **época baixa**.

## 9. Consultar Setores e Talhões

Mostra a estrutura completa do parque: cada setor com a sua cor e a lista dos respetivos talhões, indicando tipo (Grande/Médio/Pequeno), se têm WiFi e/ou eletricidade, e se estão livres ou ocupados.

## 10. Persistência

Todo o estado do parque (setores, talhões, utentes, unidades, entradas e faturas) é gravado num único ficheiro binário (`parque.dat`) através de serialização Java (`ObjectOutputStream`/`ObjectInputStream`). Não é necessária nenhuma base de dados externa — basta manter esse ficheiro na mesma pasta do executável para retomar o parque na próxima execução.
