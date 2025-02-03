import { useEffect, useState } from 'react';
import api from '../services/api';
import Loader from '../components/Loader';
import React from 'react';
import { Link } from 'react-router-dom';

const Profile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    api.get('/users/me')
      .then(res => {
        setUser(res.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Erro ao carregar perfil:', error);
        setError('Erro ao carregar o perfil. Tente novamente mais tarde.');
        setLoading(false);
        if (error.response?.status === 403) {
          console.log('Por favor, faça login para ver seu perfil');
          localStorage.setItem('redirectAfterLogin', window.location.pathname); 
        }
      });
  }, []);
  

  if (loading) return <Loader />;

  if (error) {
    return (
      <div className="profile">
        <h2>Erro</h2>
        <p>{error}</p>
        <Link to="/" className="btn">Voltar para a página inicial</Link>
      </div>
    );
  }

  return (
    <div className="profile">
      <h2>Meu Perfil</h2>
      {user ? (
        <div className="profile-info">
          <p>Nome: {user.login}</p>
          <p>Email: {user.email}</p>
          <p>Tipo de Conta: {user.role === 'SELLER' ? 'Vendedor' : 'Cliente'}</p>
          <div className="profile-links">
            <Link to="/orders" className="btn">Meus Pedidos</Link>
            {user.role === 'SELLER' && (
              <Link to="/dashboard" className="btn">Painel do Vendedor</Link>
            )}
          </div>
        </div>
      ) : (
        <p>Nenhum dado de usuário encontrado.</p>
      )}
    </div>
  );
};

export default Profile;
