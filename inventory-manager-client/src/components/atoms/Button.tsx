import React from 'react';

interface Props extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    children: React.ReactNode;
}

export const Button: React.FC<Props> = ({ children, ...rest }) => {
    return (
        <button {...rest} className={` ${rest.className ?? ''}`}>
            {children}
        </button>
    );
};
