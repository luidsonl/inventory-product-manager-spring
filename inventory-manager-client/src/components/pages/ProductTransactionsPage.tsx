import React from 'react';
import { ProductTransactionList } from '../organisms/ProductTransactionList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { useNavigate } from 'react-router-dom';

export const ProductTransactionsPage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl">Product Transactions</h1>
                <Button onClick={() => navigate('/transactions/products/create')}>New Transaction</Button>
            </div>
            <ProductTransactionList />
        </MainLayout>
    );
};
