import { useGetAllHymnBooksQuery, useUpdateHymnBookMutation } from "../../store/api/hymnBooksApi.ts";
import Loading from "../../components/Loading/Loading.tsx";
import { ErrorFallback } from "../../components/ErrorBoundary/ErrorBoundary.tsx";
import HymnBook from "./HymnBook.tsx";
import NoContent from "../../components/NoContent/NoContent.tsx";
import type { HymnBook as HymnBookInterface } from "../../domain/HymnBook.ts";
import { toast } from "react-toastify";

const HymnBooks = () => {
  const { data: hymnBooks, error, isLoading } = useGetAllHymnBooksQuery();
  const [updateHymnBook, { isLoading: isUpdating }] = useUpdateHymnBookMutation();

  if (isLoading || isUpdating) {
    return <Loading />;
  }

  if (error) {
    return <ErrorFallback error={error.toString()} />;
  }

  if (hymnBooks && !hymnBooks.length) {
    return <NoContent entity="Hymn Books" />;
  }

  const handleChange = (hymnBook: HymnBookInterface) => {
    // letters, _, _ and space
    const regExp: RegExp = new RegExp("^[a-zA-Z\\-\\_\\s]{3,100}$", "gm");

    if (regExp.test(hymnBook.name)) {
      updateHymnBook(hymnBook);
    } else {
      toast.error("Hymn Book name: only letters, '-', '_' or space");
    }
  };

  return (
    <div>
      {hymnBooks &&
        hymnBooks.length &&
        hymnBooks.map((hymnBook) => {
          return (
            <HymnBook
              key={hymnBook.hymnBookId}
              hymnBookId={hymnBook.hymnBookId}
              name={hymnBook.name}
              onChange={handleChange}
            />
          );
        })}
    </div>
  );
};

export default HymnBooks;
