import React from 'react';
import { Header } from '../molecules/Header';

export const MainLayout: React.FC<{ children: React.ReactNode; }> = ({ children }) => (
    <div >
        <Header />
        <main>{children}</main>
    </div>
);
