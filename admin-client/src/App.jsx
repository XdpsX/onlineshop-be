import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import './App.css'
import {
  CategoryPage,
  Dashboard,
  ErrorPage,
  LoginPage,
  ProtectedRoute,
  SharedLayout,
} from './pages'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

const router = createBrowserRouter([
  {
    path: 'login',
    element: <LoginPage />,
  },
  {
    path: '/',
    element: <Navigate to="/dashboard" />,
  },
  {
    path: 'dashboard',
    element: (
      <ProtectedRoute>
        <SharedLayout />
      </ProtectedRoute>
    ),
    children: [
      {
        index: true,
        element: <Dashboard />,
      },
      {
        path: 'categories',
        element: <CategoryPage />,
      },
    ],
  },
  {
    path: '*',
    element: <ErrorPage />,
  },
])

function App() {
  return (
    <>
      <ToastContainer position="top-center" autoClose={2000} />
      <RouterProvider router={router} />
    </>
  )
}

export default App
