import React from 'react';
import { PackagingTransactionList } from '../organisms/PackagingTransactionList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { useNavigate } from 'react-router-dom';

export const PackagingTransactionsPage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl">Packaging Transactions</h1>
                <Button onClick={() => navigate('/transactions/packaging/create')}>New Transaction</Button>
            </div>
            <PackagingTransactionList />
        </MainLayout>
    );
};
