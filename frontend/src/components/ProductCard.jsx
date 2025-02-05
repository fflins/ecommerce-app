import React, { useState } from "react";
import { Link } from "react-router-dom";

const url = "http://localhost:8080"; 
const ProductCard = ({ product }) => {

  return (
    <div className="product-card">
      <img src={`${url}${product.imageURL}`} alt={product.name} />
      <h3>{product.name}</h3>
      <p className="price">R$ {product.price}</p>
  
        <Link to={`/products/${product.productId}`} className="details-link">
          Ver Detalhes
        </Link>
      </div>
  );
};

export default ProductCard;
