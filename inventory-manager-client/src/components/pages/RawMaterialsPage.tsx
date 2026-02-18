import React, { useState } from 'react';
import { RawMaterialList } from '../organisms/RawMaterialList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { Input } from '../atoms/Input';
import { Modal } from '../molecules/Modal';
import { useCreateRawMaterialMutation, useUpdateRawMaterialMutation, useDeleteRawMaterialMutation, useGetRawMaterialsQuery } from '../../services/api.service';

export const RawMaterialsPage: React.FC = () => {
    const [showForm, setShowForm] = useState(false);
    const [createRawMaterial] = useCreateRawMaterialMutation();
    const [updateRawMaterial] = useUpdateRawMaterialMutation();
    const [deleteRawMaterial] = useDeleteRawMaterialMutation();
    const { data: rawMaterials } = useGetRawMaterialsQuery(); // To find item by ID for editing

    const [editingId, setEditingId] = useState<number | null>(null);
    const [formData, setFormData] = useState({ code: '', name: '', description: '', unit: 'UNIT', fractionable: false });

    const handleCreate = () => {
        setEditingId(null);
        setFormData({ code: '', name: '', description: '', unit: 'UNIT', fractionable: false });
        setShowForm(true);
    };

    const handleEdit = (id: number) => {
        const item = rawMaterials?.find(r => r.id === id);
        if (item) {
            setEditingId(id);
            setFormData({
                code: item.code || '',
                name: item.name || '',
                description: item.description || '',
                unit: item.unit || 'UNIT',
                fractionable: item.fractionable || false
            });
            setShowForm(true);
        }
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Are you sure you want to delete this raw material?')) {
            await deleteRawMaterial(id);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (editingId) {
            await updateRawMaterial({ id: editingId, body: formData as any });
        } else {
            await createRawMaterial(formData as any);
        }
        setShowForm(false);
    };

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-slate-800">Raw Materials</h1>
                <Button onClick={handleCreate}>
                    Create New
                </Button>
            </div>

            <RawMaterialList onEdit={handleEdit} onDelete={handleDelete} />

            <Modal isOpen={showForm} onClose={() => setShowForm(false)} title={editingId ? "Edit Raw Material" : "Create New Raw Material"}>
                <form onSubmit={handleSubmit} className="space-y-4">
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
                    <div className="flex gap-3 justify-end pt-4">
                        <Button type="button" onClick={() => setShowForm(false)} className="bg-slate-100 text-slate-700 hover:bg-slate-200">
                            Cancel
                        </Button>
                        <Button type="submit">Save</Button>
                    </div>
                </form>
            </Modal>
        </MainLayout>
    );
};
