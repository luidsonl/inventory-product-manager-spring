import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import { RawMaterialsPage } from './components/pages/RawMaterialsPage';
import { RawMaterialEditPage } from './components/pages/RawMaterialEditPage';
import { ProductsPage } from './components/pages/ProductsPage';
import { ProductEditPage } from './components/pages/ProductEditPage';
import { ProductCreatePage } from './components/pages/ProductCreatePage';
import { PackagingPage } from './components/pages/PackagingPage';
import { PackagingEditPage } from './components/pages/PackagingEditPage';
import { PackagingCreatePage } from './components/pages/PackagingCreatePage';
import { ProductTransactionsPage } from './components/pages/ProductTransactionsPage';
import { ProductTransactionCreatePage } from './components/pages/ProductTransactionCreatePage';
import { PackagingTransactionsPage } from './components/pages/PackagingTransactionsPage';
import { PackagingTransactionCreatePage } from './components/pages/PackagingTransactionCreatePage';
import { ProductionPage } from './components/pages/ProductionPage';
import { HomePage } from './components/pages/HomePage';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/raw-materials" element={<RawMaterialsPage />} />
        <Route path="/raw-materials/:id/edit" element={<RawMaterialEditPage />} />
        <Route path="/products" element={<ProductsPage />} />
        <Route path="/products/create" element={<ProductCreatePage />} />
        <Route path="/products/:id/edit" element={<ProductEditPage />} />
        <Route path="/packaging" element={<PackagingPage />} />
        <Route path="/packaging/create" element={<PackagingCreatePage />} />
        <Route path="/packaging/:id/edit" element={<PackagingEditPage />} />
        <Route path="/transactions/products" element={<ProductTransactionsPage />} />
        <Route path="/transactions/products/create" element={<ProductTransactionCreatePage />} />
        <Route path="/transactions/packaging" element={<PackagingTransactionsPage />} />
        <Route path="/transactions/packaging/create" element={<PackagingTransactionCreatePage />} />
        <Route path="/production" element={<ProductionPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
