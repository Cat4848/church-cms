import { configureStore } from "@reduxjs/toolkit";
import userReducer from "./slices/user";
import { authorsApi } from "./api/authorsApi.ts";
import { setupListeners } from "@reduxjs/toolkit/query";
import { hymnBooksApi } from "./api/hymnBooksApi.ts";
import { topicsApi } from "./api/topicsApi.ts";
import { labelsApi } from "./api/labelsApi.ts";
import { hymnsApi } from "./api/hymnsApi.ts";

const reducer = {
  user: userReducer,
  [authorsApi.reducerPath]: authorsApi.reducer,
  [hymnBooksApi.reducerPath]: hymnBooksApi.reducer,
  [topicsApi.reducerPath]: topicsApi.reducer,
  [labelsApi.reducerPath]: labelsApi.reducer,
  [hymnsApi.reducerPath]: hymnsApi.reducer,
};

const store = configureStore({
  reducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(
      authorsApi.middleware,
      hymnBooksApi.middleware,
      topicsApi.middleware,
      labelsApi.middleware,
      hymnsApi.middleware,
    ),
  devTools: true,
});

setupListeners(store.dispatch);

export default store;
