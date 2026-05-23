import "./App.css";
import { BrowserRouter} from "react-router";
import AppRoutes from "./components/AppRoutes/AppRoutes.tsx";

const App = () => {
  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
  );
};

export default App;
