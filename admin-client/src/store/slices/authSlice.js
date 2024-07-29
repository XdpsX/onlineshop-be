import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import api from "../../utils/api"
import { jwtDecode } from "jwt-decode"

const returnRole = (token) => {
  if (token) {
    const decodeToken = jwtDecode(token)
    const expireTime = new Date(decodeToken.exp * 1000)
    if (new Date() > expireTime) {
      localStorage.removeItem('accessToken')
      return ''
    } else {
      return decodeToken.role
    }
  } else {
    return ''
  }
}

export const adminLogin = createAsyncThunk(
  'auth/adminLogin',
  async (request, { rejectWithValue, fulfillWithValue }) => {
    console.log(request)
    try {
      const { data } = await api.post('/auth/login', request)
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

export const getUserProfile = createAsyncThunk(
  'auth/getUserProfile',
  async (_, { rejectWithValue, fulfillWithValue }) => {

    try {
      const { data } = await api.get('/users/profile')
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

const initialState = {
  accessToken: localStorage.getItem('accessToken'),
  refreshToken: localStorage.getItem('refreshToken'),
  userProfile: null,
  error: null,
  isLoading: false,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(adminLogin.pending, (state) => {
        state.isLoading = true
        state.error = null
      })
      .addCase(adminLogin.rejected, (state, { payload }) => {
        state.isLoading = false;
        state.error = payload.message
      })
      .addCase(adminLogin.fulfilled, (state, { payload }) => {
        state.isLoading = false;
        if (returnRole(payload.accessToken) === "Role_ADMIN") {
          state.accessToken = payload.accessToken
          state.refreshToken = payload.refreshToken
          localStorage.setItem('accessToken', payload.accessToken)
          localStorage.setItem('refreshToken', payload.refreshToken)
        } else {
          state.error = "This is not admin account!"
        }
      })

      .addCase(getUserProfile.pending, (state) => {
        state.isLoading = true
      })
      .addCase(getUserProfile.fulfilled, (state, { payload }) => {
        state.isLoading = false
        state.userProfile = payload
      })
  }
})

const authReducer = authSlice.reducer
export default authReducer