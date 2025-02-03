import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import React from 'react';


const Register = () => {
  const [formData, setFormData] = useState({
    login: '',
    password: '',
    email: '',
    role: 'CLIENT'
  });
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/auth/register', formData);
      navigate('/login');
    } catch (error) {
      alert('Erro no registro!');
    }
  };

  return (
    <div className="auth-form">
      <h2>Criar Conta</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Nome de usuário"
          onChange={(e) => setFormData({...formData, login: e.target.value})}
        />
        <input
          type="password"
          placeholder="Senha"
          onChange={(e) => setFormData({...formData, password: e.target.value})}
        />
        <input
          type="email"
          placeholder="Email"
          onChange={(e) => setFormData({...formData, email: e.target.value})}
        />
        <select onChange={(e) => setFormData({...formData, role: e.target.value})}>
          <option value="CLIENT">Cliente</option>
          <option value="SELLER">Vendedor</option>
        </select>
        <button type="submit">Registrar</button>
      </form>
    </div>
  );
};

export default Register;