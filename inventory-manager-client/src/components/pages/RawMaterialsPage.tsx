import React, { useState } from 'react';
import { RawMaterialList } from '../organisms/RawMaterialList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { Input } from '../atoms/Input';
import { useCreateRawMaterialMutation } from '../../services/api.service';

export const RawMaterialsPage: React.FC = () => {
    const [showForm, setShowForm] = useState(false);
    const [createRawMaterial] = useCreateRawMaterialMutation();
    const [formData, setFormData] = useState({ code: '', name: '', description: '', unit: 'UNIT', fractionable: false });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await createRawMaterial(formData as any);
        setFormData({ code: '', name: '', description: '', unit: 'UNIT', fractionable: false });
        setShowForm(false);
    };

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl">Raw Materials</h1>
                <Button onClick={() => setShowForm(!showForm)}>
                    {showForm ? 'Cancel' : 'Create New'}
                </Button>
            </div>

            {showForm && (
                <form onSubmit={handleSubmit} className="max-w-md mb-6 p-4 border">
                    <h2 className="text-lg mb-3">Create New Raw Material</h2>
                    <Input
                        label="Code"
                        name="code"
                        value={formData.code}
                        onChange={handleChange}
                        required
                    />
                    <Input
                        label="Name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                    <Input
                        label="Description"
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                    />
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
                    <label className="flex gap-2 mb-3">
                        <input
                            type="checkbox"
                            name="fractionable"
                            checked={formData.fractionable}
                            onChange={handleChange}
                        />
                        <span>Fractionable</span>
                    </label>
                    <div className="flex gap-2">
                        <Button type="submit">Save</Button>
                        <Button type="button" onClick={() => setShowForm(false)}>
                            Cancel
                        </Button>
                    </div>
                </form>
            )}

            <RawMaterialList />
        </MainLayout>
    );
};
