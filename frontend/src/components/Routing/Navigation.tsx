import { NavLink } from "react-router";
import { useLocation } from "react-router";

const Navigation = () => {
  const location = useLocation();

  return !location.pathname.includes("login") ? (
    <nav>
      <NavLink to="/" className={({ isActive }) => (isActive ? "nav-active" : "")}>
        Dashboard
      </NavLink>

      <NavLink to="/authors" className={({ isActive }) => (isActive ? "nav-active" : "")}>
        Authors
      </NavLink>

      <NavLink to="/hymn-books" className={({ isActive }) => (isActive ? "nav-active" : "")}>
        Hymn Books
      </NavLink>

      <NavLink to="/topics" className={({ isActive }) => (isActive ? "nav-active" : "")}>
        Topics
      </NavLink>

      <NavLink to="/labels" className={({ isActive }) => (isActive ? "nav-active" : "")}>
        Labels
      </NavLink>
    </nav>
  ) : (
    <nav></nav>
  );
};

export default Navigation;
