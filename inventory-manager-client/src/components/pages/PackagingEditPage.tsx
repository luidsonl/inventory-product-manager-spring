import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useGetPackagingByIdQuery, useUpdatePackagingMutation, useGetRawMaterialsQuery } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';

export const PackagingEditPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { data: packaging } = useGetPackagingByIdQuery(Number(id!), { skip: !id });
    const [updatePackaging] = useUpdatePackagingMutation();
    const { data: rawMaterials } = useGetRawMaterialsQuery();
    const [formData, setFormData] = useState({ name: '', rawMaterialId: 0, quantityInside: 0, currentStock: 0 });

    useEffect(() => {
        if (packaging) {
            setFormData({
                name: packaging.name || '',
                rawMaterialId: packaging.rawMaterialId || 0,
                quantityInside: packaging.quantityInside || 0,
                currentStock: packaging.currentStock || 0,
            });
        }
    }, [packaging]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'number' ? parseFloat(value) : value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await updatePackaging({ id: Number(id!), body: formData as any });
        navigate('/packaging');
    };

    return (
        <MainLayout>
            <div className="max-w-md mx-auto space-y-6">
                <h1 className="text-2xl font-bold text-slate-800 text-center">Edit Packaging</h1>
                <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow-sm border border-slate-100">
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
                    <Input label="Quantity Inside" name="quantityInside" type="number" step="0.001" value={formData.quantityInside} onChange={handleChange} required />
                    <Input label="Current Stock" name="currentStock" type="number" value={formData.currentStock} onChange={handleChange} required />
                    <div className="flex gap-2 justify-end pt-2">
                        <Button type="button" onClick={() => navigate('/packaging')} className="bg-slate-100 text-slate-700 hover:bg-slate-200">Cancel</Button>
                        <Button type="submit">Save Changes</Button>
                    </div>
                </form>
            </div>
        </MainLayout>
    );
};
