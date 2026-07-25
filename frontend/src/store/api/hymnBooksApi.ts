import { createApi } from "@reduxjs/toolkit/query/react";
import type { HymnBook } from "../../domain/HymnBook.ts";
import { sharedBaseQuery } from "./sharedBaseQuery.ts";

export const hymnBooksApi = createApi({
  reducerPath: "hymnBooksApi",
  tagTypes: ["HymnBooks"],
  baseQuery: sharedBaseQuery,
  endpoints: (builder) => ({
    getAllHymnBooks: builder.query<HymnBook[], void>({
      query: () => "/hymn-books",
      providesTags: ["HymnBooks"],
    }),
    updateHymnBook: builder.mutation<HymnBook, HymnBook>({
      query: (hymnBook: HymnBook) => ({
        url: "/hymn-books",
        method: "PUT",
        body: hymnBook,
      }),
      invalidatesTags: ["HymnBooks"],
    }),
  }),
});

export const { useGetAllHymnBooksQuery, useUpdateHymnBookMutation } = hymnBooksApi;
