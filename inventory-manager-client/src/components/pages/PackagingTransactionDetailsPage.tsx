import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useGetPackagingTransactionsQuery } from '../../services/api.service';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { Card } from '../atoms/Card';

export const PackagingTransactionDetailsPage: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();
    const { transaction, isLoading, error } = useGetPackagingTransactionsQuery(undefined, {
        selectFromResult: ({ data, isLoading, isError }) => ({
            transaction: data?.find((t) => t.id === Number(id)),
            isLoading,
            error: isError,
        }),
    });

    if (isLoading) return <MainLayout><div>Loading...</div></MainLayout>;
    if (error || !transaction) return <MainLayout><div>Error loading transaction</div></MainLayout>;

    return (
        <MainLayout>
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl">Transaction #{transaction.id}</h1>
                <Button onClick={() => navigate('/transactions/packaging')}>Back to List</Button>
            </div>

            <Card>
                <div className="space-y-4">
                    <div>
                        <span className="font-bold block text-sm text-gray-600">Packaging</span>
                        <div className="text-lg">{transaction.packagingName || `ID: ${transaction.packagingId}`}</div>
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <span className="font-bold block text-sm text-gray-600">Type</span>
                            <div>{transaction.type}</div>
                        </div>
                        <div>
                            <span className="font-bold block text-sm text-gray-600">Quantity</span>
                            <div className={transaction.type === 'INVENTORY_OUT' ? 'text-red-600' : 'text-green-600'}>
                                {transaction.type === 'INVENTORY_OUT' ? '-' : '+'}{transaction.quantity}
                            </div>
                        </div>
                    </div>

                    <div>
                        <span className="font-bold block text-sm text-gray-600">Date</span>
                        <div>{transaction.transactionDate ? new Date(transaction.transactionDate).toLocaleString() : 'N/A'}</div>
                    </div>

                    {transaction.note && (
                        <div>
                            <span className="font-bold block text-sm text-gray-600">Note</span>
                            <div className="bg-gray-50 p-2 rounded">{transaction.note}</div>
                        </div>
                    )}
                </div>
            </Card>
        </MainLayout>
    );
};
