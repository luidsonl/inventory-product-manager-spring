import React from 'react';
import { useGetPackagingsQuery, useDeletePackagingMutation } from '../../services/api.service';
import { PackagingCard } from '../molecules/PackagingCard';
import { useNavigate } from 'react-router-dom';

export const PackagingList: React.FC = () => {
    const { data, isLoading, error } = useGetPackagingsQuery();
    const [deletePackaging] = useDeletePackagingMutation();
    const navigate = useNavigate();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading packaging</div>;

    const handleDelete = async (id: number) => {
        await deletePackaging(id);
    };

    return (
        <div className="grid gap-3">
            {data?.map((packaging) => (
                <PackagingCard
                    key={packaging.id}
                    packaging={packaging}
                    onEdit={(id) => navigate(`/packaging/${id}/edit`)}
                    onDelete={handleDelete}
                />
            ))}
        </div>
    );
};
