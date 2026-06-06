import { useAppSelector } from "../store/hooks";
import { selectUser } from "../store/slices/user/selectors";
import { type UserState } from "../store/slices/user";
import { useState, useEffect } from "react";

interface UseFetchReqParams {
  url: string;
  method: "GET" | "POST" | "PUT" | "DELETE";
  csrfToken?: string;
  body?: object;
}

export interface UseFetchReturnValue<T> {
  res: T | undefined;
  isLoading: boolean;
  error: string | undefined;
}

const useFetchReq = <T>({ url, method, csrfToken, body }: UseFetchReqParams): UseFetchReturnValue<T> => {
  const [res, setResponse] = useState<T | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | undefined>(undefined);
  const user: UserState = useAppSelector(selectUser);

  useEffect(() => {
    (async () => {
      setIsLoading(true);

      try {
        const response: Response = await fetch(url, {
          method,
          headers: {
            "X-CSRF-TOKEN": csrfToken ?? user.csrfToken,
          },
          credentials: "include",
          ...(body && { body: JSON.stringify(body) }),
        });

        if (response.ok) {
          const data: T = await response.json();
          setResponse(data);
        } else {
          const errorMessage: string = await response.text();
          setError(errorMessage);
        }
      } catch (e) {
        if (e instanceof Error) {
          setError(e.message);
        }
      } finally {
        setIsLoading(false);
      }
    })();
  }, [url, method, csrfToken, user.csrfToken, body]);

  return { res, isLoading, error };
};

export default useFetchReq;
