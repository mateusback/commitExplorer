# Sistema de Avaliação Acadêmica de Repositórios Git

## Visão Geral

O sistema foi refatorado para incluir uma avaliação mais abrangente e focada em aspectos acadêmicos, considerando não apenas a qualidade técnica do código, mas também práticas de desenvolvimento que são importantes em um contexto educacional.

## Novos Critérios de Avaliação

### 1. Frequência e Consistência (25% da nota)
- **Frequência de commits**: Avalia se o estudante mantém uma cadência regular de commits
- **Consistência temporal**: Percentual de dias com commits no período
- **Regularidade**: Variação na quantidade de commits por dia
- **Intervalos sem commits**: Identifica períodos longos sem atividade

**Exemplos:**
- ✅ **Bom**: Commits distribuídos em 60% dos dias, 10-20 commits por semana
- ❌ **Ruim**: Commits concentrados em poucos dias, intervalos de 7+ dias sem commits

### 2. Qualidade das Mensagens (20% da nota)
- **Mensagens descritivas**: Uso de padrões como Conventional Commits
- **Tamanho adequado**: Mensagens nem muito curtas nem muito longas
- **Especificidade**: Evita mensagens genéricas como "fix", "update"

**Exemplos:**
- ✅ **Bom**: `feat(auth): add JWT token validation with expiry check`
- ✅ **Bom**: `fix: resolve null pointer exception in user service`
- ❌ **Ruim**: `fix`, `update`, `changes`, `wip`

### 3. Variedade e Organização de Tipos (15% da nota)
- **Diversidade de tipos**: feat, fix, docs, test, refactor, etc.
- **Proporção balanceada**: Não apenas features, mas também manutenção
- **Presença de refatoração**: Demonstra cuidado com qualidade
- **Commits de teste**: Mostra preocupação com testes

**Tipos recomendados:**
- `feat`: Novas funcionalidades
- `fix`: Correções de bugs
- `docs`: Documentação
- `test`: Adição ou modificação de testes
- `refactor`: Refatoração sem mudança de funcionalidade
- `style`: Formatação, estilo de código
- `chore`: Tarefas de manutenção

### 4. Distribuição de Trabalho (15% da nota)
- **Colaboração**: Para projetos em equipe, avalia distribuição entre membros
- **Concentração**: Evita que um membro faça todo o trabalho
- **Equilíbrio**: Todos os membros contribuem de forma significativa

### 5. Distribuição Temporal (10% da nota)
- **Horários de trabalho**: Preferência por horário comercial
- **Concentração horária**: Evita trabalho apenas em horários extremos
- **Organização**: Demonstra disciplina e planejamento

### 6. Qualidade Técnica (15% da nota)
- **Code smells**: Densidade de problemas de qualidade
- **Complexidade**: Complexidade ciclomática do código
- **Manutenibilidade**: Facilidade de manutenção do código

## Como Usar

### No Código

O sistema agora oferece dois métodos de avaliação:

```java
// Avaliação básica (método original)
FeedbackDinamicoView feedbackBasico = pontuacaoAnaliseService.gerarFeedback(
    geral, indicadores, autores
);

// Nova avaliação acadêmica (mais completa)
FeedbackDinamicoView feedbackAcademico = pontuacaoAnaliseService.gerarFeedbackAcademico(
    commits, dataInicio, dataFim, indicadoresBasicos
);
```

### Interpretação das Notas

| Nota | Score | Descrição |
|------|-------|-----------|
| A    | 85-100| Excelente: Demonstra domínio das práticas de desenvolvimento |
| B    | 70-84 | Bom: Práticas adequadas com algumas oportunidades de melhoria |
| C    | 55-69 | Regular: Precisa melhorar em várias áreas |
| D    | 0-54  | Insuficiente: Necessita revisão significativa das práticas |

## Feedback Automático

O sistema agora fornece feedback específico e actionable:

### Aspectos Positivos Identificados
- "Excelente consistência: commits em 75% dos dias do período"
- "Boa variedade de tipos de commit: 6 tipos diferentes"
- "Mensagens descritivas em 80% dos commits"

### Problemas Identificados e Sugestões
- **Problema**: "Baixa consistência: commits em apenas 25% dos dias"
  **Sugestão**: "Estabeleça uma rotina de commits diários ou a cada 2 dias"

- **Problema**: "Apenas 30% das mensagens são descritivas"
  **Sugestão**: "Use o padrão Conventional Commits: feat:, fix:, docs:, etc."

- **Problema**: "Ausência de commits de refatoração"
  **Sugestão**: "Inclua refatorações regulares para melhorar a qualidade do código"

## Benefícios Pedagógicos

1. **Ensina boas práticas**: Incentiva o uso de padrões da indústria
2. **Desenvolve disciplina**: Promove trabalho consistente e organizado
3. **Melhora comunicação**: Enfatiza a importância de mensagens claras
4. **Preparação profissional**: Simula ambientes de trabalho reais
5. **Autoavaliação**: Fornece feedback imediato para melhoria contínua

## Implementação

O sistema foi implementado com:
- `AvaliadorAcademicoService`: Novo serviço para cálculos acadêmicos
- `PontuacaoAnaliseService`: Refatorado para incluir avaliação acadêmica
- Integração automática no `ObterInformacoesAnaliseQueryHandler`
- Mantém compatibilidade com sistema anterior

O sistema está pronto para uso e fornecerá avaliações mais justas e educativas para projetos acadêmicos.
