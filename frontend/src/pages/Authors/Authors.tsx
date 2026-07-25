import { useGetAllAuthorsQuery, useUpdateAuthorMutation } from "../../store/api/authorsApi.ts";
import Loading from "../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../components/ErrorBoundary/ErrorBoundary.tsx";
import Author from "./Author/Author.tsx";
import NoContent from "../../components/NoContent/NoContent.tsx";
import type { Author as AuthorInterface } from "../../domain/Author.ts";
import { toast } from "react-toastify";

const Authors = () => {
  const { data: authors, error, isLoading } = useGetAllAuthorsQuery();
  const [updateAuthor, { isLoading: isUpdating }] = useUpdateAuthorMutation();

  if (isLoading || isUpdating) {
    return <Loading />;
  }

  if (error) {
    return <ErrorFallback error={error.toString()} />;
  }

  if (authors && !authors.length) {
    return <NoContent entity="Authors" />;
  }

  const handleChange = (author: AuthorInterface): void => {
    const regExp: RegExp = new RegExp("^[a-zA-Z\\-\\_\\s]{3,100}$", "gm");

    if (regExp.test(author.name)) {
      updateAuthor(author);
    } else {
      toast.error("Author name: only letters, '-', '_' or space");
    }
  };

  return (
    <div>
      {authors &&
        authors.length &&
        authors.map((author) => {
          return <Author key={author.authorId} authorId={author.authorId} name={author.name} onChange={handleChange} />;
        })}
    </div>
  );
};

export default Authors;
