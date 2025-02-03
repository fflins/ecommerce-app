import { useState } from 'react';
import api from '../services/api';

const AddProduct = () => {
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    category: '',
    price: '',
  });

  const [imageFile, setImageFile] = useState(null); // Estado para armazenar o arquivo de imagem
  const [previewImage, setPreviewImage] = useState(''); // Estado para pré-visualização da imagem

  // Função para lidar com a mudança da imagem
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file);
    
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => setPreviewImage(reader.result);
      reader.readAsDataURL(file);
    }
  };

  // Função para lidar com o envio do formulário
  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();
    data.append('name', formData.name);
    data.append('description', formData.description);
    data.append('category', formData.category);
    data.append('price', formData.price);

    if (imageFile) {
      data.append('file', imageFile); // Adiciona o arquivo de imagem ao FormData
    }

    try {
      const response = await api.post('/products/add', data, { // Certifique-se de que a rota esteja correta
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      alert('Produto criado com sucesso!');
      console.log('Produto criado:', response.data);
      
      // Limpa o formulário após o sucesso
      setFormData({ name: '', description: '', category: '', price: '' });
      setImageFile(null);
      setPreviewImage('');
    } catch (error) {
      console.error('Erro ao criar produto:', error.response || error.message);
      alert('Erro ao criar produto!');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        placeholder="Nome"
        value={formData.name}
        onChange={(e) => setFormData({ ...formData, name: e.target.value })}
        required
      />
      <textarea
        placeholder="Descrição"
        value={formData.description}
        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
        required
      />
      <input
        type="text"
        placeholder="Categoria"
        value={formData.category}
        onChange={(e) => setFormData({ ...formData, category: e.target.value })}
        required
      />
      <input
        type="number"
        placeholder="Preço"
        value={formData.price}
        onChange={(e) => setFormData({ ...formData, price: e.target.value })}
        step="0.01"
        required
      />
      <input
        type="file"
        accept="image/*"
        onChange={handleImageChange}
      />
      {previewImage && <img src={previewImage} alt="Pré-visualização" style={{ width: '100px', marginTop: '10px' }} />}
      <button type="submit">Adicionar Produto</button>
    </form>
  );
};

export default AddProduct;
