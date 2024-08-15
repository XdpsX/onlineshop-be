import { configureStore } from "@reduxjs/toolkit"
import authReducer from "./slices/authSlice"
import categoryReducer from "./slices/categorySlice"
import vendorReducer from "./slices/vendorSlice"

const store = configureStore({
  reducer: {
    auth: authReducer,
    category: categoryReducer,
    vendor: vendorReducer
  }
})

export default store