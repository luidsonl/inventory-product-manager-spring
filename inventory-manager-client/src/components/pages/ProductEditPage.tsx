import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useGetProductByIdQuery, useUpdateProductMutation, useGetRawMaterialsQuery, useAddRawMaterialToProductMutation, useRemoveRawMaterialFromProductMutation } from '../../services/api.service';
import { Input } from '../atoms/Input';
import { Button } from '../atoms/Button';
import { MainLayout } from '../templates/MainLayout';
import { Card } from '../atoms/Card';

export const ProductEditPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { data: product } = useGetProductByIdQuery(Number(id!), { skip: !id });
    const [updateProduct] = useUpdateProductMutation();
    const { data: rawMaterials } = useGetRawMaterialsQuery();
    const [addRawMaterial] = useAddRawMaterialToProductMutation();
    const [removeRawMaterial] = useRemoveRawMaterialFromProductMutation();

    const [formData, setFormData] = useState({ code: '', name: '', price: 0, fractionable: false });
    const [newAssoc, setNewAssoc] = useState({ rawMaterialId: 0, quantity: 0 });

    useEffect(() => {
        if (product) {
            setFormData({
                code: product.code || '',
                name: product.name || '',
                price: product.price || 0,
                fractionable: product.fractionable || false,
            });
        }
    }, [product]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value, type, checked } = e.currentTarget;
        setFormData({ ...formData, [name]: type === 'checkbox' ? checked : type === 'number' ? parseFloat(value) : value });
    };

    const handleAddAssoc = async () => {
        if (newAssoc.rawMaterialId && newAssoc.quantity > 0) {
            await addRawMaterial({ productId: Number(id!), ...newAssoc });
            setNewAssoc({ rawMaterialId: 0, quantity: 0 });
        }
    };

    const handleRemoveAssoc = async (assocId: number) => {
        await removeRawMaterial(assocId);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        await updateProduct({ id: Number(id!), body: formData as any });
        navigate('/products');
    };

    return (
        <MainLayout>
            <div className="space-y-6 max-w-3xl mx-auto">
                <section>
                    <h2 className="text-xl mb-3">Product Details</h2>
                    <form onSubmit={handleSubmit} className="space-y-3">
                        <Input label="Code" name="code" value={formData.code} onChange={handleChange} required />
                        <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                        <Input label="Price" name="price" type="number" step="0.01" value={formData.price} onChange={handleChange} />
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
                        <div className="flex gap-2">
                            <Button type="submit">Save Changes</Button>
                            <Button type="button" onClick={() => navigate('/products')} className="bg-slate-100 text-slate-700 hover:bg-slate-200">Back</Button>
                        </div>
                    </form>
                </section>

                <section className="border-t border-slate-200 pt-6">
                    <h2 className="text-xl mb-4 font-semibold text-slate-800">Raw Material Associations</h2>
                    <div className="space-y-4 mb-6">
                        {product?.rawMaterials?.length === 0 && <div className="text-slate-500 italic">No raw materials associated.</div>}
                        {product?.rawMaterials?.map((rm) => (
                            <Card key={rm.id}>
                                <div className="flex justify-between items-center">
                                    <div>
                                        <span className="font-semibold text-slate-800">{rm.rawMaterialName}</span>
                                        <span className="text-slate-500 mx-2">|</span>
                                        <span className="text-sm text-slate-600">Qty: {rm.quantityNeeded}</span>
                                    </div>
                                    <Button onClick={() => handleRemoveAssoc(rm.id!)} className="bg-red-50 text-red-600 hover:bg-red-100 border-red-200">Remove</Button>
                                </div>
                            </Card>
                        ))}
                    </div>

                    <div className="p-5 border border-slate-200 rounded-lg bg-slate-50">
                        <h3 className="font-semibold mb-4 text-slate-800">Add Raw Material</h3>
                        <div className="flex flex-col gap-4">
                            <div className="flex flex-col gap-1.5">
                                <label className="text-sm font-medium text-slate-700">Select Raw Material</label>
                                <select
                                    className="px-3 py-2 rounded-md border border-slate-300 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors outline-none bg-white"
                                    value={newAssoc.rawMaterialId}
                                    onChange={(e) => setNewAssoc({ ...newAssoc, rawMaterialId: Number(e.target.value) })}
                                >
                                    <option value="0">-- Select --</option>
                                    {rawMaterials?.map(rm => (
                                        <option key={rm.id} value={rm.id}>{rm.name}</option>
                                    ))}
                                </select>
                            </div>
                            <Input
                                label="Quantity"
                                type="number"
                                step="0.001"
                                value={newAssoc.quantity}
                                onChange={(e) => setNewAssoc({ ...newAssoc, quantity: Number(e.target.value) })}
                            />
                            <div className="flex justify-end mt-2">
                                <Button onClick={handleAddAssoc} disabled={!newAssoc.rawMaterialId || newAssoc.quantity <= 0}>Add Association</Button>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </MainLayout>
    );
};
