import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGetProductsQuery, useCreateProductTransactionMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';
import { TransactionType } from '../../services/api.types';

export const ProductTransactionCreatePage: React.FC = () => {
    const navigate = useNavigate();
    const { data: products } = useGetProductsQuery();
    const [createProductTx] = useCreateProductTransactionMutation();
    const [formData, setFormData] = useState({ productId: 0, quantity: 0, type: TransactionType.INVENTORY_IN, note: '' });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.productId) return;
        await createProductTx(formData as any);
        navigate('/transactions/products');
    };

    return (
        <MainLayout>
            <h1 className="text-2xl mb-4">New Product Transaction</h1>
            <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                <div>
                    <label className="block text-sm">Product</label>
                    <select
                        className="border p-1 w-full"
                        value={formData.productId}
                        onChange={(e) => setFormData({ ...formData, productId: Number(e.target.value) })}
                        required
                    >
                        <option value="0">-- Select --</option>
                        {products?.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                    </select>
                </div>
                <Input label="Quantity" type="number" value={formData.quantity} onChange={(e) => setFormData({ ...formData, quantity: Number(e.target.value) })} required />
                <div>
                    <label className="block text-sm">Type</label>
                    <select
                        className="border p-1 w-full"
                        value={formData.type}
                        onChange={(e) => setFormData({ ...formData, type: e.target.value as any })}
                    >
                        <option value="INVENTORY_IN">Inventory In</option>
                        <option value="INVENTORY_OUT">Inventory Out</option>
                        <option value="ADJUSTMENT">Adjustment</option>
                    </select>
                </div>
                <Input label="Note" value={formData.note} onChange={(e) => setFormData({ ...formData, note: e.target.value })} />
                <div className="flex gap-2">
                    <Button type="submit">Record</Button>
                    <Button type="button" onClick={() => navigate('/transactions/products')}>Cancel</Button>
                </div>
            </form>
        </MainLayout>
    );
};
