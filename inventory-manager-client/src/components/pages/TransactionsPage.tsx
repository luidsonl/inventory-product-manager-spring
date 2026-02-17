import React from 'react';
import { ProductTransactionList } from '../organisms/ProductTransactionList';
import { PackagingTransactionList } from '../organisms/PackagingTransactionList';
import { MainLayout } from '../templates/MainLayout';

export const TransactionsPage: React.FC = () => {
    return (
        <MainLayout title="Transactions">
            <h1 className="text-2xl mb-4">Transactions</h1>
            <div className="space-y-6">
                <div>
                    <h2 className="text-xl mb-3">Product Transactions</h2>
                    <ProductTransactionList />
                </div>
                <div>
                    <h2 className="text-xl mb-3">Packaging Transactions</h2>
                    <PackagingTransactionList />
                </div>
            </div>
        </MainLayout>
    );
};
