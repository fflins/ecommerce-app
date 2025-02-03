import React, { useState, useEffect } from "react";
import api from "../services/api.js";
import { useParams } from "react-router-dom";

const EditProduct = () => {
  const { productId } = useParams();
  console.log("productId:", productId);
  
  const [product, setProduct] = useState({
    name: "",
    description: "",
    category: "",
    price: 0,
    imageUrl: "",
  });
  const [stock, setStock] = useState(0);
  const [imageFile, setImageFile] = useState(null);
  const [previewImage, setPreviewImage] = useState("");

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const productResponse = await api.get(`/products/${productId}`);
        setProduct(productResponse.data);
        setPreviewImage(productResponse.data.imageUrl);
      } catch (error) {
        console.error("Erro ao buscar o produto:", error);
      }
    };

    const fetchInventory = async () => {
      try {
        const inventoryResponse = await api.get(`/inventory/${productId}`);
        setStock(inventoryResponse.data.stock);
      } catch (error) {
        console.error("Erro ao buscar o estoque:", error);
      }
    };

    fetchProduct();
    fetchInventory();
  }, [productId]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("name", product.name);
    formData.append("description", product.description);
    formData.append("category", product.category);
    formData.append("price", product.price);
    if (imageFile) {
      formData.append("file", imageFile);
    }

    try {
      const response = await api.put(`/products/${productId}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Produto atualizado com sucesso!");
      setProduct(response.data);
      setPreviewImage(response.data.imageURL);
    } catch (error) {
      console.error("Erro ao atualizar o produto:", error);
      alert("Erro ao atualizar o produto.");
    }
  };

  const handleStockUpdate = async () => {
    const stockValue = Number(stock);  // Converte explicitamente para número
    if (isNaN(stockValue)) {
      alert("Por favor, insira um valor numérico válido para o estoque.");
      return;
    }
  
    try {
      const response = await api.put(`/inventory/products/${productId}`, {
        quantity: stockValue,  
      });
      alert("Estoque atualizado com sucesso!");
      setStock(response.data.stock);
    } catch (error) {
      console.error("Erro ao atualizar o estoque:", error);
      alert("Erro ao atualizar o estoque.");
    }
  };
  

  return (
    <div className="edit-product">
      <h2>Editar Produto</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="name">Nome:</label>
          <input
            type="text"
            id="name"
            value={product.name || ""}
            onChange={(e) => setProduct({ ...product, name: e.target.value })}
          />
        </div>
  
        <div>
          <label htmlFor="description">Descrição:</label>
          <textarea
            id="description"
            value={product.description || ""}
            onChange={(e) => setProduct({ ...product, description: e.target.value })}
          />
        </div>
  
        <div>
          <label htmlFor="category">Categoria:</label>
          <input
            type="text"
            id="category"
            value={product.category || ""}
            onChange={(e) => setProduct({ ...product, category: e.target.value })}
          />
        </div>
  
        <div>
          <label htmlFor="price">Preço:</label>
          <input
            type="number"
            id="price"
            value={product.price || 0}
            onChange={(e) => setProduct({ ...product, price: e.target.value })}
          />
        </div>
  
        <div>
          <label htmlFor="image">Imagem:</label>
          <input
            type="file"
            id="image"
            onChange={(e) => setImageFile(e.target.files[0])}
          />
          {previewImage && <img src={previewImage} alt="Preview" width="100" />}
        </div>

        <button type="submit">Atualizar Produto</button>
      </form>
  
      <div>
        <h3>Estoque</h3>
        <label htmlFor="stock">Quantidade em estoque:</label>
        <input
          type="number"
          id="stock"
          value={stock}
          onChange={(e) => setStock(e.target.value)}
        />
        <button onClick={handleStockUpdate}>Atualizar Estoque</button>
      </div>
    </div>
  );
};  

export default EditProduct;
