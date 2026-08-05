import { useGetAllTopicsQuery, useCreateTopicMutation, useUpdateTopicMutation } from "../../../store/api/topicsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import Topic from "../Topic/Topic.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import SearchAndCreate from "../../../components/SearchAndCreate/SearchAndCreate.tsx";
import { useState } from "react";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";

const Topics = () => {
  const { data: topics, error: getAllTopicsError, isLoading } = useGetAllTopicsQuery();
  const [createTopic, { isLoading: isCreating, error: createError }] = useCreateTopicMutation();
  const [updateTopic, { isLoading: isUpdating, error: updateError }] = useUpdateTopicMutation();
  const [searchTopicName, setSearchTopicName] = useState("");
  const [newTopicName, setNewTopicName] = useState("");
  const [createTopicError, setCreateTopicError] = useState("");

  if (isLoading || isCreating || isUpdating) {
    return <Loading />;
  }
  if (getAllTopicsError) {
    return <ErrorFallback error={getAllTopicsError.toString()} />;
  }
  if (createError) {
    return <ErrorFallback error={createError.toString()} />;
  }
  if (updateError) {
    return <ErrorFallback error={updateError.toString()} />;
  }
  if (topics && !topics.length) {
    return <NoContent entity="Topics" />;
  }

  const handleEditSearchTerm = (searchTerm: string) => {
    setSearchTopicName(searchTerm);
  };

  const handleCreateTopic = (closeCreateForm: () => void) => {
    if (!nameRegexp.test(newTopicName)) {
      setCreateTopicError(invalidNameErrorMessage);
    } else {
      closeCreateForm();
      createTopic(newTopicName);
    }
  };

  return (
    <div>
      <SearchAndCreate
        searchTerm={searchTopicName}
        onChange={handleEditSearchTerm}
        entity="Topic"
        isCreateButtonDisabled={newTopicName.length < 3 || newTopicName.length > 100}
        isCreateFormValid={!createTopicError}
        onCreate={handleCreateTopic}
        resetCreateState={() => setNewTopicName("")}
      >
        <div className={globalStyles["flex-box-column-center-gap-1"]}>
          <div className={globalStyles["flex-box-center-gap-1"]}>
            <label htmlFor="topic-name">Topic Name</label>
            <input
              id="topic-name"
              value={newTopicName}
              onChange={(e) => {
                setCreateTopicError("");
                setNewTopicName(e.target.value);
              }}
            />
          </div>
          <span className={globalStyles["error-box"]}>{createTopicError && createTopicError}</span>
        </div>
      </SearchAndCreate>
      <div className={globalStyles["grid-two-columns"]}>
        {topics &&
          topics.length &&
          topics
            .filter((topic) => topic.name.toLowerCase().includes(searchTopicName.toLowerCase()))
            .map((topic) => {
              return <Topic key={topic.topicId} topicId={topic.topicId} name={topic.name} onUpdate={updateTopic} />;
            })}
      </div>
    </div>
  );
};

export default Topics;
