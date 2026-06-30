import { Outlet } from "react-router";
import { useAppSelector } from "../../store/hooks";
import { selectUser } from "../../store/slices/user/selectors";
import type { UserState } from "../../store/slices/user";
import { Navigate } from "react-router";

const LoginAuthCheck = () => {
  const user: UserState = useAppSelector(selectUser);

  if (user.isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default LoginAuthCheck;
