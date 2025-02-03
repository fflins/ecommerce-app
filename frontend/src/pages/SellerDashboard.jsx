import { useEffect, useState } from 'react';
import api from '../services/api';
import { Link } from 'react-router-dom';
import React from 'react';

const SellerDashboard = () => {
  const [products, setProducts] = useState([]);
  const [inventory, setInventory] = useState([]);
  const [inputValues, setInputValues] = useState({});

  useEffect(() => {
    api.get('/products')
      .then(res => {
        console.log("Produtos carregados:", res.data);
        setProducts(res.data);
      });
    api.get('/inventory')
      .then(res => {
        console.log("Inventário carregado:", res.data);
        setInventory(res.data);
      });
  }, []);

  const removeProduct = (productId) => {
    api.delete(`/products/${productId}`)
      .then(() => {
        setProducts(products.filter(product => product.productId !== productId));
        setInventory(inventory.filter(item => item.productId !== productId));
      })
      .catch(err => console.error("Erro ao remover produto:", err));
  };

  const handleInputChange = (productId, value) => {
    setInputValues(prev => ({
      ...prev,
      [productId]: parseInt(value) || 0
    }));
  };

  const updateStock = (productId, quantity) => {
    if (!productId || quantity === undefined) {
      console.error("Erro: productId ou quantity inválidos!");
      return;
    }
  
    api.put(`/inventory/products/${productId}`, { quantity })
      .then(res => {
        console.log(`Estoque atualizado para ${res.data.stock} unidades`);
        setInventory(inventory.map(item => 
          item.productId === productId ? { ...item, stock: res.data.stock } : item
        ));
      })
      .catch(err => console.error("Erro ao atualizar estoque:", err));
  };

  return (
    <div className="dashboard">
      <h2>Gerenciamento de Produtos</h2>
      <Link to="/add-product" className="btn">Adicionar Novo Produto</Link>

      <div className="inventory-list">
        <h3>Estoque</h3>
        {inventory.map(item => {
          // Encontre o produto correspondente ao item do inventário
          const product = products.find(p => p.productId === item.productId);

          return (
            <div key={item.productId} className="inventory-item">
              <span>{product ? product.name : 'Produto não encontrado'}</span>
              <span>Estoque: {item.stock}</span>
              <input
                type="number"
                defaultValue="0"
                onChange={(e) => handleInputChange(item.productId, e.target.value)}
              />
              <button onClick={() => updateStock(item.productId, inputValues[item.productId] || 0)}>
                Atualizar
              </button>
            </div>
          );
        })}
      </div>

      <div className="product-list">
        <h3>Produtos</h3>
        {products.map(product => (
          <div key={product.productId} className="product-item">
            <h4>{product.name}</h4>
            <Link to={`/products/edit/${product.productId}`} className="btn">Editar</Link>
            <button className="btn btn-danger" onClick={() => removeProduct(product.productId)}>
              Remover
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SellerDashboard;
