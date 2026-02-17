import React from 'react';
import { Card } from '../atoms/Card';
import { Button } from '../atoms/Button';
import type { ProductDTO } from '../../services/api.types';

interface Props {
    product: ProductDTO;
    onEdit?: (id: number) => void;
    onDelete?: (id: number) => void;
}

export const ProductCard: React.FC<Props> = ({ product, onEdit, onDelete }) => (
    <Card>
        <div className="space-y-2">
            <div className="font-bold">{product.code} - {product.name}</div>
            <div className="text-sm">Price: ${product.price}</div>
            {product.rawMaterials && product.rawMaterials.length > 0 && (
                <div className="text-sm">Raw Materials: {product.rawMaterials.length}</div>
            )}
            <div className="flex gap-2">
                {onEdit && <Button onClick={() => onEdit(product.id!)}>Edit</Button>}
                {onDelete && <Button onClick={() => onDelete(product.id!)}>Delete</Button>}
            </div>
        </div>
    </Card>
);
