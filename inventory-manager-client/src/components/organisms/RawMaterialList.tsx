import React from 'react';
import { Card } from '../atoms/Card';
import { useGetRawMaterialsQuery } from '../../services/api.service';

interface Props {
    onEdit?: (id: number) => void;
    onDelete?: (id: number) => void;
}

export const RawMaterialList: React.FC<Props> = ({ onEdit, onDelete }) => {
    const { data, error, isLoading } = useGetRawMaterialsQuery();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading raw materials</div>;

    return (
        <div className="space-y-3">
            {[...(data || [])].sort((a, b) => (b.id || 0) - (a.id || 0)).map((r) => (
                <Card key={r.id}>
                    <div className="flex justify-between items-start">
                        <div>
                            <div><strong>{r.code}</strong> - {r.name}</div>
                            {r.description && <div className="text-sm italic text-slate-500">{r.description}</div>}
                            <div className="text-sm text-slate-600">Unit: {r.unit}</div>
                            <div className="text-sm text-slate-600">Fractionable: {r.fractionable ? 'Yes' : 'No'}</div>
                        </div>
                        <div className="flex gap-2">
                            {onEdit && (
                                <button
                                    onClick={() => r.id && onEdit(r.id)}
                                    className="px-3 py-1 text-sm font-medium text-blue-600 bg-blue-50 hover:bg-blue-100 rounded transition-colors"
                                >
                                    Edit
                                </button>
                            )}
                            {onDelete && (
                                <button
                                    onClick={() => r.id && onDelete(r.id)}
                                    className="px-3 py-1 text-sm font-medium text-red-600 bg-red-50 hover:bg-red-100 rounded transition-colors"
                                >
                                    Delete
                                </button>
                            )}
                        </div>
                    </div>
                </Card>
            ))}
        </div>
    );
};
