import { BsEye, BsEyeSlash } from 'react-icons/bs'
import logo from '../assets/react.svg'
import { useEffect, useState } from 'react'
import { Field, Form, Formik } from 'formik'
import * as Yup from 'yup'
import { useDispatch, useSelector } from 'react-redux'
import { adminLogin } from '../store/slices/authSlice'
import { BeatLoader } from 'react-spinners'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { overrideStyle } from '../utils/cssHelper'

const LoginSchema = Yup.object().shape({
  email: Yup.string()
    .email('Invalid email format')
    .max(128, 'Maximum 128 characters')
    .required('Email is required!'),
  password: Yup.string()
    .min(8, 'Minimum 8 characters')
    .max(128, 'Maximum 128 characters')
    .required('Password is required!'),
})

const initialValues = {
  email: 'admin@gmail.com',
  password: '12345678',
}

const LoginPage = () => {
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const { accessToken, error, isLoading } = useSelector((state) => state.auth)

  const [isShowPassword, setIsShowPassword] = useState(false)

  useEffect(() => {
    if (accessToken) {
      navigate('/dashboard')
    }
  }, [accessToken, navigate])

  const submitHandler = (values, { setFieldError }) => {
    dispatch(adminLogin(values))
      .unwrap()
      .then(() => {
        navigate('/')
        toast.success('Login Successfully')
      })
      .catch((err) => {
        if (err.details) {
          Object.keys(err.details).forEach((field) => {
            setFieldError(field, err.details[field])
          })
        }
      })
  }

  return (
    <div className="min-w-screen min-h-screen bg-[#f2f2f2] flex justify-center items-center">
      <div className="w-[350px] text-black py-16 px-4 shadow-lg bg-white p-4 rounded-md">
        <h2 className="text-3xl mb-6 font-bold text-center">Admin Dashboard</h2>
        <img src={logo} alt="Logo" className="m-auto h-16 mb-12" />
        <Formik
          initialValues={initialValues}
          validationSchema={LoginSchema}
          onSubmit={submitHandler}
        >
          {({ errors, touched }) => (
            <Form>
              {error && (
                <p className="text-md font-bold text-red-600 text-center mb-2">
                  {error}
                </p>
              )}
              <div className="mb-5">
                <div className="flex flex-col mb-1 w-full gap-1 border-b-2 border-slate-400">
                  <Field
                    className="px-3 py-2 outline-none bg-transparent"
                    type="email"
                    name="email"
                    placeholder="Email"
                  />
                </div>
                {errors.email && touched.email && (
                  <p className="text-sm text-red-600">{errors.email}</p>
                )}
              </div>

              <div className="mb-7">
                <div className="flex w-full mb-1 border-b-2 border-slate-400 items-center">
                  <Field
                    className="px-3 py-2 outline-none bg-transparen flex-1"
                    type={!isShowPassword ? 'password' : 'text'}
                    name="password"
                    placeholder="Password"
                    required
                  />
                  <div
                    className="cursor-pointer"
                    onClick={() => setIsShowPassword(!isShowPassword)}
                  >
                    {!isShowPassword ? <BsEye /> : <BsEyeSlash />}
                  </div>
                </div>
                {errors.password && touched.password && (
                  <p className="text-sm text-red-600">{errors.password}</p>
                )}
              </div>

              <button
                disabled={isLoading}
                className={
                  isLoading
                    ? 'bg-slate-800 w-full text-white rounded-md px-7 py-2 mb-3 opacity-90'
                    : 'bg-slate-800 w-full transition-colors hover:shadow-blue-300 hover:shadow-md text-white hover:bg-blue-500 rounded-md px-7 py-2 mb-3'
                }
              >
                {isLoading ? (
                  <BeatLoader
                    color="#fff"
                    size={12}
                    cssOverride={overrideStyle}
                  />
                ) : (
                  'Login'
                )}
              </button>
            </Form>
          )}
        </Formik>
      </div>
    </div>
  )
}
export default LoginPage
