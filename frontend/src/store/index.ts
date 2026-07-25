import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./slices/user";
import { authorsApi } from "./api/authorsApi.ts";
import { setupListeners } from "@reduxjs/toolkit/query";

const reducer = {
  user: userReducer,
  [authorsApi.reducerPath]: authorsApi.reducer,
};

const store = configureStore({
  reducer,
  middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(authorsApi.middleware),
  devTools: true,
});

setupListeners(store.dispatch);

export default store;
