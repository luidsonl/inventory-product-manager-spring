import React from 'react';
import { useGetProductsQuery, useDeleteProductMutation } from '../../services/api.service';
import { ProductCard } from '../molecules/ProductCard';
import { useNavigate } from 'react-router-dom';

export const ProductList: React.FC = () => {
    const { data, isLoading, error } = useGetProductsQuery();
    const [deleteProduct] = useDeleteProductMutation();
    const navigate = useNavigate();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading products</div>;

    const handleDelete = async (id: number) => {
        await deleteProduct(id);
    };

    return (
        <div className="grid gap-3">
            {data?.map((product) => (
                <ProductCard
                    key={product.id}
                    product={product}
                    onEdit={(id) => navigate(`/products/${id}/edit`)}
                    onDelete={handleDelete}
                />
            ))}
        </div>
    );
};
