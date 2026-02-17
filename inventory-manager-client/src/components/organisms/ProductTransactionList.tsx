import React from 'react';
import { useGetProductTransactionsQuery } from '../../services/api.service';
import { ProductTransactionCard } from '../molecules/ProductTransactionCard';

export const ProductTransactionList: React.FC = () => {
    const { data, isLoading, error } = useGetProductTransactionsQuery();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading transactions</div>;

    return (
        <div className="grid gap-3">
            {data?.map((tx) => (
                <ProductTransactionCard key={tx.id} transaction={tx} />
            ))}
        </div>
    );
};
