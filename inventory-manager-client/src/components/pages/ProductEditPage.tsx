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
            <div className="space-y-6">
                <section>
                    <h2 className="text-xl mb-3">Product Details</h2>
                    <form onSubmit={handleSubmit} className="max-w-md space-y-3">
                        <Input label="Code" name="code" value={formData.code} onChange={handleChange} required />
                        <Input label="Name" name="name" value={formData.name} onChange={handleChange} required />
                        <Input label="Price" name="price" type="number" value={formData.price} onChange={handleChange} />
                        <label className="flex gap-2">
                            <input type="checkbox" name="fractionable" checked={formData.fractionable} onChange={handleChange} />
                            Fractionable
                        </label>
                        <div className="flex gap-2">
                            <Button type="submit">Save Changes</Button>
                            <Button type="button" onClick={() => navigate('/products')}>Back</Button>
                        </div>
                    </form>
                </section>

                <section className="border-t pt-6">
                    <h2 className="text-xl mb-3">Raw Material Associations</h2>
                    <div className="space-y-4">
                        {product?.rawMaterials?.map((rm) => (
                            <Card key={rm.id}>
                                <div className="flex justify-between items-center">
                                    <div>
                                        <strong>{rm.rawMaterialName}</strong>: {rm.quantityNeeded}
                                    </div>
                                    <Button onClick={() => handleRemoveAssoc(rm.id!)}>Remove</Button>
                                </div>
                            </Card>
                        ))}
                    </div>

                    <div className="mt-6 p-4 border bg-gray-50 max-w-md">
                        <h3 className="font-bold mb-3">Add Raw Material</h3>
                        <div className="space-y-3">
                            <div>
                                <label className="block text-sm">Select Raw Material</label>
                                <select
                                    className="border p-1 w-full"
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
                                value={newAssoc.quantity}
                                onChange={(e) => setNewAssoc({ ...newAssoc, quantity: Number(e.target.value) })}
                            />
                            <Button onClick={handleAddAssoc}>Add Association</Button>
                        </div>
                    </div>
                </section>
            </div>
        </MainLayout>
    );
};
