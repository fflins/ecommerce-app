import { useEffect, useState } from 'react';
import api from '../services/api';
import ProductCard from '../components/ProductCard';
import React from 'react';

const ProductList = () => {
  const [products, setProducts] = useState([]);

  useEffect(() => {
    api.get('/products')
      .then(response => {
        console.log('Produtos recebidos:', response.data); // Verifique a estrutura dos dados
        setProducts(response.data);
      })
      .catch(error => {
        console.error('Erro detalhado:', {
          status: error.response?.status,
          data: error.response?.data,
          headers: error.response?.headers
        });
      });
  }, []);

  return (
    <div className="product-grid">
      {products.map(product => (
        <ProductCard key={product.productId} product={product} /> /* Corrigido para 'productId' */
      ))}
    </div>
  );
};

export default ProductList;
