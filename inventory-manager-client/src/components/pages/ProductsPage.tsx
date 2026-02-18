import React, { useState } from 'react';
import { ProductList } from '../organisms/ProductList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { Modal } from '../molecules/Modal';
import { Input } from '../atoms/Input';
import { useCreateProductMutation } from '../../services/api.service';

export const ProductsPage: React.FC = () => {
    const [showForm, setShowForm] = useState(false);
    const [createProduct] = useCreateProductMutation();
    const [formData, setFormData] = useState<{
        code: string;
        name: string;
        price: number | string;
        fractionable: boolean;
    }>({ code: '', name: '', price: '', fractionable: false });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.currentTarget;
        if (type === 'number') {
            // Remove leading zero if it's not "0" or "0."
            let niceValue = value;
            if (value.length > 1 && value.startsWith('0') && value[1] !== '.') {
                niceValue = value.replace(/^0+/, '');
            }
            setFormData({ ...formData, [name]: niceValue });
        } else {
            setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await createProduct({ ...formData, price: Number(formData.price) } as any);
        setFormData({ code: '', name: '', price: '', fractionable: false });
        setShowForm(false);
    };

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Products</h1>
                <Button onClick={() => setShowForm(true)}>Create New</Button>
            </div>

            <ProductList />

            <Modal isOpen={showForm} onClose={() => setShowForm(false)} title="Create New Product">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <Input label="Code" name="code" value={formData.code} onChange={handleChange} required />
                    <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                    <Input label="Price" name="price" type="number" step="0.01" value={formData.price || ''} onChange={handleChange} />
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
                    <div className="flex gap-3 justify-end pt-4">
                        <Button type="button" onClick={() => setShowForm(false)} className="bg-slate-100 text-slate-700 hover:bg-slate-200">Cancel</Button>
                        <Button type="submit">Save</Button>
                    </div>
                </form>
            </Modal>
        </MainLayout>
    );
};
