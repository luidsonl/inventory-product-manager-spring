export const Unit = {
    MILLIGRAM: 'MILLIGRAM',
    GRAM: 'GRAM',
    KILOGRAM: 'KILOGRAM',
    TON: 'TON',
    MILLILITER: 'MILLILITER',
    LITER: 'LITER',
    UNIT: 'UNIT',
    PIECE: 'PIECE',
    BOX: 'BOX',
    PACK: 'PACK',
    DOZEN: 'DOZEN',
} as const;

export type Unit = (typeof Unit)[keyof typeof Unit];

export const TransactionType = {
    INVENTORY_IN: 'INVENTORY_IN',
    INVENTORY_OUT: 'INVENTORY_OUT',
    ADJUSTMENT: 'ADJUSTMENT',
} as const;

export type TransactionType = (typeof TransactionType)[keyof typeof TransactionType];

export interface RawMaterialDTO {
    id?: number;
    code: string;
    name: string;
    description?: string;
    unit: Unit;
    fractionable: boolean;
}

export interface ProductRawMaterialDTO {
    id?: number;
    rawMaterialId: number;
    rawMaterialName?: string;
    quantityNeeded: number;
}

export interface ProductDTO {
    id?: number;
    code: string;
    name: string;
    price: number;
    fractionable: boolean;
    rawMaterials?: ProductRawMaterialDTO[];
}

export interface RawMaterialPackagingDTO {
    id?: number;
    name: string;
    rawMaterialId: number;
    quantityInside: number;
    currentStock: number;
}

export interface ConsumedMaterialInputDTO {
    packagingId: number;
    quantity: number;
}

export interface ProducedProductInputDTO {
    productId: number;
    quantity: number;
}

export interface ProductionExecutionDTO {
    producedProducts: ProducedProductInputDTO[];
    consumedMaterials: ConsumedMaterialInputDTO[];
    note?: string;
}

export interface ProductTransactionDTO {
    id?: number;
    productId: number;
    productName?: string;
    quantity: number;
    type: TransactionType;
    transactionDate?: string;
    note?: string;
}

export interface RawMaterialPackagingTransactionDTO {
    id?: number;
    packagingId: number;
    packagingName?: string;
    quantity: number;
    type: TransactionType;
    transactionDate?: string;
    note?: string;
}

export interface SuggestedProductDTO {
    productId: number;
    productName: string;
    quantityToProduce: number;
    unitPrice: number;
    totalPrice: number;
}

export interface ProductionSuggestionDTO {
    products: SuggestedProductDTO[];
    grandTotalValue: number;
}

export interface MaterialItemDTO {
    rawMaterialId: number;
    rawMaterialName: string;
    totalNeededQuantity: number;
    unit: string;
}

export interface MaterialRequirementDTO {
    productId: number;
    productName: string;
    productionQuantity: number;
    materials: MaterialItemDTO[];
}
