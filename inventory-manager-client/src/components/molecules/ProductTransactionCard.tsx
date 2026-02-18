import React from 'react';
import { Card } from '../atoms/Card';
import { TransactionType, type ProductTransactionDTO } from '../../services/api.types';

interface Props {
    transaction: ProductTransactionDTO;
    onClick?: () => void;
}

export const ProductTransactionCard: React.FC<Props> = ({ transaction, onClick }) => (
    <div onClick={onClick} className={onClick ? "cursor-pointer transition-all hover:-translate-y-1 duration-200" : ""}>
        <Card className="h-full hover:shadow-md transition-shadow">
            <div className="space-y-3">
                <div className="font-bold text-slate-900 border-b border-slate-100 pb-2 mb-2">
                    {transaction.productName || `Product ${transaction.productId}`}
                </div>
                <div className="space-y-1">
                    <div className="text-sm text-slate-600 flex justify-between">
                        <span>Type:</span>
                        <span className={`font-medium px-2 py-0.5 rounded text-xs ${transaction.type === TransactionType.INVENTORY_IN ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'
                            }`}>
                            {transaction.type}
                        </span>
                    </div>
                    <div className="text-sm text-slate-600 flex justify-between">
                        <span>Quantity:</span>
                        <span className="font-medium text-slate-900">{transaction.quantity}</span>
                    </div>
                    {transaction.transactionDate && (
                        <div className="text-sm text-slate-500 flex justify-between">
                            <span>Date:</span>
                            <span>{new Date(transaction.transactionDate).toLocaleDateString()}</span>
                        </div>
                    )}
                    {transaction.note && (
                        <div className="text-sm text-slate-500 italic mt-2 pt-2 border-t border-slate-50">
                            "{transaction.note}"
                        </div>
                    )}
                </div>
            </div>
        </Card>
    </div>
);
