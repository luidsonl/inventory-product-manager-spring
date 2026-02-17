import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useGetPackagingByIdQuery, useUpdatePackagingMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';

export const PackagingEditPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { data: packaging } = useGetPackagingByIdQuery(Number(id!), { skip: !id });
    const [updatePackaging] = useUpdatePackagingMutation();
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
            <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                <Input label="Raw Material ID" name="rawMaterialId" type="number" value={formData.rawMaterialId} onChange={handleChange} />
                <Input label="Quantity Inside" name="quantityInside" type="number" value={formData.quantityInside} onChange={handleChange} />
                <Input label="Current Stock" name="currentStock" type="number" value={formData.currentStock} onChange={handleChange} />
                <div className="flex gap-2">
                    <Button type="submit">Save</Button>
                    <Button onClick={() => navigate('/packaging')}>Cancel</Button>
                </div>
            </form>
        </MainLayout>
    );
};
