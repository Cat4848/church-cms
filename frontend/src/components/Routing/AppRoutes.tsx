import Login from "../Login/Login.tsx";
import { Route, Routes } from "react-router";
import Dashboard from "../Dashboard/Dashboard.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import Authors from "../Authors/Authors.tsx";
import HymnBooks from "../HymnBooks/HymnBooks.tsx";
import Topics from "../Topics/Topics.tsx";
import Labels from "../Labels/Labels.tsx";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      // TODO all protected components could be under this layout
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
