import { useState, useContext } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import React from 'react';
import { AuthContext } from '../services/AuthContext'; // Importando o contexto

const Login = () => {
  const [formData, setFormData] = useState({
    login: '',
    password: ''
  });
  const navigate = useNavigate();
  const { login } = useContext(AuthContext); // Obtém a função login do contexto

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await api.post('/auth/login', formData);
      login(response.data.token); // Atualiza o contexto de autenticação
  
      // Verifica se há uma página para redirecionar após o login
      const redirectTo = localStorage.getItem('redirectAfterLogin') || '/';
      localStorage.removeItem('redirectAfterLogin'); // Limpa o valor após redirecionar
      navigate(redirectTo);
    } catch (error) {
      alert('Login falhou!');
    }
  };

  return (
    <div className="auth-form">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Usuário"
          onChange={(e) => setFormData({...formData, login: e.target.value})}
        />
        <input
          type="password"
          placeholder="Senha"
          onChange={(e) => setFormData({...formData, password: e.target.value})}
        />
        <button type="submit">Entrar</button>
      </form>
    </div>
  );
};

export default Login;
