// letters, _, _ and space
export const nameRegexp: RegExp = new RegExp("^[a-zA-Z\\-\\_\\s]{3,100}$");
export const invalidNameErrorMessage: string = "only letters, '-', '_' or space";
