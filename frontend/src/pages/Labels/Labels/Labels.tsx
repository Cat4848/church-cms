import { useGetAllLabelsQuery, useCreateLabelMutation, useUpdateLabelMutation } from "../../../store/api/labelsApi.ts";
import Loading from "../../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../../components/ErrorBoundary/ErrorBoundary.tsx";
import Label from "../Label/Label.tsx";
import NoContent from "../../../components/NoContent/NoContent.tsx";
import SearchAndCreate from "../../../components/SearchAndCreate/SearchAndCreate.tsx";
import { useState } from "react";
import globalStyles from "../../../css/global.module.css";
import { nameRegexp, invalidNameErrorMessage } from "../../../lib/constants.ts";

const Labels = () => {
  const { data: labels, error: getAllLabelsError, isLoading } = useGetAllLabelsQuery();
  const [createLabel, { isLoading: isCreating, error: createError }] = useCreateLabelMutation();
  const [updateLabel, { isLoading: isUpdating, error: updateError }] = useUpdateLabelMutation();
  const [searchLabelName, setSearchLabelName] = useState("");
  const [newLabelName, setNewLabelName] = useState("");
  const [createLabelError, setCreateLabelError] = useState("");

  if (isLoading || isCreating || isUpdating) {
    return <Loading />;
  }
  if (getAllLabelsError) {
    return <ErrorFallback error={getAllLabelsError.toString()} />;
  }
  if (createError) {
    return <ErrorFallback error={createError.toString()} />;
  }
  if (updateError) {
    return <ErrorFallback error={updateError.toString()} />;
  }
  if (labels && !labels.length) {
    return <NoContent entity="Labels" />;
  }

  const handleEditSearchTerm = (searchTerm: string) => {
    setSearchLabelName(searchTerm);
  };

  const handleCreateLabel = (closeCreateForm: () => void) => {
    if (!nameRegexp.test(newLabelName)) {
      setCreateLabelError(invalidNameErrorMessage);
    } else {
      closeCreateForm();
      createLabel(newLabelName);
    }
  };

  return (
    <div>
      <SearchAndCreate
        searchTerm={searchLabelName}
        onChange={handleEditSearchTerm}
        entity="Label"
        isCreateButtonDisabled={newLabelName.length < 3 || newLabelName.length > 100}
        isCreateFormValid={!createLabelError}
        onCreate={handleCreateLabel}
        resetCreateState={() => setNewLabelName("")}
      >
        <div className={globalStyles["flex-box-column-center-gap-1"]}>
          <div className={globalStyles["flex-box-center-gap-1"]}>
            <label htmlFor="label-name">Label Name</label>
            <input
              id="label-name"
              value={newLabelName}
              onChange={(e) => {
                setCreateLabelError("");
                setNewLabelName(e.target.value);
              }}
            />
          </div>
          <span className={globalStyles["error-box"]}>{createLabelError && createLabelError}</span>
        </div>
      </SearchAndCreate>
      <div className={globalStyles["grid-two-columns"]}>
        {labels &&
          labels.length &&
          labels
            .filter((label) => label.name.toLowerCase().includes(searchLabelName.toLowerCase()))
            .map((label) => {
              return <Label key={label.labelId} labelId={label.labelId} name={label.name} onUpdate={updateLabel} />;
            })}
      </div>
    </div>
  );
};

export default Labels;
