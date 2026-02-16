import { describe, it, expect } from 'vitest';
import { apiService } from './api.service';

describe('apiService', () => {
    it('should have all expected raw material hooks', () => {
        expect(apiService.useGetRawMaterialsQuery).toBeDefined();
        expect(apiService.useGetRawMaterialByIdQuery).toBeDefined();
        expect(apiService.useGetRawMaterialByCodeQuery).toBeDefined();
        expect(apiService.useCreateRawMaterialMutation).toBeDefined();
        expect(apiService.useUpdateRawMaterialMutation).toBeDefined();
        expect(apiService.useDeleteRawMaterialMutation).toBeDefined();
    });

    it('should have all expected product hooks', () => {
        expect(apiService.useGetProductsQuery).toBeDefined();
        expect(apiService.useGetProductByIdQuery).toBeDefined();
        expect(apiService.useGetProductByCodeQuery).toBeDefined();
        expect(apiService.useCreateProductMutation).toBeDefined();
        expect(apiService.useUpdateProductMutation).toBeDefined();
        expect(apiService.useDeleteProductMutation).toBeDefined();
        expect(apiService.useAddRawMaterialToProductMutation).toBeDefined();
        expect(apiService.useRemoveRawMaterialFromProductMutation).toBeDefined();
    });

    it('should have all expected packaging hooks', () => {
        expect(apiService.useGetPackagingsQuery).toBeDefined();
        expect(apiService.useGetPackagingByIdQuery).toBeDefined();
        expect(apiService.useGetPackagingsByRawMaterialQuery).toBeDefined();
        expect(apiService.useCreatePackagingMutation).toBeDefined();
        expect(apiService.useUpdatePackagingMutation).toBeDefined();
        expect(apiService.useDeletePackagingMutation).toBeDefined();
    });

    it('should have all expected transaction hooks', () => {
        expect(apiService.useGetProductTransactionsQuery).toBeDefined();
        expect(apiService.useGetProductTransactionsByProductQuery).toBeDefined();
        expect(apiService.useCreateProductTransactionMutation).toBeDefined();
        expect(apiService.useGetPackagingTransactionsQuery).toBeDefined();
        expect(apiService.useGetPackagingTransactionsByPackagingQuery).toBeDefined();
        expect(apiService.useCreatePackagingTransactionMutation).toBeDefined();
    });

    it('should have all expected production hooks', () => {
        expect(apiService.useGetProductionSuggestionsQuery).toBeDefined();
        expect(apiService.useGetProductionRequirementsQuery).toBeDefined();
        expect(apiService.useExecuteProductionMutation).toBeDefined();
    });
});
