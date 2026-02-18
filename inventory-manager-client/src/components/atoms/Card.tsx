import React from 'react';

export const Card: React.FC<{ children: React.ReactNode; className?: string }> = ({ children, className }) => (
    <div className={`bg-white rounded-lg border border-slate-200 shadow-sm p-6 ${className ?? ''}`}>{children}</div>
);
