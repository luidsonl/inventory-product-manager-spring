import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useGetRawMaterialByIdQuery, useUpdateRawMaterialMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';

export const RawMaterialEditPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { data: rawMaterial } = useGetRawMaterialByIdQuery(Number(id!), { skip: !id });
    const [updateRawMaterial] = useUpdateRawMaterialMutation();
    const [formData, setFormData] = useState({ code: '', name: '', description: '', unit: 'UNIT', fractionable: false });

    useEffect(() => {
        if (rawMaterial) {
            setFormData({
                code: rawMaterial.code || '',
                name: rawMaterial.name || '',
                description: rawMaterial.description || '',
                unit: rawMaterial.unit || 'UNIT',
                fractionable: rawMaterial.fractionable || false,
            });
        }
    }, [rawMaterial]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await updateRawMaterial({ id: Number(id!), body: formData as any });
        navigate('/raw-materials');
    };

    return (
        <MainLayout>
            <div className="max-w-md mx-auto space-y-6">
                <h1 className="text-2xl font-bold text-slate-800 text-center">Edit Raw Material</h1>
                <form onSubmit={handleSubmit} className="space-y-4 bg-white p-6 rounded-lg shadow-sm border border-slate-100">
                    <Input label="Code" name="code" value={formData.code} onChange={handleChange} required />
                    <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                    <Input label="Description" name="description" value={formData.description} onChange={handleChange} />
                    <div className="flex flex-col gap-1.5">
                        <label className="text-sm font-medium text-slate-700">Unit</label>
                        <select
                            name="unit"
                            value={formData.unit}
                            onChange={(e) => setFormData({ ...formData, unit: e.target.value })}
                            className="px-3 py-2 rounded-md border border-slate-300 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors outline-none bg-white"
                        >
                            <option value="MILLIGRAM">Milligram</option>
                            <option value="GRAM">Gram</option>
                            <option value="KILOGRAM">Kilogram</option>
                            <option value="TON">Ton</option>
                            <option value="MILLILITER">Milliliter</option>
                            <option value="LITER">Liter</option>
                            <option value="UNIT">Unit</option>
                            <option value="PIECE">Piece</option>
                            <option value="BOX">Box</option>
                            <option value="PACK">Pack</option>
                            <option value="DOZEN">Dozen</option>
                        </select>
                    </div>
                    <label className="flex items-center gap-2 cursor-pointer">
                        <input
                            type="checkbox"
                            name="fractionable"
                            checked={formData.fractionable}
                            onChange={handleChange}
                            className="rounded border-slate-300 text-blue-600 focus:ring-blue-500"
                        />
                        <span className="text-sm font-medium text-slate-700">Fractionable</span>
                    </label>
                    <div className="flex gap-2 justify-end pt-2">
                        <Button type="button" onClick={() => navigate('/raw-materials')} className="bg-slate-100 text-slate-700 hover:bg-slate-200">Cancel</Button>
                        <Button type="submit">Save Changes</Button>
                    </div>
                </form>
            </div>
        </MainLayout>
    );
};
