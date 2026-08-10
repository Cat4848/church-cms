import { createApi } from "@reduxjs/toolkit/query/react";
import { sharedBaseQuery } from "./sharedBaseQuery.ts";
import type { Hymn } from "../../domain/Hymn.ts";

export const hymnsApi = createApi({
  reducerPath: "hymnsApi",
  tagTypes: ["Hymns"],
  baseQuery: sharedBaseQuery,
  endpoints: (builder) => ({
    getAllHymns: builder.query<Hymn[], void>({
      query: () => "/hymns",
      providesTags: ["Hymns"],
    }),
    createHymn: builder.mutation<Hymn, Omit<Hymn, "hymnId">>({
      query: (hymn: Omit<Hymn, "hymnId">) => ({
        url: "/hymns",
        method: "POST",
        body: hymn,
      }),
      invalidatesTags: ["Hymns"],
    }),
    updateHymn: builder.mutation<Hymn, Hymn>({
      query: (hymn: Hymn) => ({
        url: "/hymns",
        method: "PUT",
        body: hymn,
      }),
      invalidatesTags: ["Hymns"],
    }),
  }),
});

export const { useGetAllHymnsQuery, useCreateHymnMutation, useUpdateHymnMutation } = hymnsApi;
