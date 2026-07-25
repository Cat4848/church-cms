import { createApi } from "@reduxjs/toolkit/query/react";
import type { Author } from "../../domain/Author.ts";
import { sharedBaseQuery } from "./sharedBaseQuery.ts";

export const authorsApi = createApi({
  reducerPath: "authorsApi",
  tagTypes: ["Authors"],
  baseQuery: sharedBaseQuery,
  endpoints: (builder) => ({
    getAllAuthors: builder.query<Author[], void>({
      query: () => "/authors",
      providesTags: ["Authors"],
    }),
    createAuthor: builder.mutation<Author, Author["name"]>({
      query: (authorName) => ({
        url: "/authors",
        method: "POST",
        body: { name: authorName },
      }),
      invalidatesTags: ["Authors"],
    }),
    updateAuthor: builder.mutation<Author, Author>({
      query: (author) => ({
        url: "/authors",
        method: "PUT",
        body: author,
      }),
      invalidatesTags: ["Authors"],
    }),
  }),
});

export const { useGetAllAuthorsQuery, useCreateAuthorMutation, useUpdateAuthorMutation } = authorsApi;
