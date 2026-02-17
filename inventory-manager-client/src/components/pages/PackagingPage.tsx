import React from 'react';
import { PackagingList } from '../organisms/PackagingList';
import { MainLayout } from '../templates/MainLayout';
import { Button } from '../atoms/Button';
import { useNavigate } from 'react-router-dom';

export const PackagingPage: React.FC = () => {
    const navigate = useNavigate();

    return (
        <MainLayout title="Packaging">
            <div className="flex justify-between items-center mb-4">
                <h1 className="text-2xl">Packaging</h1>
                <Button onClick={() => navigate('/packaging/create')}>Create New</Button>
            </div>
            <PackagingList />
        </MainLayout>
    );
};
