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

export const admin_login = createAsyncThunk(
  'auth/admin_login',
  async (request, { rejectWithValue, fulfillWithValue }) => {
    console.log(request)
    try {
      const { data } = await api.post('/auth/login', request)
      console.log(data)
      return fulfillWithValue(data)
    } catch (error) {
      console.log(error.response.data)
      return rejectWithValue(error.response.data)
    }
  }
)

const initialState = {
  accessToken: null,
  refreshToken: null,
  error: null,
  isLoading: false,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {},
  extraReducers: builder => {
    builder
      .addCase(admin_login.pending, (state) => {
        state.isLoading = true
        state.error = null
      })
      .addCase(admin_login.rejected, (state, { payload }) => {
        state.isLoading = false;
        state.error = payload.message
      })
      .addCase(admin_login.fulfilled, (state, { payload }) => {
        state.isLoading = false;
        if (returnRole(payload.accessToken) === "Role_ADMIN") {
          state.accessToken = payload.accessToken
          state.refreshToken = payload.refreshToken
          localStorage.setItem('accessToken', payload.accessToken)
          localStorage.setItem('refreshToken', payload.refreshToken)
        } else {
          console.log("Tét")
          state.error = "This is not admin account!"
        }

      })
  }
})

const authReducer = authSlice.reducer
export default authReducer