import axios from 'axios';

// Cria uma instância do axios com uma URL base e cabeçalhos globais
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json', // Cabeçalho global
  },
});


api.interceptors.request.use(config => {
  const token = localStorage.getItem('token'); // Recupera o token do localStorage
  if (token) {
    config.headers.Authorization = `Bearer ${token}`; // Adiciona o token ao cabeçalho
  }
  return config; // Retorna a configuração atualizada
});

api.interceptors.response.use(
  response => response,
  error => {
      if (error.response?.status === 403) {
          // Redireciona para login se não estiver autenticado
          window.location.href = '/login';
      }
      return Promise.reject(error);
  }
);

export default api; // Exporta a instância configurada