import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import api from "../../utils/api"

export const getCategories = createAsyncThunk(
  'category/getCategories',
  async ({ pageSize, pageNum, search, sort }, { rejectWithValue, fulfillWithValue }) => {
    console.log("search: ", search)
    try {
      const { data } = await api.get('/categories', {
        params: {
          pageSize,
          pageNum,
          search,
          sort
        }
      })
      // console.log(data)
      return fulfillWithValue(data)
    } catch (error) {
      // console.log(error.response.data)
      return rejectWithValue(error.response.data)
    }
  }
)

const initialState = {
  categories: null,
  params: {
    pageNum: 1,
    pageSize: 5,
    search: '',
    sort: null,
  },
  totalItems: 0,
  totalPages: 0,
  error: null,
  isLoading: true,
}

const categorySlice = createSlice({
  name: 'category',
  initialState,
  reducers: {
    setSearch: (state, { payload }) => {
      state.params.search = payload
    },
    setPageNumber: (state, { payload }) => {
      state.params.pageNum = payload
    },
    setSort: (state, { payload }) => {
      state.params.sort = payload
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getCategories.pending, (state) => {
        state.isLoading = true
      })
      .addCase(getCategories.fulfilled, (state, { payload }) => {
        state.isLoading = false
        console.log(payload.totalItems)
        state.categories = payload.items
        state.totalItems = payload.totalItems
        state.totalPages = payload.totalPages
      })
  }
})

export const { setSearch, setPageNumber, setSort } = categorySlice.actions
const categoryReducer = categorySlice.reducer
export default categoryReducer