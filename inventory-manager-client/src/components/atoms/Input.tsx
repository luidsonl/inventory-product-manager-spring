import React from 'react';

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
    label?: string;
}

export const Input: React.FC<Props> = ({ label, ...rest }) => {
    return (
        <label>
            {label && <span>{label}</span>}
            <input {...rest} className={`${rest.className ?? ''}`} />
        </label>
    );
};
