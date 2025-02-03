import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import Loader from '../components/Loader';
import React from 'react';

const Cart = () => {
  const [cart, setCart] = useState({ items: [] }); 
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    api.get('/cart')
      .then(res => {
        setCart(res.data);
        setLoading(false);
      })
      .catch(error => {
        setError('Erro ao carregar o carrinho. Tente novamente mais tarde.');
        setLoading(false);
      });
  }, []);

  const removeItem = (itemId) => {
    api.delete(`/cart/items/${itemId}`)
      .then(() => {
        setCart(prev => ({
          ...prev,
          items: prev.items.filter(item => item.id !== itemId)
        }));
      })
      .catch(error => {
        setError('Erro ao remover item do carrinho.');
      });
  };

  const clearCart = () => {
    api.post('/cart/clear')
      .then(() => {
        setCart({ items: [] });
      })
      .catch(error => {
        setError('Erro ao limpar o carrinho.');
      });
  };

  if (loading) return <Loader />;

  if (error) {
    return (
      <div className="cart-page">
        <h2>Erro</h2>
        <p>{error}</p>
        <Link to="/" className="btn">Voltar para a página inicial</Link>
      </div>
    );
  }

  const handleCheckout = () => {
    api.post('/orders')
      .then(() => {
        alert('Pedido criado com sucesso!');
        window.location.href = "/orders";
      })
      .catch(error => {
        alert('Erro ao efetuar pagamento. Tente novamente.');
      });
  };

  return (
    <div className="cart-page">
      <h2>Seu Carrinho de Compras</h2>
      {cart.items.length === 0 ? (
        <div className="empty-cart">
          <p>Seu carrinho está vazio</p>
          <Link to="/products" className="btn">Continuar Comprando</Link>
        </div>
      ) : (
        <div className="cart-content">
          <div className="cart-items">
            {cart.items.map(item => (
              <div key={item.id} className="cart-item">
                <img src={item.productImageUrl} alt={item.productName} />
                <div className="item-info">
                  <Link to={`/products/${item.productId}`}>{item.productName}</Link>
                  <p>Quantidade: {item.quantity}</p>
                  <p>Preço: R$ {item.productPrice}</p>
                </div>
                <button onClick={() => removeItem(item.id)} className="remove-btn">Remover</button>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <h3>Resumo do Pedido</h3>
            <div className="summary-row">
              <span>Itens ({cart.items.length})</span>
              <span>R$ {cart.items.reduce((sum, item) => sum + item.productPrice * item.quantity, 0).toFixed(2)}</span>
            </div>
            <button onClick={handleCheckout} className="btn">Efetuar Pagamento</button>
            <button onClick={clearCart} className="clear-btn">Limpar Carrinho</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;
