import { useEffect, useState } from 'react';
import api from '../services/api';
import React from 'react';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    api.get('/orders')
      .then(res => {
        console.log('Dados da API:', res.data); // Veja a estrutura real
        setOrders(Array.isArray(res.data) ? res.data : []);
        setLoading(false);
      })
      .catch(error => {
        console.error('Erro ao carregar pedidos:', error);
        setError('Erro ao carregar seus pedidos. Por favor, faça login.');
        setLoading(false);
      });
  }, []);
  
  

  if (loading) return <div>Carregando...</div>;

  if (error) {
    return (
      <div className="error">
        <h2>Erro</h2>
        <p>{error}</p>
        <a href="/login" className="btn">Ir para o Login</a>
      </div>
    );
  }

  return (
    <div className="orders-list">
      <h2>Meus Pedidos</h2>
      {orders.map(order => (
        <div key={order.id} className="order-item">
          <div className="order-header">
            <span>Pedido #${order.id}</span>
            <span>{new Date(order.date).toLocaleDateString()}</span>
          </div>
          <div className="order-products">
          {order.products.map(op => (
  <div key={op.productId} className="order-product">
    <span>{op.productName}</span>  {/* Agora, op.productName existe */}
    <span>{op.quantity}x R$ {op.price}</span>  {/* op.price e op.quantity estão presentes */}
  </div>
))}

          </div>
          <div className="order-total">
            Total: R$ {order.products.reduce((sum, op) => sum + (op.price * op.quantity), 0)}
          </div>
        </div>
      ))}
    </div>
  );
};

export default Orders;
