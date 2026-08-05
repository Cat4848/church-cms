import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./slices/user";
import { authorsApi } from "./api/authorsApi.ts";
import { setupListeners } from "@reduxjs/toolkit/query";
import { hymnBooksApi } from "./api/hymnBooksApi.ts";
import { topicsApi } from "./api/topicsApi.ts";

const reducer = {
  user: userReducer,
  [authorsApi.reducerPath]: authorsApi.reducer,
  [hymnBooksApi.reducerPath]: hymnBooksApi.reducer,
  [topicsApi.reducerPath]: topicsApi.reducer,
};

const store = configureStore({
  reducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(authorsApi.middleware, hymnBooksApi.middleware, topicsApi.middleware),
  devTools: true,
});

setupListeners(store.dispatch);

export default store;
