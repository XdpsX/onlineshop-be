import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './App.css'
import { LoginPage } from './pages'
import { ToastContainer } from 'react-toastify'
import 'react-toastify/dist/ReactToastify.css'

const router = createBrowserRouter([
  {
    path: 'login',
    element: <LoginPage />,
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
