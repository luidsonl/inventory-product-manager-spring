import React, { useState } from 'react';
import { ProductTransactionList } from '../organisms/ProductTransactionList';
import { PackagingTransactionList } from '../organisms/PackagingTransactionList';
import { MainLayout } from '../templates/MainLayout';
import { useGetProductsQuery, useGetPackagingsQuery, useCreateProductTransactionMutation, useCreatePackagingTransactionMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { TransactionType } from '../../services/api.types';

export const TransactionsPage: React.FC = () => {
    const { data: products } = useGetProductsQuery();
    const { data: packagings } = useGetPackagingsQuery();
    const [createProductTx] = useCreateProductTransactionMutation();
    const [createPackagingTx] = useCreatePackagingTransactionMutation();

    const [productFormData, setProductFormData] = useState({ productId: 0, quantity: 0, type: TransactionType.INVENTORY_IN, note: '' });
    const [packagingFormData, setPackagingFormData] = useState({ packagingId: 0, quantity: 0, type: TransactionType.INVENTORY_IN, note: '' });

    const handleProductSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!productFormData.productId) return;
        await createProductTx(productFormData as any);
        setProductFormData({ productId: 0, quantity: 0, type: TransactionType.INVENTORY_IN, note: '' });
    };

    const handlePackagingSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!packagingFormData.packagingId) return;
        await createPackagingTx(packagingFormData as any);
        setPackagingFormData({ packagingId: 0, quantity: 0, type: TransactionType.INVENTORY_IN, note: '' });
    };

    return (
        <MainLayout>
            <h1 className="text-2xl mb-6">Stock Transactions</h1>

            <div className="grid md:grid-cols-2 gap-8">
                {/* Product Transactions */}
                <div className="space-y-6">
                    <section className="p-4 border">
                        <h2 className="text-xl mb-3 font-semibold">Record Product Transaction</h2>
                        <form onSubmit={handleProductSubmit} className="space-y-3">
                            <div>
                                <label className="block text-sm">Product</label>
                                <select
                                    className="border p-1 w-full"
                                    value={productFormData.productId}
                                    onChange={(e) => setProductFormData({ ...productFormData, productId: Number(e.target.value) })}
                                    required
                                >
                                    <option value="0">-- Select --</option>
                                    {products?.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                                </select>
                            </div>
                            <Input label="Quantity" type="number" value={productFormData.quantity} onChange={(e) => setProductFormData({ ...productFormData, quantity: Number(e.target.value) })} required />
                            <div>
                                <label className="block text-sm">Type</label>
                                <select
                                    className="border p-1 w-full"
                                    value={productFormData.type}
                                    onChange={(e) => setProductFormData({ ...productFormData, type: e.target.value as any })}
                                >
                                    <option value="INVENTORY_IN">Inventory In</option>
                                    <option value="INVENTORY_OUT">Inventory Out</option>
                                    <option value="ADJUSTMENT">Adjustment</option>
                                </select>
                            </div>
                            <Input label="Note" value={productFormData.note} onChange={(e) => setProductFormData({ ...productFormData, note: e.target.value })} />
                            <Button type="submit">Record</Button>
                        </form>
                    </section>

                    <section>
                        <h3 className="text-lg mb-3 font-semibold">Product History</h3>
                        <ProductTransactionList />
                    </section>
                </div>

                {/* Packaging Transactions */}
                <div className="space-y-6">
                    <section className="p-4 border">
                        <h2 className="text-xl mb-3 font-semibold">Record Packaging Transaction</h2>
                        <form onSubmit={handlePackagingSubmit} className="space-y-3">
                            <div>
                                <label className="block text-sm">Packaging</label>
                                <select
                                    className="border p-1 w-full"
                                    value={packagingFormData.packagingId}
                                    onChange={(e) => setPackagingFormData({ ...packagingFormData, packagingId: Number(e.target.value) })}
                                    required
                                >
                                    <option value="0">-- Select --</option>
                                    {packagings?.map(pk => <option key={pk.id} value={pk.id}>{pk.name}</option>)}
                                </select>
                            </div>
                            <Input label="Quantity" type="number" value={packagingFormData.quantity} onChange={(e) => setPackagingFormData({ ...packagingFormData, quantity: Number(e.target.value) })} required />
                            <div>
                                <label className="block text-sm">Type</label>
                                <select
                                    className="border p-1 w-full"
                                    value={packagingFormData.type}
                                    onChange={(e) => setPackagingFormData({ ...packagingFormData, type: e.target.value as any })}
                                >
                                    <option value="INVENTORY_IN">Inventory In</option>
                                    <option value="INVENTORY_OUT">Inventory Out</option>
                                    <option value="ADJUSTMENT">Adjustment</option>
                                </select>
                            </div>
                            <Input label="Note" value={packagingFormData.note} onChange={(e) => setPackagingFormData({ ...packagingFormData, note: e.target.value })} />
                            <Button type="submit">Record</Button>
                        </form>
                    </section>

                    <section>
                        <h3 className="text-lg mb-3 font-semibold">Packaging History</h3>
                        <PackagingTransactionList />
                    </section>
                </div>
            </div>
        </MainLayout>
    );
};
