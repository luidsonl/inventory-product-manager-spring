import React, { useState, useEffect } from 'react';
import { useGetProductionSuggestionsQuery, useExecuteProductionMutation, useGetProductsQuery, useGetPackagingsQuery } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';
import { Card } from '../atoms/Card';
import type { ProductionExecutionDTO } from '../../services/api.types';

export const ProductionPage: React.FC = () => {
    const { data: suggestions, isLoading: suggestionsLoading } = useGetProductionSuggestionsQuery();
    const { data: products } = useGetProductsQuery();
    const { data: packagings } = useGetPackagingsQuery();

    const [selectedProductId, setSelectedProductId] = useState<number>(0);
    const [quantity, setQuantity] = useState<number>(0);
    const [executeProduction] = useExecuteProductionMutation();
    const [consumedMaterials, setConsumedMaterials] = useState<ProductionExecutionDTO['consumedMaterials']>([]);
    const [note, setNote] = useState('');

    const selectedProduct = products?.find(p => p.id === selectedProductId);

    useEffect(() => {
        if (selectedProduct && quantity > 0 && selectedProduct.rawMaterials) {
            const requirements = selectedProduct.rawMaterials.map(rm => {
                const requiredQty = (rm.quantityNeeded || 0) * quantity;
                // Find best packaging (simplistic: first available)
                const availablePackaging = packagings?.filter(p => p.rawMaterialId === rm.rawMaterialId) || [];
                const defaultPackaging = availablePackaging[0];
                return {
                    rawMaterialId: rm.rawMaterialId,
                    rawMaterialName: rm.rawMaterialName,
                    packagingId: defaultPackaging?.id || 0,
                    quantity: requiredQty
                };
            });
            setConsumedMaterials(requirements.map(r => ({ packagingId: r.packagingId, quantity: r.quantity })));
        } else {
            setConsumedMaterials([]);
        }
    }, [selectedProductId, quantity, products, packagings]);

    const handleConstraintChange = (index: number, field: 'packagingId' | 'quantity', value: number) => {
        const newConsumed = [...consumedMaterials];
        newConsumed[index] = { ...newConsumed[index], [field]: value };
        setConsumedMaterials(newConsumed);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedProductId || quantity <= 0) return;

        const execBody: ProductionExecutionDTO = {
            producedProducts: [{ productId: selectedProductId, quantity }],
            consumedMaterials,
            note: note || undefined,
        };
        await executeProduction(execBody);
        setSelectedProductId(0);
        setQuantity(0);
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
                <div className="space-y-6">
                    <section className="p-4 border">
                        <h2 className="text-xl mb-3 font-semibold">Execute Production</h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <label className="block text-sm">Product to Produce</label>
                                    <select
                                        className="border p-1 w-full"
                                        value={selectedProductId}
                                        onChange={(e) => setSelectedProductId(Number(e.target.value))}
                                        required
                                    >
                                        <option value="0">-- Select Product --</option>
                                        {products?.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                                    </select>
                                </div>
                                <Input label="Quantity" type="number" value={quantity} onChange={(e) => setQuantity(Number(e.target.value))} required />
                            </div>

                            {selectedProduct && selectedProduct.rawMaterials && selectedProduct.rawMaterials.length > 0 && (
                                <div>
                                    <h3 className="font-semibold mb-2">Materials to Consume</h3>
                                    <table className="w-full text-sm">
                                        <thead>
                                            <tr>
                                                <th className="text-left">Raw Material</th>
                                                <th className="text-left">Packaging Source</th>
                                                <th className="text-left">Quantity</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {selectedProduct.rawMaterials.map((rm, index) => {
                                                const availablePackaging = packagings?.filter(p => p.rawMaterialId === rm.rawMaterialId);
                                                return (
                                                    <tr key={rm.rawMaterialId}>
                                                        <td>{rm.rawMaterialName}</td>
                                                        <td>
                                                            <select
                                                                className="border p-1 w-full"
                                                                value={consumedMaterials[index]?.packagingId || 0}
                                                                onChange={(e) => handleConstraintChange(index, 'packagingId', Number(e.target.value))}
                                                            >
                                                                <option value="0">-- Select Stock --</option>
                                                                {availablePackaging?.map(p => (
                                                                    <option key={p.id} value={p.id}>{p.name} (Stock: {p.currentStock})</option>
                                                                ))}
                                                            </select>
                                                        </td>
                                                        <td>
                                                            <Input
                                                                type="number"
                                                                value={consumedMaterials[index]?.quantity || 0}
                                                                onChange={(e) => handleConstraintChange(index, 'quantity', Number(e.target.value))}
                                                            />
                                                        </td>
                                                    </tr>
                                                );
                                            })}
                                        </tbody>
                                    </table>
                                </div>
                            )}

                            <Input label="Production Note" value={note} onChange={(e) => setNote(e.target.value)} />

                            <Button type="submit" disabled={!selectedProductId || quantity <= 0}>Execute Production</Button>
                        </form>
                    </section>
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
