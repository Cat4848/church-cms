import type { RootState } from "../../../types";
import type { UserState } from "../index.ts";

export const selectUser = (state: RootState): UserState => state.user;
