import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type {
    RawMaterialDTO,
    ProductDTO,
    RawMaterialPackagingDTO,
    ProductionExecutionDTO,
    ProductTransactionDTO,
    RawMaterialPackagingTransactionDTO,
    ProductionSuggestionDTO,
    MaterialRequirementDTO,
    ProductRawMaterialDTO,
} from './api.types';

export const apiService = createApi({
    reducerPath: 'api',
    baseQuery: fetchBaseQuery({ baseUrl: 'http://localhost:8080/api' }),
    tagTypes: ['RawMaterial', 'Product', 'Packaging', 'ProductTransaction', 'PackagingTransaction'],
    endpoints: (builder) => ({
        // Raw Materials
        getRawMaterials: builder.query<RawMaterialDTO[], void>({
            query: () => '/raw-materials',
            providesTags: ['RawMaterial'],
        }),
        getRawMaterialById: builder.query<RawMaterialDTO, number>({
            query: (id) => `/raw-materials/${id}`,
            providesTags: (_, __, id) => [{ type: 'RawMaterial', id }],
        }),
        getRawMaterialByCode: builder.query<RawMaterialDTO, string>({
            query: (code) => `/raw-materials/code/${code}`,
            providesTags: (_, __, code) => [{ type: 'RawMaterial', id: code }],
        }),
        createRawMaterial: builder.mutation<RawMaterialDTO, RawMaterialDTO>({
            query: (body) => ({
                url: '/raw-materials',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['RawMaterial'],
        }),
        updateRawMaterial: builder.mutation<RawMaterialDTO, { id: number; body: RawMaterialDTO }>({
            query: ({ id, body }) => ({
                url: `/raw-materials/${id}`,
                method: 'PUT',
                body,
            }),
            invalidatesTags: (_, __, { id }) => [{ type: 'RawMaterial', id }, 'RawMaterial'],
        }),
        deleteRawMaterial: builder.mutation<void, number>({
            query: (id) => ({
                url: `/raw-materials/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: ['RawMaterial'],
        }),

        // Products
        getProducts: builder.query<ProductDTO[], void>({
            query: () => '/products',
            providesTags: ['Product'],
        }),
        getProductById: builder.query<ProductDTO, number>({
            query: (id) => `/products/${id}`,
            providesTags: (_, __, id) => [{ type: 'Product', id }],
        }),
        getProductByCode: builder.query<ProductDTO, string>({
            query: (code) => `/products/code/${code}`,
            providesTags: (_, __, code) => [{ type: 'Product', id: code }],
        }),
        createProduct: builder.mutation<ProductDTO, ProductDTO>({
            query: (body) => ({
                url: '/products',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['Product'],
        }),
        updateProduct: builder.mutation<ProductDTO, { id: number; body: ProductDTO }>({
            query: ({ id, body }) => ({
                url: `/products/${id}`,
                method: 'PUT',
                body,
            }),
            invalidatesTags: (_, __, { id }) => [{ type: 'Product', id }, 'Product'],
        }),
        deleteProduct: builder.mutation<void, number>({
            query: (id) => ({
                url: `/products/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: ['Product'],
        }),
        addRawMaterialToProduct: builder.mutation<
            ProductRawMaterialDTO,
            { productId: number; rawMaterialId: number; quantity: number }
        >({
            query: ({ productId, rawMaterialId, quantity }) => ({
                url: `/products/${productId}/raw-materials`,
                method: 'POST',
                params: { rawMaterialId, quantity },
            }),
            invalidatesTags: (_, __, { productId }) => [{ type: 'Product', id: productId }],
        }),
        removeRawMaterialFromProduct: builder.mutation<void, number>({
            query: (associationId) => ({
                url: `/products/raw-materials/${associationId}`,
                method: 'DELETE',
            }),
            invalidatesTags: ['Product'],
        }),

        // Packaging
        getPackagings: builder.query<RawMaterialPackagingDTO[], void>({
            query: () => '/packaging',
            providesTags: ['Packaging'],
        }),
        getPackagingById: builder.query<RawMaterialPackagingDTO, number>({
            query: (id) => `/packaging/${id}`,
            providesTags: (_, __, id) => [{ type: 'Packaging', id }],
        }),
        getPackagingsByRawMaterial: builder.query<RawMaterialPackagingDTO[], number>({
            query: (rawMaterialId) => `/packaging/raw-material/${rawMaterialId}`,
            providesTags: (_, __, rawMaterialId) => [
                { type: 'Packaging', id: `rawMaterial-${rawMaterialId}` },
            ],
        }),
        createPackaging: builder.mutation<RawMaterialPackagingDTO, RawMaterialPackagingDTO>({
            query: (body) => ({
                url: '/packaging',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['Packaging'],
        }),
        updatePackaging: builder.mutation<
            RawMaterialPackagingDTO,
            { id: number; body: RawMaterialPackagingDTO }
        >({
            query: ({ id, body }) => ({
                url: `/packaging/${id}`,
                method: 'PUT',
                body,
            }),
            invalidatesTags: (_, __, { id }) => [{ type: 'Packaging', id }, 'Packaging'],
        }),
        deletePackaging: builder.mutation<void, number>({
            query: (id) => ({
                url: `/packaging/${id}`,
                method: 'DELETE',
            }),
            invalidatesTags: ['Packaging'],
        }),

        // Transactions
        getProductTransactions: builder.query<ProductTransactionDTO[], void>({
            query: () => '/product-transactions',
            providesTags: ['ProductTransaction'],
        }),
        getProductTransactionsByProduct: builder.query<ProductTransactionDTO[], number>({
            query: (productId) => `/product-transactions/product/${productId}`,
            providesTags: (_, __, productId) => [
                { type: 'ProductTransaction', id: `product-${productId}` },
            ],
        }),
        createProductTransaction: builder.mutation<ProductTransactionDTO, ProductTransactionDTO>({
            query: (body) => ({
                url: '/product-transactions',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['ProductTransaction', 'Product'],
        }),

        getPackagingTransactions: builder.query<RawMaterialPackagingTransactionDTO[], void>({
            query: () => '/packaging-transactions',
            providesTags: ['PackagingTransaction'],
        }),
        getPackagingTransactionsByPackaging: builder.query<
            RawMaterialPackagingTransactionDTO[],
            number
        >({
            query: (packagingId) => `/packaging-transactions/packaging/${packagingId}`,
            providesTags: (_, __, packagingId) => [
                { type: 'PackagingTransaction', id: `packaging-${packagingId}` },
            ],
        }),
        createPackagingTransaction: builder.mutation<
            RawMaterialPackagingTransactionDTO,
            RawMaterialPackagingTransactionDTO
        >({
            query: (body) => ({
                url: '/packaging-transactions',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['PackagingTransaction', 'Packaging'],
        }),

        // Production
        getProductionSuggestions: builder.query<ProductionSuggestionDTO, void>({
            query: () => '/production/suggest',
        }),
        getProductionRequirements: builder.query<
            MaterialRequirementDTO,
            { productId: number; quantity: number }
        >({
            query: ({ productId, quantity }) => ({
                url: '/production/requirements',
                params: { productId, quantity },
            }),
        }),
        executeProduction: builder.mutation<void, ProductionExecutionDTO>({
            query: (body) => ({
                url: '/production/execute',
                method: 'POST',
                body,
            }),
            invalidatesTags: ['Product', 'Packaging', 'ProductTransaction', 'PackagingTransaction'],
        }),
    }),
});

export const {
    useGetRawMaterialsQuery,
    useGetRawMaterialByIdQuery,
    useGetRawMaterialByCodeQuery,
    useCreateRawMaterialMutation,
    useUpdateRawMaterialMutation,
    useDeleteRawMaterialMutation,
    useGetProductsQuery,
    useGetProductByIdQuery,
    useGetProductByCodeQuery,
    useCreateProductMutation,
    useUpdateProductMutation,
    useDeleteProductMutation,
    useAddRawMaterialToProductMutation,
    useRemoveRawMaterialFromProductMutation,
    useGetPackagingsQuery,
    useGetPackagingByIdQuery,
    useGetPackagingsByRawMaterialQuery,
    useCreatePackagingMutation,
    useUpdatePackagingMutation,
    useDeletePackagingMutation,
    useGetProductTransactionsQuery,
    useGetProductTransactionsByProductQuery,
    useCreateProductTransactionMutation,
    useGetPackagingTransactionsQuery,
    useGetPackagingTransactionsByPackagingQuery,
    useCreatePackagingTransactionMutation,
    useGetProductionSuggestionsQuery,
    useGetProductionRequirementsQuery,
    useExecuteProductionMutation,
} = apiService;
