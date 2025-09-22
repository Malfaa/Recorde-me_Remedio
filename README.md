# Recorde-me Remédio 💊
Recorde-me Remédio é um aplicativo Android nativo, desenvolvido em Kotlin, com o objetivo de oferecer uma solução simples e eficaz para o gerenciamento e lembrete de medicamentos.

Com uma interface limpa e focada na praticidade, o aplicativo foi projetado para que usuários de qualquer idade possam organizar seu tratamento sem complicações.

### Funcionalidades Principais:
- **Lista de Medicamentos**: Permite ao usuário criar e visualizar uma lista centralizada com todos os seus medicamentos.

- **Cadastro Detalhado**: Para cada item, é possível adicionar informações essenciais como:

  - Nome do medicamento.

  - Descrição (ex: "tomar com comida", "antes de dormir").

  - Data de início do tratamento.

  - Período exato entre as doses (de 4 em 4 horas, 8 em 8 horas, uma vez ao dia, etc.).

- **Agendamento Inteligente**: Ao salvar um novo medicamento, o aplicativo agenda um serviço em background no sistema operacional Android.

- **Alarmes e Notificações Confiáveis**: No horário programado, o serviço em background dispara um alarme customizado e uma notificação push, garantindo que o usuário seja alertado de forma eficaz, mesmo que o aplicativo esteja fechado.

Este projeto foi uma excelente oportunidade para aplicar conceitos fundamentais do desenvolvimento Android, como a gestão de serviços em background (Services), agendamento de tarefas (AlarmManager) e a criação de notificações (Notifications).
