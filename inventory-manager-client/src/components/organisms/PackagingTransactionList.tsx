import React from 'react';
import { useGetPackagingTransactionsQuery } from '../../services/api.service';
import { PackagingTransactionCard } from '../molecules/PackagingTransactionCard';

export const PackagingTransactionList: React.FC = () => {
    const { data, isLoading, error } = useGetPackagingTransactionsQuery();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading transactions</div>;

    return (
        <div className="grid gap-3">
            {data?.map((tx) => (
                <PackagingTransactionCard key={tx.id} transaction={tx} />
            ))}
        </div>
    );
};
