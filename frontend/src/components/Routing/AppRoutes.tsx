import Login from "../../pages/Login/Login.tsx";
import { Route, Routes } from "react-router";
import Dashboard from "../Dashboard/Dashboard.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import Authors from "../../pages/Authors/Authors.tsx";
import HymnBooks from "../../pages/HymnBooks/HymnBooks.tsx";
import Topics from "../Topics/Topics.tsx";
import Labels from "../Labels/Labels.tsx";
import LoginAuthCheck from "./LoginAuthCheck.tsx";

const AppRoutes = () => {
  return (
    <Routes>
      <Route element={<LoginAuthCheck />}>
        <Route path="/login" element={<Login />} />
      </Route>
      <Route element={<ProtectedRoute />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/authors" element={<Authors />} />
        <Route path="/hymn-books" element={<HymnBooks />} />
        <Route path="/topics" element={<Topics />} />
        <Route path="/labels" element={<Labels />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
