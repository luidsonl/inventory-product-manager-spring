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
            <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                <Input label="Code" name="code" value={formData.code} onChange={handleChange} required />
                <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                <Input label="Description" name="description" value={formData.description} onChange={handleChange} />
                <div className="mb-3">
                    <label className="block text-sm">Unit</label>
                    <select
                        name="unit"
                        value={formData.unit}
                        onChange={(e) => setFormData({ ...formData, unit: e.target.value })}
                        className="border p-1 w-full"
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
                <label className="flex gap-2">
                    <input type="checkbox" name="fractionable" checked={formData.fractionable} onChange={handleChange} />
                    Fractionable
                </label>
                <div className="flex gap-2">
                    <Button type="submit">Save</Button>
                    <Button onClick={() => navigate('/raw-materials')}>Cancel</Button>
                </div>
            </form>
        </MainLayout>
    );
};
