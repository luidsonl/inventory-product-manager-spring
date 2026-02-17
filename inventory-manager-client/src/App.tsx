import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import { RawMaterialsListPage } from './components/pages/RawMaterialsListPage';
import { HomePage } from './components/pages/HomePage';



function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/raw-materials" element={<RawMaterialsListPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
