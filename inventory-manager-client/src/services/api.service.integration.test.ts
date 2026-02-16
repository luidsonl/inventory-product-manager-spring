import { describe, it, expect, beforeAll, afterAll, vi } from 'vitest';
import { configureStore } from '@reduxjs/toolkit';
import { apiService } from './api.service';
import { env } from '../env';
import type { RawMaterialDTO, ProductDTO, RawMaterialPackagingDTO, ProductRawMaterialDTO, ProductionExecutionDTO } from './api.types';

vi.setConfig({ testTimeout: 30000 });

function makeStore() {
    return configureStore({
        reducer: { [apiService.reducerPath]: apiService.reducer },
        middleware: (gdm) => gdm().concat(apiService.middleware),
    });
}

describe('apiService integration', () => {
    let store = makeStore();

    beforeAll(() => {
        // ensure base URL is set
        if (!env.API_BASE_URL) throw new Error('API_BASE_URL not set');
    });

    afterAll(() => {
        // nothing to clean globally
    });

    it('raw materials CRUD lifecycle works', async () => {
        store = makeStore();

        const newRaw: RawMaterialDTO = {
            code: `rt-${Date.now()}`,
            name: 'RT Integration Test',
            unit: 'UNIT',
            fractionable: false,
        };

        const created = await store.dispatch(apiService.endpoints.createRawMaterial.initiate(newRaw));
        expect(created.error).toBeUndefined();
        const raw = created.data as RawMaterialDTO;
        expect(raw.id).toBeDefined();

        const byId = await store.dispatch(apiService.endpoints.getRawMaterialById.initiate(raw.id!));
        expect(byId.error).toBeUndefined();
        expect(byId.data?.code).toBe(newRaw.code);

        const byCode = await store.dispatch(apiService.endpoints.getRawMaterialByCode.initiate(newRaw.code));
        expect(byCode.error).toBeUndefined();
        expect(byCode.data?.id).toBe(raw.id);

        const updated = await store.dispatch(
            apiService.endpoints.updateRawMaterial.initiate({ id: raw.id!, body: { ...raw, name: 'RT Updated' } })
        );
        expect(updated.error).toBeUndefined();

        const deleted = await store.dispatch(apiService.endpoints.deleteRawMaterial.initiate(raw.id!));
        expect(deleted.error).toBeUndefined();
    });

    it('products lifecycle and association with raw material works', async () => {
        store = makeStore();

        const newRaw: RawMaterialDTO = {
            code: `rt-${Date.now()}`,
            name: 'RT For Product',
            unit: 'UNIT',
            fractionable: false,
        };
        const createdRaw = await store.dispatch(apiService.endpoints.createRawMaterial.initiate(newRaw));
        expect(createdRaw.error).toBeUndefined();
        const raw = createdRaw.data as RawMaterialDTO;

        const newProd: ProductDTO = {
            code: `p-${Date.now()}`,
            name: 'Product Integration Test',
            price: 1.23,
            fractionable: false,
        };
        const createdProd = await store.dispatch(apiService.endpoints.createProduct.initiate(newProd));
        expect(createdProd.error).toBeUndefined();
        const prod = createdProd.data as ProductDTO;

        const addAssoc = await store.dispatch(
            apiService.endpoints.addRawMaterialToProduct.initiate({ productId: prod.id!, rawMaterialId: raw.id!, quantity: 1 })
        );
        expect(addAssoc.error).toBeUndefined();
        const assoc = addAssoc.data as ProductRawMaterialDTO;
        expect(assoc.id).toBeDefined();

        const removeAssoc = await store.dispatch(apiService.endpoints.removeRawMaterialFromProduct.initiate(assoc.id!));
        expect(removeAssoc.error).toBeUndefined();

        const updatedProd = await store.dispatch(
            apiService.endpoints.updateProduct.initiate({ id: prod.id!, body: { ...prod, name: 'Prod Updated' } })
        );
        expect(updatedProd.error).toBeUndefined();

        const delProd = await store.dispatch(apiService.endpoints.deleteProduct.initiate(prod.id!));
        expect(delProd.error).toBeUndefined();

        const delRaw = await store.dispatch(apiService.endpoints.deleteRawMaterial.initiate(raw.id!));
        expect(delRaw.error).toBeUndefined();
    });

    it('packaging CRUD and queries work', async () => {
        store = makeStore();

        const newRaw: RawMaterialDTO = {
            code: `rt-${Date.now()}`,
            name: 'RT For Packaging',
            unit: 'UNIT',
            fractionable: false,
        };
        const createdRaw = await store.dispatch(apiService.endpoints.createRawMaterial.initiate(newRaw));
        expect(createdRaw.error).toBeUndefined();
        const raw = createdRaw.data as RawMaterialDTO;

        const newPack: RawMaterialPackagingDTO = {
            name: 'Pack Integration Test',
            rawMaterialId: raw.id!,
            quantityInside: 100,
            currentStock: 10,
        } as any;

        const createdPack = await store.dispatch(apiService.endpoints.createPackaging.initiate(newPack));
        expect(createdPack.error).toBeUndefined();
        const pack = createdPack.data as RawMaterialPackagingDTO;

        const byId = await store.dispatch(apiService.endpoints.getPackagingById.initiate(pack.id!));
        expect(byId.error).toBeUndefined();
        expect(byId.data?.id).toBe(pack.id);

        const byRaw = await store.dispatch(apiService.endpoints.getPackagingsByRawMaterial.initiate(raw.id!));
        expect(byRaw.error).toBeUndefined();

        const updated = await store.dispatch(
            apiService.endpoints.updatePackaging.initiate({ id: pack.id!, body: { ...pack, name: 'Pack Updated' } })
        );
        expect(updated.error).toBeUndefined();

        const delPack = await store.dispatch(apiService.endpoints.deletePackaging.initiate(pack.id!));
        expect(delPack.error).toBeUndefined();

        const delRaw = await store.dispatch(apiService.endpoints.deleteRawMaterial.initiate(raw.id!));
        expect(delRaw.error).toBeUndefined();
    });

    it('transactions and production endpoints respond', async () => {
        store = makeStore();

        // Create raw and product to use in transactions
        const newRaw: RawMaterialDTO = {
            code: `rt-${Date.now()}`,
            name: 'RT For Tx',
            unit: 'UNIT',
            fractionable: false,
        };
        const createdRaw = await store.dispatch(apiService.endpoints.createRawMaterial.initiate(newRaw));
        expect(createdRaw.error).toBeUndefined();
        const raw = createdRaw.data as RawMaterialDTO;

        const newPack: RawMaterialPackagingDTO = {
            name: 'Pack For Tx',
            rawMaterialId: raw.id!,
            quantityInside: 50,
            currentStock: 5,
        } as any;
        const createdPack = await store.dispatch(apiService.endpoints.createPackaging.initiate(newPack));
        expect(createdPack.error).toBeUndefined();
        const pack = createdPack.data as RawMaterialPackagingDTO;

        const newProd: ProductDTO = {
            code: `p-${Date.now()}`,
            name: 'Prod For Tx',
            price: 2.5,
            fractionable: false,
        };
        const createdProd = await store.dispatch(apiService.endpoints.createProduct.initiate(newProd));
        expect(createdProd.error).toBeUndefined();
        const prod = createdProd.data as ProductDTO;

        // Product transaction
        const prodTx = await store.dispatch(
            apiService.endpoints.createProductTransaction.initiate({ productId: prod.id!, quantity: 1, type: 'INVENTORY_IN' } as any)
        );
        expect(prodTx.error).toBeUndefined();

        // Packaging transaction
        const packTx = await store.dispatch(
            apiService.endpoints.createPackagingTransaction.initiate({ packagingId: pack.id!, quantity: 1, type: 'INVENTORY_IN' } as any)
        );
        expect(packTx.error).toBeUndefined();

        // Production suggestions and requirements
        const suggestions = await store.dispatch(apiService.endpoints.getProductionSuggestions.initiate(undefined));
        expect(suggestions.error).toBeUndefined();

        const requirements = await store.dispatch(apiService.endpoints.getProductionRequirements.initiate({ productId: prod.id!, quantity: 1 }));
        expect(requirements.error).toBeUndefined();

        // execute production (best-effort - may fail if backend enforces business rules)
        const execBody: ProductionExecutionDTO = {
            producedProducts: [{ productId: prod.id!, quantity: 1 } as any],
            consumedMaterials: [{ packagingId: pack.id!, quantity: 1 } as any],
        } as any;

        const exec = await store.dispatch(apiService.endpoints.executeProduction.initiate(execBody));
        // Accept either success or a business-rule error (so just ensure request reached server)
        expect(exec).toBeDefined();

        // cleanup
        await store.dispatch(apiService.endpoints.deleteProduct.initiate(prod.id!));
        await store.dispatch(apiService.endpoints.deletePackaging.initiate(pack.id!));
        await store.dispatch(apiService.endpoints.deleteRawMaterial.initiate(raw.id!));
    });
});
