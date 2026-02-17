import React from 'react';
import { Card } from '../atoms/Card';
import { useGetRawMaterialsQuery } from '../../services/api.service';

export const RawMaterialList: React.FC = () => {
    const { data, error, isLoading } = useGetRawMaterialsQuery();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading raw materials</div>;

    return (
        <div>
            {data?.map((r) => (
                <Card key={r.id}>
                    <div><strong>{r.code}</strong> - {r.name}</div>
                    {r.description && <div className="text-sm italic">{r.description}</div>}
                    <div>Unit: {r.unit}</div>
                    <div>Fractionable: {r.fractionable ? 'Yes' : 'No'}</div>
                </Card>
            ))}
        </div>
    );
};
