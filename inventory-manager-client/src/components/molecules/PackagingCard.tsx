import React from 'react';
import { Card } from '../atoms/Card';
import { Button } from '../atoms/Button';
import type { RawMaterialPackagingDTO } from '../../services/api.types';

interface Props {
    packaging: RawMaterialPackagingDTO;
    onEdit?: (id: number) => void;
    onDelete?: (id: number) => void;
}

export const PackagingCard: React.FC<Props> = ({ packaging, onEdit, onDelete }) => (
    <Card>
        <div className="space-y-3">
            <div className="font-bold text-slate-900 border-b border-slate-100 pb-2 mb-2">{packaging.name}</div>
            <div className="space-y-1">
                <div className="text-sm text-slate-600 flex justify-between">
                    <span>Quantity Inside:</span>
                    <span className="font-medium text-slate-900">{packaging.quantityInside}</span>
                </div>
                <div className="text-sm text-slate-600 flex justify-between">
                    <span>Current Stock:</span>
                    <span className="font-medium text-slate-900">{packaging.currentStock}</span>
                </div>
            </div>
            <div className="flex gap-2 mt-4 pt-2 border-t border-slate-100">
                {onEdit && <Button onClick={() => onEdit(packaging.id!)} className="flex-1 bg-slate-100 text-slate-700 hover:bg-slate-200 focus:ring-slate-300">Edit</Button>}
                {onDelete && <Button onClick={() => onDelete(packaging.id!)} className="flex-1 bg-red-50 text-red-600 hover:bg-red-100 focus:ring-red-200">Delete</Button>}
            </div>
        </div>
    </Card>
);
