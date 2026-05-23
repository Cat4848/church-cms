import "./App.css";
import { BrowserRouter } from "react-router";
import AppRoutes from "./components/Routing/AppRoutes.tsx";
import Navigation from "./components/Routing/Navigation.tsx";

const App = () => {
  return (
    // add here any other providers
    <BrowserRouter>
      <Navigation />
      <AppRoutes />
    </BrowserRouter>
  );
};

export default App;
