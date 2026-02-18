import React, { useState } from 'react';
import { PackagingTransactionList } from '../organisms/PackagingTransactionList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { Modal } from '../molecules/Modal';
import { Input } from '../atoms/Input';
import { useGetPackagingsQuery, useCreatePackagingTransactionMutation } from '../../services/api.service';
import { TransactionType } from '../../services/api.types';

export const PackagingTransactionsPage: React.FC = () => {
    const [showForm, setShowForm] = useState(false);
    const { data: packagings } = useGetPackagingsQuery();
    const [createPackagingTx] = useCreatePackagingTransactionMutation();
    const [formData, setFormData] = useState<{
        packagingId: number;
        quantity: number | string;
        type: TransactionType;
        note: string;
    }>({ packagingId: 0, quantity: '', type: TransactionType.INVENTORY_IN, note: '' });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type } = e.currentTarget;
        if (type === 'number') {
            let niceValue = value;
            if (value.length > 1 && value.startsWith('0') && value[1] !== '.') {
                niceValue = value.replace(/^0+/, '');
            }
            setFormData({ ...formData, [name]: niceValue });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.packagingId) return;
        await createPackagingTx({ ...formData, quantity: Number(formData.quantity) } as any);
        setFormData({ packagingId: 0, quantity: '', type: TransactionType.INVENTORY_IN, note: '' });
        setShowForm(false);
    };

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Packaging Transactions</h1>
                <Button onClick={() => setShowForm(true)}>New Transaction</Button>
            </div>

            <PackagingTransactionList />

            <Modal isOpen={showForm} onClose={() => setShowForm(false)} title="New Packaging Transaction">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-medium text-slate-700">Packaging</label>
                        <select
                            className="px-3 py-2 rounded-md border border-slate-300 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors outline-none bg-white"
                            value={formData.packagingId}
                            onChange={(e) => setFormData({ ...formData, packagingId: Number(e.target.value) })}
                            required
                        >
                            <option value="0">-- Select --</option>
                            {packagings?.map(pk => <option key={pk.id} value={pk.id}>{pk.name}</option>)}
                        </select>
                    </div>
                    <Input label="Quantity" name="quantity" type="number" step="0.01" value={formData.quantity || ''} onChange={handleChange} required />
                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-medium text-slate-700">Type</label>
                        <select
                            className="px-3 py-2 rounded-md border border-slate-300 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors outline-none bg-white"
                            value={formData.type}
                            onChange={(e) => setFormData({ ...formData, type: e.target.value as any })}
                        >
                            <option value="INVENTORY_IN">Inventory In</option>
                            <option value="INVENTORY_OUT">Inventory Out</option>
                            <option value="ADJUSTMENT">Adjustment</option>
                        </select>
                    </div>
                    <Input label="Note" name="note" value={formData.note} onChange={handleChange} />
                    <div className="flex gap-3 justify-end pt-4">
                        <Button type="button" onClick={() => setShowForm(false)} className="bg-slate-100 text-slate-700 hover:bg-slate-200">Cancel</Button>
                        <Button type="submit">Record</Button>
                    </div>
                </form>
            </Modal>
        </MainLayout>
    );
};
