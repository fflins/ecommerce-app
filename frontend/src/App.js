import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import * as Pages from './pages'; // Importação única
import React from 'react';
import { AuthProvider } from './services/AuthContext'; // Importando o contexto de autenticação
import './App.css';

function App() {
  return (
    <AuthProvider> {/* Envolvendo toda a aplicação com o contexto */}
      <Router>
        <Navbar />
        <div className="container">
          <Routes>
            <Route path="/" element={<Pages.Home />} />
            <Route path="/products" element={<Pages.ProductList />} />
            <Route path="/products/:id" element={<Pages.ProductDetail />} />
            <Route path="/cart" element={<Pages.Cart />} />
            <Route path="/login" element={<Pages.Login />} />
            <Route path="/register" element={<Pages.Register />} />
            <Route path="/profile" element={<Pages.Profile />} />
            <Route path="/orders" element={<Pages.Orders />} />
            <Route path="/dashboard" element={<Pages.SellerDashboard />} />
            <Route path="/add-product" element={<Pages.AddProduct />} />
            <Route path="/products/edit/:productId" element={<Pages.EditProduct />} />
          </Routes>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
