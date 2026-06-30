import loginStyles from "./Login.module.css";
import formStyles from "../../css/forms.module.css";
import { useForm, type SubmitHandler } from "react-hook-form";
import { api } from "../../../config/endpoints.ts";
import { useAppDispatch } from "../../store/hooks";
import { updateUserDetails } from "../../store/slices/user";
import { type NavigateFunction, useNavigate } from "react-router";
import { toast } from "react-toastify";
import type { LoginSuccessRequestPayload } from "./types.ts";
import { useState } from "react";
import Loading from "../../components/Loading/Loading.tsx";
import MandatoryField from "../../components/MandatoryField/MandatoryField.tsx";

interface FormValues {
  email: string;
  password: string;
}

const Login = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>();

  const dispatch = useAppDispatch();
  const navigate: NavigateFunction = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  if (isLoading) {
    return <Loading />;
  }

  const handleSubmitLoginRequest: SubmitHandler<FormValues> = async (data: FormValues) => {
    setIsLoading(true);

    try {
      const res: Response = await fetch(api + "/auth/login", {
        method: "POST",
        body: JSON.stringify(data),
        credentials: "include",
      });

      if (res.ok) {
        const csrfToken: string = res.headers.get("X-CSRF-TOKEN")!;

        localStorage.setItem("csrfToken", csrfToken);

        const userDetails: LoginSuccessRequestPayload = await res.json();

        dispatch(
          updateUserDetails({
            firstName: userDetails.firstName,
            lastName: userDetails.lastName,
            email: userDetails.email,
            isAuthenticated: true,
            isAdmin: userDetails.isAdmin,
            csrfToken,
          }),
        );
        navigate("/");
      } else {
        toast.error("Incorrect email or password.");
      }
    } catch (e) {
      if (e instanceof Error) {
        // we don't want to show a long error message
        toast.error("An error occurred while logging in " + e.message.slice(1, 11));
      } else {
        toast.error("An error occurred while logging in.");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit(handleSubmitLoginRequest)} className={loginStyles["outer-container"]}>
      <div className={loginStyles["inner-container"]}>
        <div className={formStyles["container"]}>
          <h1>Login</h1>

          <div className={formStyles["input-group"]}>
            <label htmlFor="login-email-address">
              <MandatoryField /> Email Address
            </label>
            <input
              id="login-email-address"
              type="email"
              {...register("email", {
                required: "Required",
                pattern: {
                  value: new RegExp("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"),
                  message: "Not valid",
                },
              })}
            />
            <div className={formStyles["error"]}>{errors.email?.message}</div>
          </div>

          <div className={formStyles["input-group"]}>
            <label htmlFor="login-password">
              <MandatoryField /> Password
            </label>
            <input
              id="login-password"
              type="password"
              {...register("password", {
                required: "Required",
                minLength: { value: 8, message: "Min 8 characters" },
                max: { value: 255, message: "Max 255 characters" },
              })}
            />
            <div className={formStyles["error"]}>{errors.password?.message}</div>
          </div>
          <button onClick={handleSubmit(handleSubmitLoginRequest)}>Login</button>
        </div>
      </div>
    </form>
  );
};

export default Login;
