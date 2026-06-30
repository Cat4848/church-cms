import "./App.css";
import { BrowserRouter } from "react-router";
import AppRoutes from "./components/Routing/AppRoutes.tsx";
import Navigation from "./components/Routing/Navigation.tsx";
import { Provider } from "react-redux";
import store from "./store";
import { ToastContainer } from "react-toastify";
import CheckAuthenticationState from "./pages/Login/CheckAuthenticationState.tsx";
import { ErrorBoundary, ErrorFallback } from "./components/ErrorBoundary/ErrorBoundary.tsx";

const App = () => {
  return (
    // add here any other providers
    <Provider store={store}>
      <ErrorBoundary fallback={<ErrorFallback />}>
        <CheckAuthenticationState>
          <BrowserRouter>
            <Navigation />
            <AppRoutes />
            <ToastContainer theme="dark" />
          </BrowserRouter>
        </CheckAuthenticationState>
      </ErrorBoundary>
    </Provider>
  );
};

export default App;
