import React from 'react';
import { Link } from 'react-router-dom';

export const Header: React.FC<{ title?: string }> = ({ title }) => (
    <header className="w-full bg-gray-800 text-white p-4">
        <div className="max-w-7xl mx-auto flex items-center justify-between">
            <div className="font-bold text-lg">{title ?? 'Inventory Manager'}</div>
            <nav className="flex gap-4 text-sm">
                <Link to="/" className="text-white hover:underline">Home</Link>
                <Link to="/raw-materials" className="text-white hover:underline">Raw Materials</Link>
                <Link to="/products" className="text-white hover:underline">Products</Link>
                <Link to="/packaging" className="text-white hover:underline">Packaging</Link>
                <Link to="/transactions/products" className="text-white hover:underline">Product Transactions</Link>
                <Link to="/transactions/packaging" className="text-white hover:underline">Packaging Transactions</Link>
                <Link to="/production" className="text-white hover:underline">Production</Link>
            </nav>
        </div>
    </header>
);
