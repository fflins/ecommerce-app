import { Link } from "react-router-dom";
import React, { useContext } from "react";
import { AuthContext } from "../services/AuthContext";

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);

  return (
    <nav className="navbar">
      <Link to="/" className="logo">ECOMMERCE</Link>
      <div className="nav-links">
        <Link to="/products">Produtos</Link>

        {user ? (
          <>
            <Link to="/cart">Carrinho</Link>
            <Link to="/orders">Pedidos</Link>
            <Link to="/profile">Perfil</Link>
            <button onClick={logout} className="logout-btn">Sair</button>
          </>
        ) : (
          <>
            <Link to="/register">Registrar</Link>
            <Link to="/login">Entrar</Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
