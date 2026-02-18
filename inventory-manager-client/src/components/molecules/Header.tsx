import React from 'react';
import { Link } from 'react-router-dom';

export const Header: React.FC<{ title?: string }> = ({ title }) => (
    <header className="w-full bg-white border-b border-slate-200 sticky top-0 z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
            <div className="font-bold text-lg text-slate-900">{title ?? 'Inventory Manager'}</div>
            <nav className="flex gap-6 text-sm font-medium">
                <Link to="/" className="text-slate-600 hover:text-slate-900 transition-colors">Home</Link>
                <Link to="/raw-materials" className="text-slate-600 hover:text-slate-900 transition-colors">Raw Materials</Link>
                <Link to="/products" className="text-slate-600 hover:text-slate-900 transition-colors">Products</Link>
                <Link to="/packaging" className="text-slate-600 hover:text-slate-900 transition-colors">Packaging</Link>
                <Link to="/transactions/products" className="text-slate-600 hover:text-slate-900 transition-colors">Product Transactions</Link>
                <Link to="/transactions/packaging" className="text-slate-600 hover:text-slate-900 transition-colors">Packaging Transactions</Link>
                <Link to="/production" className="text-slate-600 hover:text-slate-900 transition-colors">Production</Link>
            </nav>
        </div>
    </header>
);
