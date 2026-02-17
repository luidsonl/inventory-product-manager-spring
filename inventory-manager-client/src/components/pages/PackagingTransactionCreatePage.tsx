import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useGetPackagingsQuery, useCreatePackagingTransactionMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';
import { TransactionType } from '../../services/api.types';

export const PackagingTransactionCreatePage: React.FC = () => {
    const navigate = useNavigate();
    const { data: packagings } = useGetPackagingsQuery();
    const [createPackagingTx] = useCreatePackagingTransactionMutation();
    const [formData, setFormData] = useState({ packagingId: 0, quantity: 0, type: TransactionType.INVENTORY_IN, note: '' });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.packagingId) return;
        await createPackagingTx(formData as any);
        navigate('/transactions/packaging');
    };

    return (
        <MainLayout>
            <h1 className="text-2xl mb-4">New Packaging Transaction</h1>
            <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                <div>
                    <label className="block text-sm">Packaging</label>
                    <select
                        className="border p-1 w-full"
                        value={formData.packagingId}
                        onChange={(e) => setFormData({ ...formData, packagingId: Number(e.target.value) })}
                        required
                    >
                        <option value="0">-- Select --</option>
                        {packagings?.map(pk => <option key={pk.id} value={pk.id}>{pk.name}</option>)}
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
                    <Button type="button" onClick={() => navigate('/transactions/packaging')}>Cancel</Button>
                </div>
            </form>
        </MainLayout>
    );
};
