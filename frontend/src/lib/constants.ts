// letters, _, _ and space
export const authorNameRegexp: RegExp = new RegExp("^[a-zA-Z\\-\\_\\s]{3,100}$");
export const invalidAuthorNameErrorMessage: string = "only letters, '-', '_' or space";
