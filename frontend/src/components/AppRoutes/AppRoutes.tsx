import Login from "../Login/Login.tsx";
import { Route, Routes } from "react-router";
import Dashboard from "../Dashboard/Dashboard.tsx";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/login" element={<Login />} />
    </Routes>
  );
};

export default AppRoutes;
