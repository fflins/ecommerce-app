import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../services/api';
import React from 'react';

const ProductDetail = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [showQuantityInput, setShowQuantityInput] = useState(false);

  useEffect(() => {
    api.get(`/products/${id}`).then((res) => {
      setProduct(res.data);
    }).catch(error => {
      console.error("Erro ao buscar o produto:", error);
    });
  }, [id]);

  const handleAddToCart = async () => {
    const token = localStorage.getItem("token"); // Verifica se o usuário está autenticado
    if (!token) {
      alert("Você precisa estar logado para adicionar um produto ao carrinho.");
      return;
    }

    if (!showQuantityInput) {
      setShowQuantityInput(true);
      return;
    }

    try {
      await api.post("/cart/items", {
        productId: product.productId,
        quantity: quantity,
      }, {
        headers: { Authorization: `Bearer ${token}` } // Envia o token para autenticação
      });

      alert("Produto adicionado ao carrinho com sucesso!");
      setShowQuantityInput(false);
      setQuantity(1);
    } catch (error) {
      alert("Erro ao adicionar produto ao carrinho.");
      console.error(error);
    }
  };

  return (
    <div className="product-detail">
      {product && (
        <div className="product-detail-container">
          <div className="product-images">
            <img src={product.imageURL} alt={product.name} />
          </div>
          <div className="product-info">
            <h1>{product.name}</h1>
            <p className="price">R$ {product.price}</p>
            <p className="description">{product.description}</p>
            {product.category && <p><strong>Categoria:</strong> {product.category}</p>}

            {showQuantityInput && (
              <div>
                <label htmlFor="quantity">Quantidade:</label>
                <input
                  type="number"
                  id="quantity"
                  min="1"
                  value={quantity}
                  onChange={(e) => setQuantity(Number(e.target.value))}
                />
              </div>
            )}

            <button className="add-to-cart-btn" onClick={handleAddToCart}>
              {showQuantityInput ? "Confirmar" : "Adicionar ao Carrinho"}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductDetail;
