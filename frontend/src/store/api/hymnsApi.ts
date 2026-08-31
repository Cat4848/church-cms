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
      /**
       * About `transformResponse`:
       *
       * Optional properties (authorExtras, hymnBookId, numberInHymnBook, topicId, and labelId)
       * sent as `undefined` from the frontend are stored as `null` by MySQL.
       *
       * When fetching all hymns, the repository maps `null` integer values to `0`. These zero
       * values are then stored in the frontend state. If a hymn is subsequently updated
       * without modifying its optional fields, the update fails because `numberInHymnBook`
       * must be a non-zero integer.
       *
       * To preserve the intended meaning of these optional fields, `transformResponse`
       * converts their `0`/empty values back to `undefined`.
       *
       * `authorExtras` requires separate handling: sending an empty string causes MySQL to
       * store an empty string rather than `null`. Therefore, we explicitly use `undefined`
       * for an empty value.
       *
       * This is also why `CreateOrUpdateHymnForm.tsx` sets these fields to `undefined` when
       * resetting the form state, ensuring that neutral values such as `0` and `""` are
       * treated as unset values.
       */
      transformResponse: (hymns: Hymn[]): Hymn[] => {
        return hymns.map((hymn) => {
          const transHymn: Hymn = {
            ...hymn,
            authorExtras: hymn.authorExtras ? hymn.authorExtras : undefined,
            hymnBookId: hymn.hymnBookId ? hymn.hymnBookId : undefined,
            numberInHymnBook: hymn.numberInHymnBook ? hymn.numberInHymnBook : undefined,
            topicId: hymn.topicId ? hymn.topicId : undefined,
            labelId: hymn.labelId ? hymn.labelId : undefined,
          };
          return transHymn;
        });
      },
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
