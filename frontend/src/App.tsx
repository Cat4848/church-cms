import "./App.css";
import { BrowserRouter } from "react-router";
import AppRoutes from "./components/Routing/AppRoutes.tsx";
import Navigation from "./components/Routing/Navigation.tsx";
import { Provider } from "react-redux";
import store from "./store";
import { ToastContainer } from "react-toastify";

const App = () => {
  return (
    // add here any other providers
    <Provider store={store}>
      <BrowserRouter>
        <Navigation />
        <AppRoutes />
        <ToastContainer theme="dark" />
      </BrowserRouter>
    </Provider>
  );
};

export default App;
