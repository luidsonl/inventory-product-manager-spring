import React from 'react';
import { RawMaterialList } from '../organisms/RawMaterialList';
import { Header } from '../molecules/Header';

export const RawMaterialsListPage: React.FC = () => {
    return (
        <div className="">
            <Header />
            <main className="">
                <h1 className="">Raw Materials</h1>
                <RawMaterialList />
            </main>
        </div>
    );
};
