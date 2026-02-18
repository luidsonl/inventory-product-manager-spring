# Inventory Manager Client

Um frontend moderno e responsivo para o sistema Inventory Manager, construído com **React**, **TypeScript**, **Vite** e **Tailwind CSS**. Esta aplicação gerencia matérias-primas, produtos, embalagens e fluxos de produção.

## Stack Tecnológico

-   **Framework**: [React 19](https://react.dev/)
-   **Ferramenta de Build**: [Vite](https://vitejs.dev/)
-   **Linguagem**: [TypeScript](https://www.typescriptlang.org/)
-   **Gerenciamento de Estado**: [Redux Toolkit (RTK)](https://redux-toolkit.js.org/) & [RTK Query](https://redux-toolkit.js.org/rtk-query/overview)
-   **Estilização**: [Tailwind CSS v4](https://tailwindcss.com/)
-   **Testes**: [Vitest](https://vitest.dev/) & [React Testing Library](https://testing-library.com/)

## Capturas de Tela

| Lista de Matérias-Primas | Criar Matéria-Prima |
| :---: | :---: |
| ![Lista de Matérias-Primas](docs/raw-materials-list.png) | ![Criar Matéria-Prima](docs/raw-materials-crete.png) |

| Lista de Produtos | Criar Produto |
| :---: | :---: |
| ![Lista de Produtos](docs/products-list.png) | ![Criar Produto](docs/product-new.png) |

| Lista de Embalagens | Criar Embalagem |
| :---: | :---: |
| ![Lista de Embalagens](docs/packiaging-list.png) | ![Criar Embalagem](docs/packaging-new.png) |

| Transações de Produtos | Criar Transação de Produto |
| :---: | :---: |
| ![Transações de Produtos](docs/product-transactions.png) | ![Criar Transação de Produto](docs/product-transaction-new.png) |

| Tela de Produção |
| :---: |
| ![Produção](docs/production-screen.png) |

## Arquitetura & Gerenciamento de Estado

### Redux Toolkit & RTK Query
A aplicação usa **RTK Query** para busca e cache eficiente de dados. Toda a definição da API reside em `src/services/api.service.ts`.

-   **API Centralizada**: Definida usando `createApi`, permitindo geração automática de hooks (ex: `useGetProductsQuery`, `useCreateProductMutation`).
-   **Invalidação de Tags**: Implementamos um sistema robusto de tags (`RawMaterial`, `Product`, `Packaging`, etc.) para atualizar automaticamente os dados em toda a aplicação quando mutações (criar/atualizar/deletar) ocorrem.
    -   *Exemplo*: Criar um novo produto automaticamente invalida a tag `Product`, fazendo com que a lista de produtos seja recarregada sem atualizações manuais de estado.

### Tipagem da API
A segurança de tipos é garantida através de Data Transfer Objects (DTOs) definidos em `src/services/api.types.ts`.
-   **Enums**: `Unit`, `TransactionType`.
-   **Interfaces**: `ProductDTO`, `RawMaterialDTO`, `ProductionExecutionDTO`, etc.
-   Esses tipos garantem que os contratos entre frontend e backend sejam estritamente respeitados.

### Camada de Serviços
A camada de serviços abstrai toda a comunicação HTTP:
-   **Matérias-Primas**: CRUD para materiais base usando enums `Unit`.
-   **Produtos**: Gerencia produtos e sua composição (receitas) de matérias-primas.
-   **Embalagens**: Rastreia inventário de embalagens vinculado a matérias-primas específicas.
-   **Transações**: Registra movimentações de `ENTRADA` e `SAÍDA` para produtos e embalagens.
-   **Produção**: Gerencia a lógica de consumo de materiais e embalagens para produzir produtos acabados.

## Testes

Usamos **Vitest** para testes de integração.

-   **Testes de Integração**: Localizados em `src/services/api.service.integration.test.ts`.
-   **Estratégia**: Os testes inicializam uma store Redux real e disparam thunks assíncronos reais para verificar o ciclo completo:
    1.  Criar recursos (Matéria-Prima -> Produto).
    2.  Verificar que aparecem nas listas.
    3.  Realizar transações.
    4.  Atualizar e Deletar.
    5.  Verificar efeitos colaterais (deleções em cascata, atualizações de cache).

Para executar os testes:
```bash
npm test
```

## Começando

### Pré-requisitos
-   Node.js (v18 ou superior recomendado)
-   npm ou yarn

### Instalação

1.  Clone o repositório:
    ```bash
    git clone https://github.com/your-repo/inventory-manager-client.git
    ```
2.  Instale as dependências:
    ```bash
    npm install
    # ou
    yarn
    ```

### Executando a Aplicação

1.  Certifique-se de que a API backend está rodando.
2.  Inicie o servidor de desenvolvimento:
    ```bash
    npm run dev
    ```
3.  Abra [http://localhost:5173](http://localhost:5173) no seu navegador.

### Build para Produção

```bash
npm run build
```
Isso gera um build pronto para produção na pasta `dist`.
