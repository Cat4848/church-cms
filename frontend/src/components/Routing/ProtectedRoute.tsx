import { Outlet } from "react-router";

const ProtectedRoute = () => {
  // TODO get the user from the global state and check if it is logged in
  // if logged it render the Outlet
  // if loading render the Loading component
  // if not logged in render the Unauthorised component
  return <Outlet />;
};

export default ProtectedRoute;
