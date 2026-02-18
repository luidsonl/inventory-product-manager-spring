import React from 'react';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
    label?: string;
}

export const Input: React.FC<Props> = ({ label, ...rest }) => {
    return (
        <label className="flex flex-col gap-1.5">
            {label && <span className="text-sm font-medium text-slate-700">{label}</span>}
            <input
                {...rest}
                className={`px-3 py-2 rounded-md border border-slate-300 shadow-sm focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-colors outline-none ${rest.className ?? ''}`}
            />
        </label>
    );
};
