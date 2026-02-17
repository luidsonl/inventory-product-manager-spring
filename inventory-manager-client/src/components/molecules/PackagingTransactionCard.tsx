import React from 'react';
import { Card } from '../atoms/Card';
import type { RawMaterialPackagingTransactionDTO } from '../../services/api.types';

interface Props {
    transaction: RawMaterialPackagingTransactionDTO;
}

export const PackagingTransactionCard: React.FC<Props> = ({ transaction }) => (
    <Card>
        <div className="space-y-2">
            <div className="font-bold">{transaction.packagingName || `Packaging ${transaction.packagingId}`}</div>
            <div className="text-sm">Type: {transaction.type}</div>
            <div className="text-sm">Quantity: {transaction.quantity}</div>
            {transaction.transactionDate && (
                <div className="text-sm">Date: {new Date(transaction.transactionDate).toLocaleDateString()}</div>
            )}
            {transaction.note && <div className="text-sm">Note: {transaction.note}</div>}
        </div>
    </Card>
);
