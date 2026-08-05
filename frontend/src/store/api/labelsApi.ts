import { createApi } from "@reduxjs/toolkit/query/react";
import { sharedBaseQuery } from "./sharedBaseQuery.ts";
import type { Label } from "../../domain/Label.ts";

export const labelsApi = createApi({
  reducerPath: "labelsApi",
  tagTypes: ["Labels"],
  baseQuery: sharedBaseQuery,
  endpoints: (builder) => ({
    getAllLabels: builder.query<Label[], void>({
      query: () => "/labels",
      providesTags: ["Labels"],
    }),
    createLabel: builder.mutation<Label, Label["name"]>({
      query: (labelName: string) => ({
        url: "/labels",
        method: "POST",
        body: { name: labelName },
      }),
      invalidatesTags: ["Labels"],
    }),
    updateLabel: builder.mutation<Label, Label>({
      query: (label) => ({
        url: "/labels",
        method: "PUT",
        body: label,
      }),
      invalidatesTags: ["Labels"],
    }),
  }),
});

export const { useGetAllLabelsQuery, useCreateLabelMutation, useUpdateLabelMutation } = labelsApi;
