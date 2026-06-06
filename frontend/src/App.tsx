import "./App.css";
import { BrowserRouter } from "react-router";
import AppRoutes from "./components/Routing/AppRoutes.tsx";
import Navigation from "./components/Routing/Navigation.tsx";
import { Provider } from "react-redux";
import store from "./store";
import { ToastContainer } from "react-toastify";
import CheckAuthenticationState from "./components/Login/CheckAuthenticationState.tsx";

const App = () => {
  return (
    // add here any other providers
    <Provider store={store}>
      <CheckAuthenticationState>
        <BrowserRouter>
          <Navigation />
          <AppRoutes />
          <ToastContainer theme="dark" />
        </BrowserRouter>
      </CheckAuthenticationState>
    </Provider>
  );
};

export default App;
