import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCreatePackagingMutation, useGetRawMaterialsQuery } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';

export const PackagingCreatePage: React.FC = () => {
    const navigate = useNavigate();
    const [createPackaging] = useCreatePackagingMutation();
    const { data: rawMaterials } = useGetRawMaterialsQuery();
    const [formData, setFormData] = useState({ name: '', rawMaterialId: 0, quantityInside: 0, currentStock: 0 });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'number' ? parseFloat(value) : value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.rawMaterialId) {
            alert('Please select a raw material');
            return;
        }
        await createPackaging(formData as any);
        navigate('/packaging');
    };

    return (
        <MainLayout>
            <h1 className="text-2xl mb-4">Create New Packaging</h1>
            <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                <div>
                    <label className="block text-sm">Raw Material</label>
                    <select
                        name="rawMaterialId"
                        className="border p-1 w-full"
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
                <Input label="Quantity Inside" name="quantityInside" type="number" value={formData.quantityInside} onChange={handleChange} required />
                <Input label="Current Stock" name="currentStock" type="number" value={formData.currentStock} onChange={handleChange} required />

                <div className="flex gap-2">
                    <Button type="submit">Save</Button>
                    <Button onClick={() => navigate('/packaging')}>Cancel</Button>
                </div>
            </form>
        </MainLayout>
    );
};
