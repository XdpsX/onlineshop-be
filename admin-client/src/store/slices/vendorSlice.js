import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"
import api from "../../utils/api"

export const getVendors = createAsyncThunk(
  'vendor/getVendors',
  async ({ pageSize, pageNum, search, sort }, { rejectWithValue, fulfillWithValue }) => {
    console.log("search: ", search)
    try {
      const { data } = await api.get('/vendors', {
        params: {
          pageSize,
          pageNum,
          search,
          sort
        }
      })
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

export const addVendor = createAsyncThunk(
  'vendor/addVendor',
  async (formData, { rejectWithValue, fulfillWithValue }) => {
    try {
      const { data } = await api.post('/vendors', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        }
      })
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

export const deleteVendor = createAsyncThunk(
  'vendor/deleteVendor',
  async (vendorId, { rejectWithValue, fulfillWithValue }) => {
    try {
      const { data } = await api.delete(`/vendors/${vendorId}`)
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

export const updateVendor = createAsyncThunk(
  'vendor/updateVendor',
  async ({ vendorId, formData }, { rejectWithValue, fulfillWithValue }) => {
    try {
      const { data } = await api.put(`/vendors/${vendorId}`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        }
      })
      return fulfillWithValue(data)
    } catch (error) {
      return rejectWithValue(error.response.data)
    }
  }
)

const initialState = {
  vendors: null,
  params: {
    pageNum: 1,
    pageSize: 5,
    search: '',
    sort: '-date',
  },
  totalItems: 0,
  totalPages: 0,
  isLoading: true,
  showModal: false,
  delVendor: null,
  updVendor: null
}

const vendorSlice = createSlice({
  name: 'vendor',
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
    setShowModal: (state, { payload }) => {
      state.showModal = payload
    },
    clearFilters: (state) => {
      state.params = initialState.params
    },
    setDelVendor: (state, { payload }) => {
      state.delVendor = payload
    },
    setUpdVendor: (state, { payload }) => {
      state.updVendor = payload
    },
  },
  extraReducers: builder => {
    builder
      .addCase(getVendors.pending, (state) => {
        state.isLoading = true
      })
      .addCase(getVendors.fulfilled, (state, { payload }) => {
        state.isLoading = false
        state.vendors = payload.items
        state.totalItems = payload.totalItems
        state.totalPages = payload.totalPages
      })
      .addCase(getVendors.rejected, (state) => {
        state.isLoading = false
      })
  }
})

export const { setSearch, setPageNumber, setSort, setShowModal, clearFilters, setDelVendor, setUpdVendor } = vendorSlice.actions
const vendorReducer = vendorSlice.reducer
export default vendorReducer