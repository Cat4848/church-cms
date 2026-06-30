import { type JSX, useEffect } from "react";
import { useAppDispatch } from "../../store/hooks";
import { updateUserDetails } from "../../store/slices/user";
import type { UseFetchReturnValue } from "../../utils";
import type { LoginSuccessRequestPayload } from "./types.ts";
import useFetchReq from "../../utils/useFetchReq.ts";
import { api } from "../../../config/endpoints.ts";
import Loading from "../Loading/Loading.tsx";

interface Props {
  children: JSX.Element;
}

const CheckAuthenticationState = ({ children }: Props) => {
  const dispatch = useAppDispatch();
  const url: string = api + "/api/session";
  const csrfToken: string | undefined = localStorage.getItem("csrfToken") ?? undefined;

  const { res, isLoading, error }: UseFetchReturnValue<LoginSuccessRequestPayload> =
    useFetchReq<LoginSuccessRequestPayload>({
      url,
      method: "GET",
      csrfToken,
    });

  useEffect(() => {
    if (res && !error && csrfToken) {
      dispatch(updateUserDetails({ ...res, isAuthenticated: true, csrfToken }));
    }
  }, [res, error, dispatch, csrfToken]);

  if (isLoading) {
    return <Loading />;
  }

  return children;
};

export default CheckAuthenticationState;
