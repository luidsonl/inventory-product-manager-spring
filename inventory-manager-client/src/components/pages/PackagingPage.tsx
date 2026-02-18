import React, { useState } from 'react';
import { PackagingList } from '../organisms/PackagingList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { Modal } from '../molecules/Modal';
import { Input } from '../atoms/Input';
import { useCreatePackagingMutation, useGetRawMaterialsQuery } from '../../services/api.service';

export const PackagingPage: React.FC = () => {
    const [showForm, setShowForm] = useState(false);
    const [createPackaging] = useCreatePackagingMutation();
    const { data: rawMaterials } = useGetRawMaterialsQuery();
    const [formData, setFormData] = useState<{
        name: string;
        rawMaterialId: number;
        quantityInside: number | string;
        currentStock: number | string;
    }>({ name: '', rawMaterialId: 0, quantityInside: '', currentStock: '' });

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
        if (!formData.rawMaterialId) {
            alert('Please select a raw material');
            return;
        }
        await createPackaging({
            ...formData,
            quantityInside: Number(formData.quantityInside),
            currentStock: Number(formData.currentStock)
        } as any);
        setFormData({ name: '', rawMaterialId: 0, quantityInside: '', currentStock: '' });
        setShowForm(false);
    };

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Packaging</h1>
                <Button onClick={() => setShowForm(true)}>Create New</Button>
            </div>

            <PackagingList />

            <Modal isOpen={showForm} onClose={() => setShowForm(false)} title="Create New Packaging">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-medium text-slate-700">Raw Material</label>
                        <select
                            name="rawMaterialId"
                            className="px-3 py-2 rounded-md border border-slate-300 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors outline-none bg-white"
                            value={formData.rawMaterialId}
                            onChange={(e) => setFormData({ ...formData, rawMaterialId: Number(e.target.value) })}
                            required
                        >
                            <option value="0">-- Select --</option>
                            {rawMaterials?.map(rm => (
                                <option key={rm.id} value={rm.id}>{rm.name}</option>
                            ))}
                        </select>
                    </div>
                    <Input label="Quantity Inside" name="quantityInside" type="number" step="0.01" value={formData.quantityInside || ''} onChange={handleChange} required />
                    <Input label="Current Stock" name="currentStock" type="number" value={formData.currentStock || ''} onChange={handleChange} required />

                    <div className="flex gap-3 justify-end pt-4">
                        <Button type="button" onClick={() => setShowForm(false)} className="bg-slate-100 text-slate-700 hover:bg-slate-200">Cancel</Button>
                        <Button type="submit">Save</Button>
                    </div>
                </form>
            </Modal>
        </MainLayout>
    );
};
