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
                    <div>{r.code} - {r.name}</div>
                    <div>Unit: {r.unit}</div>
                </Card>
            ))}
        </div>
    );
};
