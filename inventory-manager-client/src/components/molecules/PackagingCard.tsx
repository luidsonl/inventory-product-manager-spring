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
        <div className="space-y-2">
            <div className="font-bold">{packaging.name}</div>
            <div className="text-sm">Quantity Inside: {packaging.quantityInside}</div>
            <div className="text-sm">Current Stock: {packaging.currentStock}</div>
            <div className="flex gap-2">
                {onEdit && <Button onClick={() => onEdit(packaging.id!)}>Edit</Button>}
                {onDelete && <Button onClick={() => onDelete(packaging.id!)}>Delete</Button>}
            </div>
        </div>
    </Card>
);
