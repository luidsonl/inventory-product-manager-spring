import { useNavigate } from 'react-router-dom';
import { useGetProductTransactionsQuery } from '../../services/api.service';
import { ProductTransactionCard } from '../molecules/ProductTransactionCard';

export const ProductTransactionList: React.FC = () => {
    const navigate = useNavigate();
    const { data, isLoading, error } = useGetProductTransactionsQuery();

    if (isLoading) return <div>Loading...</div>;
    if (error) return <div>Error loading transactions</div>;

    return (
        <div className="grid gap-3">
            {data?.map((tx) => (
                <ProductTransactionCard
                    key={tx.id}
                    transaction={tx}
                    onClick={() => navigate(`/transactions/products/${tx.id}`)}
                />
            ))}
        </div>
    );
};
