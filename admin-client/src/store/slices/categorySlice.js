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

export const addCategory = createAsyncThunk(
  'category/addCategory',
  async (request, { rejectWithValue, fulfillWithValue }) => {
    try {
      const { data } = await api.post('/categories', request)
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

export const updateCategory = createAsyncThunk(
  'category/updateCategory',
  async (request, { rejectWithValue, fulfillWithValue }) => {
    try {
      const { data } = await api.put(`/categories/${request.id}`, request)
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

export const deleteCategory = createAsyncThunk(
  'category/deleteCategory',
  async (categoryId, { rejectWithValue, fulfillWithValue }) => {
    try {
      const { data } = await api.delete(`/categories/${categoryId}`)
      return fulfillWithValue(data)
    } catch (error) {
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
  isLoading: true,
  showModal: false,
  delCategory: null,
  updCategory: null
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
    setDelCategory: (state, { payload }) => {
      state.delCategory = payload
    },
    setUpdCategory: (state, { payload }) => {
      state.updCategory = payload
    },
    setShowModal: (state, { payload }) => {
      state.showModal = payload
    }
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

export const { setSearch, setPageNumber, setSort, setDelCategory, setUpdCategory, setShowModal } = categorySlice.actions
const categoryReducer = categorySlice.reducer
export default categoryReducer