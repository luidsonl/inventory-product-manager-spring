import React, { useState } from 'react';
import { useGetProductionSuggestionsQuery, useGetProductionRequirementsQuery, useExecuteProductionMutation, useGetProductsQuery, useGetPackagingsQuery } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';
import { Card } from '../atoms/Card';
import type { ProductionExecutionDTO } from '../../services/api.types';

export const ProductionPage: React.FC = () => {
    const { data: suggestions, isLoading: suggestionsLoading } = useGetProductionSuggestionsQuery();
    const { data: products } = useGetProductsQuery();
    const { data: packagings } = useGetPackagingsQuery();

    const [selectedProductId, setSelectedProductId] = useState<number | ''>('');
    const [selectedQuantity, setSelectedQuantity] = useState(0);
    const { data: requirements } = useGetProductionRequirementsQuery(
        { productId: selectedProductId as number, quantity: selectedQuantity },
        { skip: !selectedProductId || selectedQuantity <= 0 }
    );

    const [executeProduction] = useExecuteProductionMutation();
    const [producedProducts, setProducedProducts] = useState<{ productId: number; quantity: number }[]>([]);
    const [consumedMaterials, setConsumedMaterials] = useState<{ packagingId: number; quantity: number }[]>([]);
    const [note, setNote] = useState('');

    const handleAddProducedProduct = () => {
        if (selectedProductId) {
            setProducedProducts([...producedProducts, { productId: selectedProductId as number, quantity: selectedQuantity }]);
            setSelectedProductId('');
            setSelectedQuantity(0);
        }
    };

    const handleAddConsumedMaterial = (packagingId: number, quantity: number) => {
        setConsumedMaterials([...consumedMaterials, { packagingId, quantity }]);
    };

    const handleExecuteProduction = async (e: React.FormEvent) => {
        e.preventDefault();
        const execBody: ProductionExecutionDTO = {
            producedProducts,
            consumedMaterials,
            note: note || undefined,
        };
        await executeProduction(execBody);
        setProducedProducts([]);
        setConsumedMaterials([]);
        setNote('');
    };

    return (
        <MainLayout >
            <h1 className="text-2xl mb-4">Production Management</h1>

            <div className="grid grid-cols-2 gap-4">
                {/* Production Suggestions */}
                <div>
                    <h2 className="text-xl mb-3">Production Suggestions</h2>
                    {suggestionsLoading ? (
                        <div>Loading...</div>
                    ) : suggestions?.products ? (
                        <div className="space-y-2">
                            {suggestions.products.map((s) => (
                                <Card key={s.productId}>
                                    <div className="text-sm">{s.productName}: {s.quantityToProduce}</div>
                                    <div className="text-sm">Price: ${s.totalPrice}</div>
                                </Card>
                            ))}
                            <div className="text-sm font-bold">Total: ${suggestions.grandTotalValue}</div>
                        </div>
                    ) : (
                        <div>No suggestions available</div>
                    )}
                </div>

                {/* Production Execution Form */}
                <div>
                    <h2 className="text-xl mb-3">Execute Production</h2>
                    <form onSubmit={handleExecuteProduction} className="space-y-3">
                        <div>
                            <label className="block text-sm">Select Product</label>
                            <select
                                value={selectedProductId}
                                onChange={(e) => setSelectedProductId(e.target.value ? Number(e.target.value) : '')}
                                className="border p-1 w-full"
                            >
                                <option value="">-- Select --</option>
                                {products?.map((p) => (
                                    <option key={p.id} value={p.id}>
                                        {p.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <Input label="Quantity" type="number" value={selectedQuantity} onChange={(e) => setSelectedQuantity(Number(e.target.value))} />
                        <Button type="button" onClick={handleAddProducedProduct}>Add to Production</Button>

                        {/* Material Requirements */}
                        {requirements && (
                            <div className="mt-4 p-2 border">
                                <h3 className="font-bold mb-2">Material Requirements</h3>
                                {requirements.materials?.map((m) => (
                                    <Card key={m.rawMaterialId}>
                                        <div className="text-sm">{m.rawMaterialName}: {m.totalNeededQuantity} {m.unit}</div>
                                    </Card>
                                ))}
                            </div>
                        )}

                        {/* Produced Products List */}
                        {producedProducts.length > 0 && (
                            <div className="mt-4 p-2 border">
                                <h3 className="font-bold mb-2">Products to Produce</h3>
                                {producedProducts.map((p, i) => (
                                    <div key={i} className="text-sm">{p.productId}: {p.quantity}</div>
                                ))}
                            </div>
                        )}

                        {/* Consumed Materials Selection */}
                        <div className="mt-4 p-2 border">
                            <h3 className="font-bold mb-2">Select Consumed Materials</h3>
                            {packagings?.map((pk) => (
                                <div key={pk.id} className="text-sm mb-2 flex gap-2">
                                    <span>{pk.name}</span>
                                    <input
                                        type="number"
                                        placeholder="Qty"
                                        className="border px-1 w-20"
                                        min={0}
                                        onChange={(e) => {
                                            if (e.target.value) {
                                                handleAddConsumedMaterial(pk.id!, Number(e.target.value));
                                                e.target.value = '';
                                            }
                                        }}
                                    />
                                </div>
                            ))}
                        </div>

                        <Input label="Production Note" value={note} onChange={(e) => setNote(e.target.value)} />

                        <div className="flex gap-2">
                            <Button type="submit">Execute Production</Button>
                        </div>
                    </form>
                </div>
            </div>

            {/* Transaction History */}
            <div className="mt-6">
                <h2 className="text-xl mb-3">Recent Transactions</h2>
                <ProductTransactionList />
            </div>
        </MainLayout>
    );
};

// Import for display
import { ProductTransactionList } from '../organisms/ProductTransactionList';
