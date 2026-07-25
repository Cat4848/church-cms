import { fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import { api } from "../../../config/endpoints.ts";
import type { RootState } from "../types";

export const sharedBaseQuery = fetchBaseQuery({
  baseUrl: api + "/api",
  credentials: "include",
  prepareHeaders: (headers: Headers, { getState }) => {
    const state = getState() as RootState;

    headers.set("X-CSRF-TOKEN", state.user.csrfToken);

    return headers;
  },
});
