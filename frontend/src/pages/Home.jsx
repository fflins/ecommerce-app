import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import ProductCard from '../components/ProductCard';
import Loader from '../components/Loader';
import React from 'react';


const Home = () => {
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/products?featured=true')
      .then(res => {
        setFeaturedProducts(res.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching products:', error);
        setLoading(false);
      });
  }, []);

  if (loading) return <Loader />;

  return (
    <div className="home-page">
      <section className="hero-banner">
        <h1>Bem-vindo à Nossa Loja</h1>
        <p>Descubra os melhores produtos pelos menores preços</p>
        <Link to="/products" className="btn">Ver Todos os Produtos</Link>
      </section>

      <section className="featured-products">
        <h2>Produtos em Destaque</h2>
        <div className="product-grid">
          {featuredProducts.map(product => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      </section>

      <section className="categories">
        <h2>Categorias Populares</h2>
        <div className="category-grid">
          {['Eletrônicos', 'Livros', 'Moda', 'Casa'].map(category => (
            <Link 
              key={category}
              to={`/products?category=${category}`}
              className="category-card"
            >
              {category}
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
};

export default Home;