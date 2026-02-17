import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCreateProductMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';

export const ProductCreatePage: React.FC = () => {
    const navigate = useNavigate();
    const [createProduct] = useCreateProductMutation();
    const [formData, setFormData] = useState({ code: '', name: '', price: 0, fractionable: false });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : type === 'number' ? parseFloat(value) : value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await createProduct(formData as any);
        navigate('/products');
    };

    return (
        <MainLayout>
            <h1 className="text-2xl mb-4">Create New Product</h1>
            <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                <Input label="Code" name="code" value={formData.code} onChange={handleChange} required />
                <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                <Input label="Price" name="price" type="number" value={formData.price} onChange={handleChange} />
                <label className="flex gap-2">
                    <input type="checkbox" name="fractionable" checked={formData.fractionable} onChange={handleChange} />
                    Fractionable
                </label>
                <div className="flex gap-2">
                    <Button type="submit">Save</Button>
                    <Button onClick={() => navigate('/products')}>Cancel</Button>
                </div>
            </form>
        </MainLayout>
    );
};
