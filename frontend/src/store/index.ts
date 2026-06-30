import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./slices/user";

const reducer = {
  user: userReducer,
};

const store = configureStore({
  reducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware(),
  devTools: true,
});

export default store;
