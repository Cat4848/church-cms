import { createApi } from "@reduxjs/toolkit/query/react";
import { sharedBaseQuery } from "./sharedBaseQuery.ts";
import type { Topic } from "../../domain/Topic.ts";

export const topicsApi = createApi({
  reducerPath: "topicsApi",
  tagTypes: ["Topics"],
  baseQuery: sharedBaseQuery,
  endpoints: (builder) => ({
    getAllTopics: builder.query<Topic[], void>({
      query: () => "/topics",
      providesTags: ["Topics"],
    }),
    createTopic: builder.mutation<Topic, Topic["name"]>({
      query: (topicName: string) => ({
        url: "/topics",
        method: "POST",
        body: { name: topicName },
      }),
      invalidatesTags: ["Topics"],
    }),
    updateTopic: builder.mutation<Topic, Topic>({
      query: (topic: Topic) => ({
        url: "/topics",
        method: "PUT",
        body: topic,
      }),
      invalidatesTags: ["Topics"],
    }),
  }),
});

export const { useGetAllTopicsQuery, useCreateTopicMutation, useUpdateTopicMutation } = topicsApi;
