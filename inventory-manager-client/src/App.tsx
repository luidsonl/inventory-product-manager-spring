import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import { RawMaterialsPage } from './components/pages/RawMaterialsPage';
import { RawMaterialEditPage } from './components/pages/RawMaterialEditPage';
import { ProductsPage } from './components/pages/ProductsPage';
import { ProductEditPage } from './components/pages/ProductEditPage';
import { PackagingPage } from './components/pages/PackagingPage';
import { PackagingEditPage } from './components/pages/PackagingEditPage';
import { TransactionsPage } from './components/pages/TransactionsPage';
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
        <Route path="/products/:id/edit" element={<ProductEditPage />} />
        <Route path="/packaging" element={<PackagingPage />} />
        <Route path="/packaging/:id/edit" element={<PackagingEditPage />} />
        <Route path="/transactions" element={<TransactionsPage />} />
        <Route path="/production" element={<ProductionPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
