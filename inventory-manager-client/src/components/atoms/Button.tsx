import React from 'react';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: React.ReactNode;
}

export const Button: React.FC<Props> = ({ children, ...rest }) => {
    return (
        <button
            {...rest}
            className={`px-4 py-2 rounded-md font-medium text-sm transition-colors focus:outline-none focus:ring-2 focus:ring-slate-900 focus:ring-offset-1 bg-slate-900 text-white hover:bg-slate-800 disabled:opacity-50 disabled:cursor-not-allowed ${rest.className ?? ''}`}
        >
            {children}
        </button>
    );
};
