import React from 'react';
import { ProductList } from '../organisms/ProductList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { useNavigate } from 'react-router-dom';

export const ProductsPage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <MainLayout title="Products">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl">Products</h1>
                <Button onClick={() => navigate('/products/create')}>Create New</Button>
            </div>
            <ProductList />
        </MainLayout>
    );
};
