import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

export interface UserState {
  firstName: string;
  lastName: string;
  email: string;
  isAuthenticated: boolean;
  isAdmin: boolean;
  csrfToken: string;
}

const initialState: UserState = {
  firstName: "",
  lastName: "",
  email: "",
  isAuthenticated: false,
  isAdmin: false,
  csrfToken: "",
};

const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    updateUserDetails(_, payload: PayloadAction<UserState>): UserState {
      return payload.payload;
    },
  },
});

export const { updateUserDetails } = userSlice.actions;
export default userSlice.reducer;
